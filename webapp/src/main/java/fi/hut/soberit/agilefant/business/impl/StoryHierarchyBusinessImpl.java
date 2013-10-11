package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.StoryFilterBusiness;
import fi.hut.soberit.agilefant.business.StoryHierarchyBusiness;
import fi.hut.soberit.agilefant.business.StoryTreeIntegrityBusiness;
import fi.hut.soberit.agilefant.db.StoryHierarchyDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryHourEntry;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskHourEntry;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.transfer.StoryTreeBranchMetrics;
import fi.hut.soberit.agilefant.util.StoryFilters;

@Service("storyHierarchyBusiness")
public class StoryHierarchyBusinessImpl implements StoryHierarchyBusiness {

    @Autowired
    private StoryHierarchyDAO storyHierarchyDAO;

    @Autowired
    private StoryBusiness storyBusiness;

    @Autowired
    private BacklogBusiness backlogBusiness;

    @Autowired
    private StoryFilterBusiness storyFilterBusiness;
    
    @Autowired
    private StoryTreeIntegrityBusiness storyTreeIntegrityBusiness;
   

    @Transactional(readOnly = true)
    public List<Story> retrieveProjectLeafStories(Project project) {
        return storyHierarchyDAO.retrieveProjectLeafStories(project);
    }

    @Transactional
    public void moveUnder(Story story, Story reference) {
        Story oldParent = story.getParent();
        this.storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story,
                reference);
        if (oldParent != null) {
            oldParent.getChildren().remove(story);
        }
        reference.getChildren().add(0, story);
        story.setParent(reference);

        updateBacklogRanks(oldParent);
        updateBacklogRanks(reference);

