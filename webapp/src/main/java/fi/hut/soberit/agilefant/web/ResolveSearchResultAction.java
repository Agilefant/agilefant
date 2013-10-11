package fi.hut.soberit.agilefant.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;


@Component("resolveSearchResultAction")
@Scope("prototype")
public class ResolveSearchResultAction extends ActionSupport {
    private static final long serialVersionUID = -3551952340761514545L;
    
    @Autowired
    private StoryBusiness storyBusiness;
    @Autowired
    private TaskBusiness taskBusiness;
    
    private String targetClassName = "";
    private int targetObjectId = 0;
    private int targetBacklogId;

    @Override
    public String execute() {
        if(Story.class.getCanonicalName().equals(this.targetClassName)) {
            return resolveStoryContainer();
        } else if(Iteration.class.getCanonicalName().equals(this.targetClassName)) {
            this.targetBacklogId = this.targetObjectId;
            return "iteration";
        } else if(Project.class.getCanonicalName().equals(this.targetClassName)) {
            this.targetBacklogId = this.targetObjectId;
            return "project";
        } else if(Product.class.getCanonicalName().equals(this.targetClassName)) {
            this.targetBacklogId = this.targetObjectId;
            return "product";
        } else if(Task.class.getCanonicalName().equals(this.targetClassName)) {
            return resolveTaskContainer();
        } else {
            return ERROR;
        }
    }

    private String resolveStoryContainer() {
        Story story = this.storyBusiness.retrieve(targetObjectId);
        Iteration i = story.getIteration();
        if(i != null) {
            this.targetBacklogId = i.getId();
            return "iteration";
        } else if(story.getBacklog() instanceof Project) {
            this.targetBacklogId = story.getBacklog().getId();
            return "project";
        } else {
            this.targetBacklogId = story.getBacklog().getId();
            return "product";
        }
    }
    
    private String resolveTaskContainer() {
        Task task = this.taskBusiness.retrieve(targetObjectId);
        if(task.getStory() != null && task.getStory().getIteration() != null) {
            this.targetBacklogId = task.getStory().getIteration().getId();
            return "iteration";
        }
        else if(task.getStory() != null && task.getStory().getBacklog() != null) {
            this.targetBacklogId = task.getStory().getBacklog().getId();
            if(task.getStory().getBacklog() instanceof Iteration) {
                return "iteration";
            } else if(task.getStory().getBacklog() instanceof Project) {
                return "project";
            } else {
                return "product";
            }
        }
        else {
            this.targetBacklogId = task.getIteration().getId();
            return "iteration";
        }
    }

    public void setTargetObjectId(int targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public int getTargetBacklogId() {
        return targetBacklogId;
    }
    
    public String getHash() {
        return targetClassName + "_" + targetObjectId;
    }

}
