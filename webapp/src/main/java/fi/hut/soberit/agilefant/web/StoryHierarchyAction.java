package fi.hut.soberit.agilefant.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.StoryHierarchyBusiness;
import fi.hut.soberit.agilefant.exception.StoryTreeIntegrityViolationException;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.transfer.StoryTreeBranchMetrics;
import fi.hut.soberit.agilefant.util.StoryFilters;
import fi.hut.soberit.agilefant.util.StoryTreeIntegrityMessage;
import fi.hut.soberit.agilefant.util.StoryTreeIntegrityUtils;

@Component("storyHierarchyAction")
@Scope("prototype")
public class StoryHierarchyAction extends ActionSupport {
    private static final long serialVersionUID = 2599224272717369900L;

    @Autowired
    private StoryBusiness storyBusiness;
    
    @Autowired
    private StoryHierarchyBusiness storyHierarchyBusiness;
        
    private List<Story> stories;
    private StoryFilters storyFilters = new StoryFilters();
    private Integer storyId;
    private Integer[] storyIds;
    private Integer projectId;
    private Integer productId;
    private Story story;
    private Story topmostStory;
    private Integer referenceStoryId;

    private List<Story> hierarchy = new ArrayList<Story>();
    
    private List<String> integrityErrors = new ArrayList<String>(); 
    
    private StoryTreeBranchMetrics branchMetrics;

    public String recurseHierarchyAsList() {
        story = storyBusiness.retrieve(storyId);
        topmostStory = storyHierarchyBusiness.recurseHierarchy(story);
        return Action.SUCCESS;
    }
    