        updateTreeRanks(reference.getChildren());
        if (oldParent != null) {
            updateTreeRanks(oldParent.getChildren());
        }

    }

    @Transactional
    public void moveAfter(Story story, Story reference) {
        Story oldParent = story.getParent();
        Story parent = reference.getParent();

        LinkedList<Story> tmpList = retrieveChildListAndMoveStory(story,
                oldParent, parent);

        if (tmpList.size() != 0 && tmpList.getLast() != reference) {
            tmpList.add(tmpList.indexOf(reference) + 1, story);
        } else {
            tmpList.addLast(story);
        }

        updateTreeRanks(tmpList);
        if (parent != null) {
            parent.setChildren(tmpList);
            if (parent != oldParent) {
                updateBacklogRanks(parent);
            }
        }

    }

    
    @Transactional
    public void moveBefore(Story story, Story reference) {        
        Story oldParent = story.getParent();
        Story parent = reference.getParent();
        LinkedList<Story> tmpList = retrieveChildListAndMoveStory(story,
                oldParent, parent);

        if (tmpList != null && tmpList.indexOf(reference) >= 0) {
            tmpList.add(tmpList.indexOf(reference), story);

            updateTreeRanks(tmpList);
            if (parent != null) {
                parent.setChildren(tmpList);
                if (parent != oldParent) {
                    updateBacklogRanks(parent);
                }
            }
        }
    }

    @Transactional
    public void moveToBottom(Story story) {
        //Product prod = backlogBusiness.getParentProduct(story.getBacklog());
        Backlog rankBacklog = story.getBacklog();
        if (rankBacklog == null) {
            rankBacklog = story.getIteration();
        }
        int rootParentId = backlogBusiness.getRootParentId(rankBacklog);
        int maxRank = storyHierarchyDAO.getMaximumTreeRank(rootParentId);
        story.setTreeRank(maxRank + 1);
    }
    
    @Transactional
    public void moveToTop(Story story) {
        // parent -> not root story, move to top of parent
        Story parent = story.getParent();
        Story firstSibling = null;
        if (parent == null) {             
            Product prod = backlogBusiness.getParentProduct(story.getBacklog());  
            if(prod == null){  //standalone iteration
                Set<Story> stories = story.getIteration().getAssignedStories();
                if(stories.size() == 0){
                    firstSibling = null;
                } else {
                    firstSibling = stories.iterator().next();
                }
            } else {
                firstSibling = this.retrieveProductRootStories(prod.getId(), null).get(0);
            }
        } else {            
            firstSibling = parent.getChildren().get(0);
        }
        
        if (firstSibling != null) {
            this.moveBefore(story, firstSibling);
        }
        // root story
        /*
        Product prod = backlogBusiness.getParentProduct(story.getBacklog());
        List<Story> stories = retrieveProductRootStories(prod.getId(), null);        
        this.moveBefore(story, stories.get(0));
        */
    }
    
    
    private LinkedList<Story> retrieveChildListAndMoveStory(Story story,
            Story oldParent, Story parent) {
        LinkedList<Story> tmpList = new LinkedList<Story>();
       /* if (story.getIteration()!=null && story.getIteration().isStandAlone()) {
            tmpList.addAll(storyBusiness.retrieveStoriesInIteration(story.getIteration()));
            if (tmpList.contains(story)) {
                tmpList.remove(story);
            }
            return tmpList;
        }*/
        if (parent != oldParent) {
            if(parent != null) {
                this.storyTreeIntegrityBusiness.checkChangeParentStoryAndThrow(story, parent);
            }
            story.setParent(parent);
            if (oldParent != null) {
                oldParent.getChildren().remove(story);
                updateBacklogRanks(oldParent);
                updateTreeRanks(oldParent.getChildren());
            }
        }
        if (parent != null) {  // account for root stories
            tmpList.addAll(parent.getChildren());
        } else if (story.getBacklog() != null) { // AND standalone iterations
            Product product = backlogBusiness.getParentProduct(story
                    .getBacklog());
            tmpList.addAll(this.retrieveProductRootStories(product.getId(),
                    null));
        }
        if (tmpList.contains(story)) {
            tmpList.remove(story);
        }
        return tmpList;
    }

    private void updateBacklogRanks(Story story) {
        if (story != null) {
            storyBusiness.updateStoryRanks(story);
        }

    }

    private void updateTreeRanks(List<Story> tmpList) {
        int currentRank = 0;
        for (Iterator<Story> iter = tmpList.iterator(); iter.hasNext(); currentRank++) {
            Story tmp = iter.next();
            if (tmp.getTreeRank() != currentRank) {
                tmp.setTreeRank(currentRank);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Story> retrieveProductRootStories(int productId,
            StoryFilters storyFilters) {
        List<Story> stories = storyHierarchyDAO
                .retrieveProductRootStories(productId);
        if (storyFilters != null) {
            return storyFilterBusiness.filterStories(stories, storyFilters);
        } else {
            return stories;
        }
    }

    @Transactional(readOnly = true)
    public List<Story> retrieveProjectRootStories(int projectId,
            StoryFilters storyFilters) {
        List<Story> stories = storyHierarchyDAO
                .retrieveProjectRootStories(projectId);
        if (storyFilters != null) {
            stories = storyFilterBusiness.filterStories(stories, storyFilters);
        }
        try {
            stories = replaceStoryNodesWithRoots(stories);
            sortStoriesByTreeRank(stories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stories;
    }
    
    private void sortStoriesByTreeRank(List<Story> stories) {
        Collections.sort(stories, new PropertyComparator("treeRank", true, true));
        for (Story story: stories) {
            if (story.getChildren().size() > 0) {
                sortStoriesByTreeRank(story.getChildren());
            }
        }
    }

    public List<Story> replaceStoryNodesWithRoots(List<Story> stories) {
        List<Story> results = new ArrayList<Story>(stories.size());
        Map<Integer, Story> addedTos = new HashMap<Integer, Story>();
        for (Story story : stories) {
            Story result = story;
            while (result.getParent() != null) {
                Story parent = result.getParent();
                Story alreadyAdded = addedTos.get(parent.getId());
                if (alreadyAdded != null) {
                    List<Story> childList = new ArrayList<Story>(alreadyAdded
                            .getChildren().size() + 1);
                    childList.addAll(alreadyAdded.getChildren());
                    childList.add(result);
                    alreadyAdded.setChildren(childList);
                    result = null;
                    break;
                } else {
                    StoryTO to = new StoryTO(parent);
                    addedTos.put(to.getId(), to);
                    to.setChildren(Arrays.asList(result));
                    result = to;
                }
            }
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }
    
    
    @Transactional
    /** {@inheritDoc} */
    public void updateChildrenTreeRanks(Story story) {
        if (story == null) {
            throw new IllegalArgumentException("No null stories allowed");
        }
        int newRank = 0;
        for (Story tmpStory : story.getChildren()) {
            tmpStory.setTreeRank(newRank++);
        }
    } 

    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public StoryTO recurseHierarchy(Story story) {
        StoryTO returned = null;
        StoryTO transfer = null;
        StoryTO old = null;
        
        for (Story iterator = story; iterator != null; iterator = iterator.getParent()) {
            transfer = new StoryTO(iterator);
            
            if (old != null) {
                transfer.setChildren(new ArrayList<Story>(Arrays.asList(old)));
            }
            else {
                transfer.setChildren(new ArrayList<Story>());
            }
            
            old = transfer;
            returned = transfer;
        }
        return returned;
    }
    
    private long storyPointsAsLong(Story story) {
        if(story.getStoryPoints() == null) {
            return 0L;
        }
        return story.getStoryPoints();
    }
    
    private long storyHoursAsLong(Story story) {
        double minutes = 0;
        for (StoryHourEntry entry: story.getHourEntries()) {
        	minutes += entry.getMinutesSpent();
        }
        for (Task task: story.getTasks()) {
        	for (TaskHourEntry entry: task.getHourEntries()) {
        		minutes += entry.getMinutesSpent();
        	}
        }
    	return Math.round(minutes / 60.0);
    }
    
    private long storyEffortLeftAsLong(Story story) {
        double effortLeft = 0;
        for (Task task: story.getTasks()) {
        	if (task.getEffortLeft() != null && task.getEffortLeft().getMinorUnits() != null) {
                effortLeft += task.getEffortLeft().doubleValue();
        	}
        }
    	return Math.round(effortLeft / 60.0);
    }
    
    public StoryTreeBranchMetrics calculateStoryTreeMetrics(Story story) {
        StoryTreeBranchMetrics metrics = new StoryTreeBranchMetrics();
        
        StoryState deferred = StoryState.DEFERRED;
        
        if(story.getChildren().isEmpty()) {
            if(story.getState() != deferred) {
                metrics.leafPoints = storyPointsAsLong(story);
                if(story.getState() == StoryState.DONE) {
                    metrics.doneLeafPoints = storyPointsAsLong(story);
                }
            }
        }
        
        metrics.spentEffort = storyHoursAsLong(story);
        metrics.effortLeft = storyEffortLeftAsLong(story);
        
        for(Story child : story.getChildren()) {
            if (child.getId() == story.getId()) {
                continue;
            }
            StoryTreeBranchMetrics childMetrics = this.calculateStoryTreeMetrics(child);
            if(child.getState() != deferred) {
                metrics.estimatedDonePoints += childMetrics.estimatedDonePoints;
                metrics.estimatedPoints += childMetrics.estimatedPoints;
                metrics.leafPoints += childMetrics.leafPoints;
                metrics.doneLeafPoints += childMetrics.doneLeafPoints;
            }
            metrics.spentEffort += childMetrics.spentEffort;
            metrics.effortLeft += childMetrics.effortLeft;
        }
        
        if(storyPointsAsLong(story) > metrics.estimatedPoints) {
            if(story.getState() != deferred) {
                metrics.estimatedPoints = storyPointsAsLong(story);
                if(story.getState() == StoryState.DONE) {
                    metrics.estimatedDonePoints = storyPointsAsLong(story);
                }
            }
        }
        
        return metrics;
    }
}
