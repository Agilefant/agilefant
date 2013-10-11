package fi.hut.soberit.agilefant.business;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import fi.hut.soberit.agilefant.business.impl.RankingBusinessImpl;
import fi.hut.soberit.agilefant.business.impl.RankingBusinessImpl.RankDirection;
import fi.hut.soberit.agilefant.model.Rankable;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.util.Pair;

public class RankingBusinessTest {

    private RankingBusinessImpl rankingBusiness = new RankingBusinessImpl();

    private Rankable rankable1;
    private Rankable rankable2;
    private Rankable rankable3;
    private Rankable rankable4;
    
    @Before
    public void setUp_data() {
        rankable1 = new Task();
        rankable2 = new Task();
        rankable3 = new Task();
        rankable4 = new Task();
    }
    
    /*
     * GET RANK BORDERS
     */
    @Test
    public void testGetRankBorders_toTop() {
        // Expected border values: (0, ownRank - 1)
        rankable1.setRank(333);
        Pair<Integer, Integer> actual = rankingBusiness.getRankBorders(rankable1, null);
        assertEquals(0, actual.first.intValue());
        assertEquals(332, actual.second.intValue());
    }
    
    @Test
    public void testGetRankBorders_up() {
        // Expected border values: (upperRank + 1, ownRank - 1)
        rankable1.setRank(287);
        rankable2.setRank(150);
        
        Pair<Integer, Integer> actual = rankingBusiness.getRankBorders(rankable1, rankable2);
        assertEquals(151, actual.first.intValue());
        assertEquals(286, actual.second.intValue());
    }
    
    @Test
    public void testGetRankBorders_down() {
        // Expected border values: (ownRank + 1, upper)
        rankable1.setRank(117);
        rankable2.setRank(3);
        
        Pair<Integer, Integer> actual = rankingBusiness.getRankBorders(rankable2, rankable1);
        assertEquals(4, actual.first.intValue());
        assertEquals(117, actual.second.intValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetRankBorders_nullRankable() {
        rankingBusiness.getRankBorders(null, null);
    }
    
    
    /*
     * FIND OUT RANK DIRECTION
     */
    
    @Test
    public void testFindOutRankDirection_toTop() {
        assertEquals(RankDirection.TOP, rankingBusiness.findOutRankDirection(rankable1, null));
    }
    
    @Test
    public void testFindOutRankDirection_up() {
        rankable1.setRank(5);
        rankable2.setRank(1);
        assertEquals(RankDirection.UP, rankingBusiness.findOutRankDirection(rankable1, rankable2));
    }
    
    @Test
    public void testFindOutRankDirection_down() {
        rankable1.setRank(5);
        rankable2.setRank(1);
        assertEquals(RankDirection.DOWN, rankingBusiness.findOutRankDirection(rankable2, rankable1));
    }
    
    @Test
    public void testFindOutRankDirection_sameRank() {
        rankable1.setRank(5);
        rankable2.setRank(5);
        assertEquals(RankDirection.DOWN, rankingBusiness.findOutRankDirection(rankable2, rankable1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindOutRankDirection_nullArguments() {
        rankingBusiness.findOutRankDirection(null, null);
    }
    
    
    /*
     * SHIFT RANKS
     */
    private void assertShiftRanks(int originalFirst, int originalSecond,
            int expectedFirst, int expectedSecond, RankDirection dir) {
        rankable1.setRank(originalFirst);
        rankable2.setRank(originalSecond);
        
        rankingBusiness.shiftRanks(dir, Arrays.asList(rankable1, rankable2));
        
        assertEquals(expectedFirst, rankable1.getRank().intValue());
        assertEquals(expectedSecond, rankable2.getRank().intValue());
    }
    
    @Test
    public void testShiftRanks_toTop() {
        assertShiftRanks(137, 337, 138, 338, RankDirection.TOP);
    }
   
    
    @Test
    public void testShiftRanks_upwards() {
        assertShiftRanks(127, 381, 128, 382, RankDirection.UP);
    }
    
    @Test
    public void testShiftRanks_downwards() {
        assertShiftRanks(521, 985, 520, 984, RankDirection.DOWN);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testShiftRanks_noDirection() {
        rankingBusiness.shiftRanks(null, new ArrayList<Rankable>());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testShiftRanks_noRankables() {
        rankingBusiness.shiftRanks(RankDirection.UP, null);
    }
    
    
    /*
     * FIND OUT NEW RANK 
     */
    @Test
    public void testFindOutNewRank_top() {
        assertEquals(0, rankingBusiness.findOutNewRank(rankable1, null, RankDirection.TOP));
    }
    
    @Test
    public void testFindOutNewRank_up() {
        rankable1.setRank(120);
        rankable2.setRank(33);
        assertEquals(34, rankingBusiness.findOutNewRank(rankable1, rankable2, RankDirection.UP));
    }
    
    @Test
    public void testFindOutNewRank_down() {
        rankable1.setRank(713);
        rankable2.setRank(97);
        assertEquals(713, rankingBusiness.findOutNewRank(rankable2, rankable1, RankDirection.DOWN));
    }
    
    @Test
    public void testRankToBottom() {
        rankable1.setRank(2);
        rankable2.setRank(4);
        
        rankingBusiness.rankToBottom(rankable1, rankable2);
        
        assertEquals(5, rankable1.getRank().intValue());
    }

    @Test
    public void testRankToBottom_nothingBefore() {
        rankable1.setRank(2);
        
        rankingBusiness.rankToBottom(rankable1, null);
        
        assertEquals(0, rankable1.getRank().intValue());
    }

    @Test
    public void testRankUnder() {
        rankable1.setRank(1);
        rankable2.setRank(2);
        rankable3.setRank(3);
        rankable4.setRank(4);
        
        rankingBusiness.rankUnder(rankable1, rankable3, new RankUnderDelegate() {
            public Collection<? extends Rankable> getWithRankBetween(
                    Integer first, Integer second) {
                assertEquals(2, first.intValue());
                assertEquals(3, second.intValue());
                
                return Arrays.asList(new Rankable[] { rankable2, rankable3 }); 
            }
        });
        
        assertEquals(3, rankable1.getRank().intValue());
    }
}
