package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.impl.StoryHierarchyBusinessImpl;
import fi.hut.soberit.agilefant.db.StoryHierarchyDAO;
import fi.hut.soberit.agilefant.exception.StoryTreeIntegrityViolationException;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.MockedTestCase;
import fi.hut.soberit.agilefant.test.TestedBean;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.transfer.StoryTreeBranchMetrics;
import fi.hut.soberit.agilefant.util.StoryFilters;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
public class StoryHierarchyBusinessTest extends MockedTestCase {

    @TestedBean
    private StoryHierarchyBusinessImpl storyHierarchyBusiness;

    @Mock(strict = true)
    private StoryHierarchyDAO storyHierarchyDAO;

    @Mock(strict = true)
    private StoryBusiness storyBusiness;

    @Mock(strict = true)
    private BacklogBusiness backlogBusiness;

    @Mock(strict = true)
    private StoryFilterBusiness storyFilterBusiness;
    
    @Mock(strict = true)
    private StoryTreeIntegrityBusiness storyTreeIntegrityBusiness;

    private List<Story> children;

    private Story story;
    private Story reference;
    private Story story1;
    private Story story2;
    private Story story3;

    private Story oldParent;
    private Story story4;

    @Before
    public void setUp_storyList() {
        story1 = new Story();
        story1.setId(1);
        story2 = new Story();
        story2.setId(2);
        story3 = new Story();
        story3.setId(3);

        children = new ArrayList<Story>();

        story = new Story();
        story.setId(4);
        reference = new Story();
        reference.setId(5);

        story4 = new Story();
        story4.setId(6);
        oldParent = new Story();

    }