    public String moveMultipleBefore() {
        Story reference = this.storyBusiness.retrieve(referenceStoryId);
        try {
            for (int i : storyIds)
            {
                Story s = this.storyBusiness.retrieve(i);
                this.storyHierarchyBusiness.moveBefore(s, reference);
            }
        } catch (StoryTreeIntegrityViolationException stive) {
            parseIntegrityErrors(stive);
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }
    
    public String moveMultipleUnder() {
        Story reference = this.storyBusiness.retrieve(referenceStoryId);
        try {
            for (int i : storyIds)
            {
                Story s = this.storyBusiness.retrieve(i);
                this.storyHierarchyBusiness.moveUnder(s, reference);
            }
        } catch (StoryTreeIntegrityViolationException stive) {
            parseIntegrityErrors(stive);
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }
    
    public String moveMultipleAfter() {
        Story reference = this.storyBusiness.retrieve(referenceStoryId);
        try {
            for (int i : storyIds)
            {
                Story s = this.storyBusiness.retrieve(i);
                this.storyHierarchyBusiness.moveAfter(s, reference);
            }
        } catch (StoryTreeIntegrityViolationException stive) {
            parseIntegrityErrors(stive);
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }
    
    public String moveStoryUnder() {
        Story target = this.storyBusiness.retrieve(storyId);
        Story reference = this.storyBusiness.retrieve(referenceStoryId);
        try {
            this.storyHierarchyBusiness.moveUnder(target, reference);
        } catch (StoryTreeIntegrityViolationException stive) {
            parseIntegrityErrors(stive);
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }

    public String moveStoryAfter() {
        Story target = this.storyBusiness.retrieve(storyId);
        Story reference = this.storyBusiness.retrieve(referenceStoryId);
        try {
            this.storyHierarchyBusiness.moveAfter(target, reference);
        } catch (StoryTreeIntegrityViolationException stive) {
            parseIntegrityErrors(stive);
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }
    
    public String moveStoryBefore() {
        Story target = this.storyBusiness.retrieve(storyId);
        Story reference = this.storyBusiness.retrieve(referenceStoryId);
        try {
            this.storyHierarchyBusiness.moveBefore(target, reference);
        } catch (StoryTreeIntegrityViolationException stive) {
            parseIntegrityErrors(stive);
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }
    
    private void parseIntegrityErrors(StoryTreeIntegrityViolationException stive) {
        List<StoryTreeIntegrityMessage> messages = stive.getMessages();
        // Check for fatal messages
        StoryTreeIntegrityUtils.getFatalMessages(messages);
        
        // Build the string
        for (StoryTreeIntegrityMessage stim : messages) {
            String message = this.getText(stim.getMessageName());
            
            if (stim.getTarget() != null) {
                message += ": " + stim.getTarget().getName() + " in "
                        + stim.getTarget().getBacklog().getName();
            }
            integrityErrors.add(message);
        }
    }
    
    public String retrieveBranchMetrics() {
        story = this.storyBusiness.retrieve(this.storyId);
        this.branchMetrics = this.storyHierarchyBusiness.calculateStoryTreeMetrics(story);
        return Action.SUCCESS;
    }
        
    public String retrieveProductRootStories() {
        stories = storyHierarchyBusiness.retrieveProductRootStories(productId, storyFilters);
        for (Story story: stories) {
            Story fullInfoStory = this.storyBusiness.retrieve(story.getId());
            story.setFullInfoStory(fullInfoStory);
            HashMap<Integer, Story> fullInfoStoryMap = new HashMap<Integer, Story>();
            initHashMap(fullInfoStory.getChildren(), fullInfoStoryMap);
            setStoriesInformation(story.getChildren(), fullInfoStoryMap);
        }
        return Action.SUCCESS;
    }
    public String retrieveProjectRootStories() {
        stories = storyHierarchyBusiness.retrieveProjectRootStories(projectId, storyFilters);
        for (Story story: stories) {
            Story fullInfoStory = this.storyBusiness.retrieve(story.getId());
            story.setFullInfoStory(fullInfoStory);
            HashMap<Integer, Story> fullInfoStoryMap = new HashMap<Integer, Story>();
            initHashMap(fullInfoStory.getChildren(), fullInfoStoryMap);
            setStoriesInformation(story.getChildren(), fullInfoStoryMap);
        }
        return Action.SUCCESS;
    }
    
    private void initHashMap(List<Story> fullInfoStories, HashMap<Integer, Story> fullInfoStoryMap) {
        for (Story fullInfoStory: fullInfoStories) {
            fullInfoStoryMap.put(fullInfoStory.getId(), fullInfoStory);
            initHashMap(fullInfoStory.getChildren(), fullInfoStoryMap);
        }
    }
    
    private void setStoriesInformation(List<Story> stories, HashMap<Integer, Story> fullInfoStoryMap) {
        for (Story story: stories) {
            Story fullInfoStory = fullInfoStoryMap.get(story.getId());
            story.setFullInfoStory(fullInfoStory);
            setStoriesInformation(story.getChildren(), fullInfoStoryMap);
        }
    }

    /*
     * SETTERS AND GETTERS
     */

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public void setStoryIds(Integer[] storyIds) {
        this.storyIds = storyIds;
    }
    
    public List<Story> getHierarchy() {
        return hierarchy;
    }

    public void setStoryBusiness(StoryBusiness storyBusiness) {
        this.storyBusiness = storyBusiness;
    }

    public Story getStory() {
        return story;
    }

    public void setReferenceStoryId(Integer parentId) {
        this.referenceStoryId = parentId;
    }

    public void setStoryHierarchyBusiness(
            StoryHierarchyBusiness storyHierarchyBusiness) {
        this.storyHierarchyBusiness = storyHierarchyBusiness;
    }
    
    public List<Story> getStories() {
        return stories;
    }
    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public StoryFilters getStoryFilters() {
        return storyFilters;
    }

    public void setStoryFilters(StoryFilters storyFilters) {
        this.storyFilters = storyFilters;
    }

    public Story getTopmostStory() {
        return topmostStory;
    }

    public List<String> getIntegrityErrors() {
        return integrityErrors;
    }

    public StoryTreeBranchMetrics getBranchMetrics() {
        return branchMetrics;
    }

}