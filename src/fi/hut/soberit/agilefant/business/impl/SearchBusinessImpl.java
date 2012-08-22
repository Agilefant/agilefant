package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.hut.soberit.agilefant.business.SearchBusiness;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.db.UserDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.NamedObject;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.SearchResultRow;

@Service("searchBusiness")
public class SearchBusinessImpl implements SearchBusiness {

    @Autowired
    private StoryDAO storyDAO;
    @Autowired
    private BacklogDAO backlogDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TaskDAO taskDAO;
    
    public List<SearchResultRow> searchStoriesAndBacklog(String searchTerm) {
        List<SearchResultRow> result = new ArrayList<SearchResultRow>();
        NamedObject quickRefMatch = this.searchByReference(searchTerm);
        if (quickRefMatch != null) {
            result.add(new SearchResultRow(quickRefMatch.getName(),
                    quickRefMatch));
        }
        List<Backlog> backlogs = backlogDAO.searchByName(searchTerm);
        backlogListSearchResult(result, backlogs);
        List<Story> stories = storyDAO.searchByName(searchTerm);
        try {
            stories.addAll(storyDAO.searchByID(searchTerm));
            storyListSearchResult(result, stories);
        }
        catch (Exception e) {
            // The search term is not an integer
            storyListSearchResult(result, stories);
        }
        List<Task> tasks = taskDAO.searchByName(searchTerm);
        taskListSearchResult(result, tasks);
        return result;
    }

    private void storyListSearchResult(List<SearchResultRow> result,
            List<Story> stories) {
        for (Story story : stories) {
            Backlog backlog = story.getIteration();
            if(backlog == null) {
                backlog = story.getBacklog();
            }
            if(checkAccess(backlog)){
                result.add(new SearchResultRow(backlog.getName() + " > "
                    + story.getName(), story));
            }
        }
    }
    
    private void taskListSearchResult(List<SearchResultRow> result,
            List<Task> tasks) {
        for(Task task : tasks) {
            if(task.getStory()!= null){
                if(checkAccess(task.getStory().getBacklog())){      
                    result.add(new SearchResultRow(task.getStory().getName() + " > " + 
                        task.getName(), task));
                }
            }
            if(task.getIteration() != null){
                if(checkAccess(task.getIteration())){  
                    result.add(new SearchResultRow(task.getIteration().getName() + " > " + 
                        task.getName(), task));
                }
            }
        }
    }

    private void backlogListSearchResult(List<SearchResultRow> result,
            List<Backlog> backlogs) {
        for (Backlog bl : backlogs) {
            if(checkAccess(bl)){            
                SearchResultRow item = new SearchResultRow();
                item.setOriginalObject(bl);
                if (bl.getParent() != null) {
                    item.setLabel(bl.getParent().getName() + " > " + bl.getName());
                } else {
                    item.setLabel(bl.getName());
                }
                result.add(item);
            }
        }
    }
    
    private boolean checkAccess(Backlog bl){
        User user = SecurityUtil.getLoggedUser();
        Product prod = null;
        
        if(bl instanceof Project){
            //look at product
            prod = (Product)bl.getParent();
        } else if(bl instanceof Iteration){
            //look at project, then product
            Backlog temp = bl.getParent();
            if(temp == null){
                //standalone iteration
                temp = bl;
                Set<Iteration> allowedIterations = new HashSet<Iteration>();
                for(Team team : user.getTeams()){
                    allowedIterations.addAll(team.getIterations());
                }

                //check if we have access 
                if(allowedIterations.contains(temp)){
                    return true;
                }                
                return false;
                
            } else {
                if(temp instanceof Product){
                    //iteration is directly under a product, not in a project
                    prod = (Product) temp;
                } else {
                    prod = (Product) temp.getParent();
                }
            }
        } else if(bl instanceof Product){
            prod = (Product)bl;
        }
        
        Set<Product> allowedProducts = new HashSet<Product>();
        for(Team team : user.getTeams()){
            allowedProducts.addAll(team.getProducts());
        }

        //check if we have access 
        if(allowedProducts.contains(prod)){
            return true;
        }
        
        return false;
    }

    public NamedObject searchByReference(String searchTerm) {
        if (searchTerm == null) {
            return null;
        }

        String[] matches = searchTerm.split(":");
        int objectId;
        String type;
        if (matches.length != 2) {
            return null;
        }
        type = matches[0];

        try {
            objectId = Integer.parseInt(matches[1]);
        } catch (Exception e) {
            return null;
        }
        if (type.equals("story")) {
            Story story = storyDAO.get(objectId);
            if(story != null && checkAccess(story.getBacklog()) && story.getBacklog() != null){  
                return story;
            } else if (story != null && story.getBacklog() == null){
                return story.getIteration();
            }
        } else if (type.equals("backlog")) {
            Backlog bl = backlogDAO.get(objectId);
            if(checkAccess(bl)){  
                return bl;
            }
        }
        return null;
    }
    
    public List<SearchResultRow> searchIterations(String searchTerm) {
        List<SearchResultRow> result = new ArrayList<SearchResultRow>();
        List<Backlog> backlogs = backlogDAO.searchByName(searchTerm, Iteration.class);
        backlogListSearchResult(result, backlogs);
        return result;
    }

    public List<SearchResultRow> searchProjects(String searchTerm) {
        List<SearchResultRow> result = new ArrayList<SearchResultRow>();
        List<Backlog> backlogs = backlogDAO.searchByName(searchTerm, Project.class);
        backlogListSearchResult(result, backlogs);
        return result;
    }

    public List<SearchResultRow> searchStories(String searchTerm) {
        List<SearchResultRow> result = new ArrayList<SearchResultRow>();
        List<Story> stories = storyDAO.searchByName(searchTerm);
        storyListSearchResult(result, stories);
        return result;
    }

    public List<SearchResultRow> searchUsers(String searchTerm) {
        List<SearchResultRow> result = new ArrayList<SearchResultRow>();
        List<User> users = userDAO.searchByName(searchTerm);
        for(User user : users) {
            result.add(new SearchResultRow(user.getFullName(), user));
        }
        return result;
    }
    
    public List<SearchResultRow> searchTasks(String searchTerm) {
        List<SearchResultRow> result = new ArrayList<SearchResultRow>();
        List<Task> tasks = taskDAO.searchByName(searchTerm);
        for(Task task : tasks) {
            if(task.getStory() != null){
                if(checkAccess(task.getStory().getBacklog())){  
                    result.add(new SearchResultRow(task.getIteration().getName() + " > " + task.getStory().getName() + " > " + 
                        task.getName(), task));
                }
            } else {
                if(checkAccess(task.getIteration())){  
                    result.add(new SearchResultRow(task.getIteration().getName() + " > No Story > " + 
                        task.getName(), task));
                }
            }
        }
        return result;
    }
}