    @Test
    @DirtiesContext
    public void testRetrieveProjectLeafStories() {
        Project proj = new Project();
        List<Story> stories = new ArrayList<Story>();
        expect(storyHierarchyDAO.retrieveProjectLeafStories(proj)).andReturn(
                stories);
        replayAll();
        assertSame(stories, storyHierarchyBusiness
                .retrieveProjectLeafStories(proj));
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testRetrieveProjectRootStories() {
        int projectId = 100;
        List<Story> stories = new ArrayList<Story>();
        expect(storyHierarchyDAO.retrieveProjectRootStories(projectId))
                .andReturn(stories);
        replayAll();
        assertEquals(stories, storyHierarchyBusiness
                .retrieveProjectRootStories(projectId, null));
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testRetrieveProductRootStories() {
        int productId = 100;
        List<Story> stories = new ArrayList<Story>();
        expect(storyHierarchyDAO.retrieveProductRootStories(productId))
                .andReturn(stories);
        replayAll();
        assertSame(stories, storyHierarchyBusiness.retrieveProductRootStories(
                productId, null));
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testRetrieveProjectRootStories_withStoryFilters() {
        int projectId = 100;
        StoryFilters storyFilters = new StoryFilters(null, null);
        List<Story> stories = new ArrayList<Story>();
        expect(storyHierarchyDAO.retrieveProjectRootStories(projectId))
                .andReturn(stories);
        expect(storyFilterBusiness.filterStories(stories, storyFilters))
                .andReturn(stories);
        replayAll();
        assertEquals(stories, storyHierarchyBusiness
                .retrieveProjectRootStories(projectId, storyFilters));
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testRetrieveProductRootStories_withStoryFilters() {
        int productId = 100;
        StoryFilters storyFilters = new StoryFilters(null, null);
        List<Story> stories = new ArrayList<Story>();
        expect(storyHierarchyDAO.retrieveProductRootStories(productId))
                .andReturn(stories);
        expect(storyFilterBusiness.filterStories(stories, storyFilters))
                .andReturn(stories);
        replayAll();
        assertSame(stories, storyHierarchyBusiness.retrieveProductRootStories(
                productId, storyFilters));
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testMoveUnder() {
        story1.setParent(reference);
        story2.setParent(reference);
        story3.setParent(reference);
        children.add(story1);
        children.add(story2);
        children.add(story3);
        reference.setChildren(children);

        story.setParent(oldParent);
        oldParent.getChildren().add(story);
        oldParent.getChildren().add(story4);
        
        storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story, reference);
        expect(storyBusiness.updateStoryRanks(oldParent)).andReturn(null);
        expect(storyBusiness.updateStoryRanks(reference)).andReturn(null);

        replayAll();

        storyHierarchyBusiness.moveUnder(story, reference);

        verifyAll();

        assertTrue(reference.getChildren().contains(story));
        assertFalse(oldParent.getChildren().contains(story));
        assertSame(reference, story.getParent());

        assertEquals(1, oldParent.getChildren().size());
        assertEquals(0, oldParent.getChildren().get(0).getTreeRank());

        assertEquals(4, reference.getChildren().size());
        assertEquals(story, reference.getChildren().get(0)); //newest at top
        assertEquals(3, reference.getChildren().get(3).getTreeRank());
    }

    @Test(expected=StoryTreeIntegrityViolationException.class)
    @DirtiesContext
    public void testMoveUnder_treeConstraint() {
        story1.setParent(reference);
        story2.setParent(reference);
        story3.setParent(reference);
        children.add(story1);
        children.add(story2);
        children.add(story3);
        reference.setChildren(children);

        story.setParent(oldParent);
        oldParent.getChildren().add(story);
        oldParent.getChildren().add(story4);
        
        storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story, reference);
        EasyMock.expectLastCall().andThrow(new StoryTreeIntegrityViolationException(null));
        replayAll();

        storyHierarchyBusiness.moveUnder(story, reference);

        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testMoveUnder_emptiesOld() {
        reference.setChildren(children);
        story1.setParent(reference);
        story2.setParent(reference);
        story3.setParent(reference);
        children.add(story1);
        children.add(story2);
        children.add(story3);

        story.setParent(oldParent);
        oldParent.getChildren().add(story);

        storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story, reference);
        expect(storyBusiness.updateStoryRanks(oldParent)).andReturn(null);
        expect(storyBusiness.updateStoryRanks(reference)).andReturn(null);

        replayAll();

        storyHierarchyBusiness.moveUnder(story, reference);

        verifyAll();

        assertTrue(reference.getChildren().contains(story));
        assertFalse(oldParent.getChildren().contains(story));
        assertSame(reference, story.getParent());

        assertEquals(0, oldParent.getChildren().size());

        assertEquals(4, reference.getChildren().size());
        assertEquals(story, reference.getChildren().get(0)); //newest at top
        assertEquals(3, reference.getChildren().get(3).getTreeRank());
    }

    @Test
    @DirtiesContext
    public void testMoveUnder_toempty() {
        reference.setChildren(children);

        story.setParent(oldParent);
        oldParent.getChildren().add(story);
        oldParent.getChildren().add(story4);

        storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story, reference);
        expect(storyBusiness.updateStoryRanks(oldParent)).andReturn(null);
        expect(storyBusiness.updateStoryRanks(reference)).andReturn(null);

        replayAll();

        storyHierarchyBusiness.moveUnder(story, reference);

        verifyAll();

        assertTrue(reference.getChildren().contains(story));
        assertFalse(oldParent.getChildren().contains(story));
        assertSame(reference, story.getParent());

        assertEquals(1, oldParent.getChildren().size());
        assertEquals(0, oldParent.getChildren().get(0).getTreeRank());

        assertEquals(1, reference.getChildren().size());
        assertEquals(story, reference.getChildren().get(0));
        assertEquals(0, reference.getChildren().get(0).getTreeRank());
    }

    @Test
    @DirtiesContext
    public void testRankAfter() {
        Story parent = new Story();
        story.setParent(parent);
        reference.setParent(parent);
        parent.setChildren(children);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        storyHierarchyBusiness.moveAfter(story, reference);
        assertEquals(0, story1.getTreeRank());
        assertEquals(1, reference.getTreeRank());
        assertEquals(2, story.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

    }

    @Test
    @DirtiesContext
    public void testRankAfter_move() {
        Story parent = new Story();
        story.setParent(oldParent);
        reference.setParent(parent);
        parent.setChildren(children);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        oldParent.getChildren().add(story4);

        storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story, parent);
        expect(storyBusiness.updateStoryRanks(oldParent)).andReturn(null);
        expect(storyBusiness.updateStoryRanks(parent)).andReturn(null);

        replayAll();

        storyHierarchyBusiness.moveAfter(story, reference);

        verifyAll();

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, reference.getTreeRank());
        assertEquals(2, story.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

        assertEquals(0, story4.getTreeRank());
        assertEquals(1, oldParent.getChildren().size());

        assertEquals(parent, story.getParent());

    }

    @Test
    @DirtiesContext
    public void testRankAfter_moveAsRoot() {
        Product product = new Product();
        Project proj = new Project();
        story.setParent(oldParent);
        reference.setParent(null);
        story.setBacklog(proj);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        expect(backlogBusiness.getParentProduct(story.getBacklog())).andReturn(
                product);
        expect(storyHierarchyDAO.retrieveProductRootStories(product.getId()))
                .andReturn(children);
        expect(storyBusiness.updateStoryRanks(oldParent)).andReturn(null);

        replayAll();

        storyHierarchyBusiness.moveAfter(story, reference);

        verifyAll();

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, reference.getTreeRank());
        assertEquals(2, story.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

        assertNull(story.getParent());

    }

    @Test
    @DirtiesContext
    public void testRankAfter_rootStories() {
        Product product = new Product();
        Project proj = new Project();
        story.setParent(null);
        reference.setParent(null);
        story.setBacklog(proj);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        expect(backlogBusiness.getParentProduct(story.getBacklog())).andReturn(
                product);
        expect(storyHierarchyDAO.retrieveProductRootStories(product.getId()))
                .andReturn(children);

        replayAll();

        storyHierarchyBusiness.moveAfter(story, reference);

        verifyAll();

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, reference.getTreeRank());
        assertEquals(2, story.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

    }

    @Test
    @DirtiesContext
    public void testRankAfter_last() {
        Story parent = new Story();
        story.setParent(parent);
        reference.setParent(parent);
        parent.setChildren(children);

        children.add(story1);
        children.add(story2);
        children.add(story3);
        children.add(reference);

        storyHierarchyBusiness.moveAfter(story, reference);

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, story2.getTreeRank());
        assertEquals(2, story3.getTreeRank());
        assertEquals(3, reference.getTreeRank());
        assertEquals(4, story.getTreeRank());
    }

    @Test
    @DirtiesContext
    public void testRankBefore() {
        Story parent = new Story();
        story.setParent(parent);
        reference.setParent(parent);
        parent.setChildren(children);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        storyHierarchyBusiness.moveBefore(story, reference);

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, story.getTreeRank());
        assertEquals(2, reference.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

    }

    @Test
    @DirtiesContext
    public void testRankBefore_move() {
        Story parent = new Story();
        story.setParent(oldParent);
        reference.setParent(parent);
        parent.setChildren(children);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        oldParent.getChildren().add(story4);

        storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story, parent);
        expect(storyBusiness.updateStoryRanks(oldParent)).andReturn(null);
        expect(storyBusiness.updateStoryRanks(parent)).andReturn(null);

        replayAll();

        storyHierarchyBusiness.moveBefore(story, reference);

        verifyAll();

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, story.getTreeRank());
        assertEquals(2, reference.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

        assertEquals(0, story4.getTreeRank());
        assertEquals(1, oldParent.getChildren().size());

        assertEquals(parent, story.getParent());

    }

