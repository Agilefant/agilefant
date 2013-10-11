package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.RankUnderDelegate;
import fi.hut.soberit.agilefant.business.RankingBusiness;
import fi.hut.soberit.agilefant.model.Rankable;
import fi.hut.soberit.agilefant.util.Pair;

@Transactional(readOnly = true)
@Service("rankingBusiness")
public class RankingBusinessImpl implements RankingBusiness {
    public enum RankDirection { TOP, UP, DOWN };
    
    /**
     * Get the border values of the ranks of the rankables to shift.
     * 
     * @param rankable the rankable to rank
     * @param upper 
     * @return a pair with first the lower rank and second the upper rank
     * @throws IllegalArgumentException if rankable was null
     */
    public Pair<Integer, Integer> getRankBorders(Rankable rankable,
            Rankable upper) throws IllegalArgumentException {
        validateRankable(rankable);
        RankDirection dir = findOutRankDirection(rankable, upper);
        
        int lower = 0;
        int higher = 0;
        
        if (dir == RankDirection.TOP) {
            higher = rankable.getRank() - 1;
        }
        else if (dir == RankDirection.UP) {
            lower = upper.getRank() + 1;
            higher = rankable.getRank() - 1;
        }
        else {
            lower = rankable.getRank() + 1;
            higher = upper.getRank();
        }
        
        return Pair.create(lower, higher);
    }

    private void validateRankable(Rankable rankable) {
        if (rankable == null) {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Find out the direction of the ranking.
     */
    public RankDirection findOutRankDirection(Rankable rankable, Rankable upper) {
        validateRankable(rankable);
        if (upper == null) {
            return RankDirection.TOP;    
        }
        else if (rankable.getRank() > upper.getRank()) {
            return RankDirection.UP;
        }
        return RankDirection.DOWN;
    }
    
    /**
     * Shift ranks of all the given rankables according to the direction. 
     * @param dir
     * @param rankablesToShift
     */
    public void shiftRanks(RankDirection dir,
            Collection<Rankable> rankablesToShift) {
        if (dir == null || rankablesToShift == null) {
            throw new IllegalArgumentException();
        }
        int modifier = 0;
        if (dir == RankDirection.DOWN) {
            modifier = -1;
        }
        else {
            modifier = 1;
        }
        for (Rankable rankable : rankablesToShift) {
           rankable.setRank(rankable.getRank() + modifier);
        }
    }
    
    /**
     * Get the new rank number.
     */
    public int findOutNewRank(Rankable rankable, Rankable upper,
            RankDirection dir) {
        if (dir == RankDirection.TOP) {
            return 0;
        }
        else if (dir == RankDirection.UP) {
            return upper.getRank() + 1;
        }
        return upper.getRank();
    }

    /**
     * {@inheritDoc}
     */
    public void rankToBottom(Rankable rankable, Rankable lastInRank) {
        // might be null if all tasks done.
        if (lastInRank != null) {
            rankable.setRank(lastInRank.getRank() + 1);
        }
        else {
            rankable.setRank(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void rankUnder(Rankable entry, Rankable upperEntry,
            RankUnderDelegate delegate) {
        
        RankDirection dir = findOutRankDirection(entry, upperEntry);
        int newRank = findOutNewRank(entry, upperEntry, dir);
        Pair<Integer, Integer> borders = getRankBorders(entry, upperEntry);
        
        Collection<Rankable> shiftables = new ArrayList<Rankable>();
        shiftables.addAll(delegate.getWithRankBetween(borders.first, borders.second));

        shiftRanks(dir, shiftables);
        entry.setRank(newRank);
    }
    
}