    @Test
    @DirtiesContext
    public void testRankBefore_moveAsRoot() {
        Product product = new Product();
        Project proj = new Project();
        story.setParent(oldParent);
        reference.setParent(null);
        story.setBacklog(proj);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        expect(backlogBusiness.getParentProduct(story.getBacklog())).andReturn(
                product);
        expect(storyHierarchyDAO.retrieveProductRootStories(product.getId()))
                .andReturn(children);
        expect(storyBusiness.updateStoryRanks(oldParent)).andReturn(null);

        replayAll();

        storyHierarchyBusiness.moveBefore(story, reference);

        verifyAll();

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, story.getTreeRank());
        assertEquals(2, reference.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

        assertNull(story.getParent());

    }

    @Test
    @DirtiesContext
    public void testRankBefore_rootStories() {
        Product product = new Product();
        Project proj = new Project();
        story.setParent(null);
        reference.setParent(null);
        story.setBacklog(proj);

        children.add(story1);
        children.add(reference);
        children.add(story2);
        children.add(story3);

        expect(backlogBusiness.getParentProduct(story.getBacklog())).andReturn(
                product);
        expect(storyHierarchyDAO.retrieveProductRootStories(product.getId()))
                .andReturn(children);

        replayAll();

        storyHierarchyBusiness.moveBefore(story, reference);

        verifyAll();

        assertEquals(0, story1.getTreeRank());
        assertEquals(1, story.getTreeRank());
        assertEquals(2, reference.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

    }

    @Test
    @DirtiesContext
    public void testRankBefore_first() {
        Story parent = new Story();
        story.setParent(parent);
        reference.setParent(parent);
        parent.setChildren(children);

        children.add(reference);
        children.add(story1);
        children.add(story2);
        children.add(story3);

        storyHierarchyBusiness.moveBefore(story, reference);

        assertEquals(0, story.getTreeRank());
        assertEquals(1, reference.getTreeRank());
        assertEquals(2, story1.getTreeRank());
        assertEquals(3, story2.getTreeRank());
        assertEquals(4, story3.getTreeRank());

    }

    @Test
    @DirtiesContext
    public void testReplaceStoryNodesWithRoots() {
        List<Story> stories = new ArrayList<Story>();

        // INPUT:
        // 1 -> [4]
        // 1 -> 2 -> [3]
        // OUTPUT:
        // 1
        // \- 2
        // ___\- 3
        // \- 4
        story.getChildren().add(story2);
        story2.setParent(story);
        story.getChildren().add(story4);
        story4.setParent(story);
        story2.getChildren().add(story3);
        story3.setParent(story2);

        stories.add(story4);
        stories.add(story3);
        List<Story> results = storyHierarchyBusiness
                .replaceStoryNodesWithRoots(stories);
        for (Story result : results) {
            System.out.println(result.getId());
        }

        assertEquals(1, results.size());
        Story result1 = results.get(0);
        assertEquals(story.getId(), result1.getId());
        assertEquals(2, result1.getChildren().size());
        Story result1_child1 = result1.getChildren().get(0);
        Story result1_child2 = result1.getChildren().get(1);
        Story result2;
        Story result4;
        if (result1_child1.getId() == story2.getId()) {
            result2 = result1_child1;
            result4 = result1_child2;
        } else {
            result2 = result1_child2;
            result4 = result1_child1;
        }

        assertEquals(story2.getId(), result2.getId());
        assertEquals(story4.getId(), result4.getId());

        assertEquals(0, story4.getChildren().size());
        assertEquals(1, story2.getChildren().size());

        Story result3 = story2.getChildren().get(0);

        assertEquals(story3.getId(), result3.getId());
    }

    @Test
    @DirtiesContext
    public void testReplaceStoryNodesWithRoots_emptyList() {
        List<Story> stories = new ArrayList<Story>();
        List<Story> results = storyHierarchyBusiness
                .replaceStoryNodesWithRoots(stories);

        assertTrue(results.isEmpty());
    }
    
    
    @Test
    @DirtiesContext
    public void testUpdateParentStoryTreeRanks() {
        Story parent = new Story();
        
        Story child1 = new Story();
        Story child2 = new Story();
        Story child3 = new Story();
        child1.setTreeRank(0);
        child2.setTreeRank(2);
        child3.setTreeRank(3);
        
        parent.setChildren(new ArrayList<Story>(Arrays.asList(child1, child2,
                child3)));
        
        replayAll();
        storyHierarchyBusiness.updateChildrenTreeRanks(parent);
        verifyAll();
        
        assertEquals(0, child1.getTreeRank());
        assertEquals(1, child2.getTreeRank());
        assertEquals(2, child3.getTreeRank());
    }
    
    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testUpdateParentStoryTreeRanks_nullStory() {
        replayAll();
        storyHierarchyBusiness.updateChildrenTreeRanks(null);
        verifyAll();
    }
    
    
    @Test
    @DirtiesContext
    public void testRecurseHierarchy_topmostStory() {
        story1.setParent(null);
        story1.setChildren(Arrays.asList(story2));
        story2.setParent(story1);
        
        replayAll();
        StoryTO actual = storyHierarchyBusiness.recurseHierarchy(story1);
        verifyAll();
        
        assertNull("Story's parent not null", actual.getParent());
        assertEquals("Story's children not empty", 0, actual.getChildren().size());
    }
    
    @Test
    @DirtiesContext
    public void testRecurseHierarchy() {
        story1.setParent(null);
        story1.setChildren(Arrays.asList(story2, story4));
        story4.setParent(story1);
        story2.setParent(story1);
        story2.setChildren(Arrays.asList(story3));
        story3.setParent(story3);
        
        replayAll();
        StoryTO actual = storyHierarchyBusiness.recurseHierarchy(story2);
        verifyAll();
        
        assertNull("Story's parent not null", actual.getParent());
        assertEquals("Story's children empty", 1, actual.getChildren().size());
        assertEquals("Child story's children not empty", 0, actual.getChildren().get(0).getChildren().size());
    }
    
    @Test
    @DirtiesContext
    public void testCalculateStoryTreeMetrics() {
       Story root = new Story();
       root.setStoryPoints(20);
       root.setId(1000);
       
       Story child1 = new Story();
       child1.setStoryPoints(7);
       child1.setId(1);
       
       Story child11 = new Story();
       child11.setStoryPoints(4);
       child11.setId(11);
       
       Story child12 = new Story();
       child12.setStoryPoints(4);
       child12.setState(StoryState.DONE);
       child12.setId(12);
       
       Story child2 = new Story();
       child2.setStoryPoints(14);
       child2.setState(StoryState.DONE);
       child2.setId(2);
       
       Story child21 = new Story();
       child21.setStoryPoints(8);
       child21.setState(StoryState.DONE);
       child21.setId(21);
       
       Story child22 = new Story();
       child22.setStoryPoints(4);
       child22.setState(StoryState.DONE);
       child22.setId(22);
       
       Story child3 = new Story();
       child3.setStoryPoints(10);
       child3.setId(3);
       
       root.setChildren(Arrays.asList(child1, child2, child3));
       child1.setChildren(Arrays.asList(child11, child12));
       child2.setChildren(Arrays.asList(child21, child22));
       
       replayAll();
       StoryTreeBranchMetrics metrics = this.storyHierarchyBusiness.calculateStoryTreeMetrics(root);
       verifyAll();
       
       assertEquals(18, metrics.estimatedDonePoints);
       assertEquals(32, metrics.estimatedPoints);
       assertEquals(16, metrics.doneLeafPoints);
       assertEquals(30, metrics.leafPoints);
       
    }
    
}
