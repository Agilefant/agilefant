package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.MenuBusiness;
import fi.hut.soberit.agilefant.business.ProductBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.db.IterationDAO;
import fi.hut.soberit.agilefant.db.ProjectDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.transfer.BacklogType;
import fi.hut.soberit.agilefant.transfer.MenuDataNode;
import fi.hut.soberit.agilefant.util.MyAssignmentsMenuBuilder;

/**
 * The implementation class for calculating data to the lefthand menu.
 * 
 * @author rjokelai
 */
@Service("menuBusiness")
@Transactional
public class MenuBusinessImpl implements MenuBusiness {

    @Autowired
    private IterationDAO iterationDAO;
    
    @Autowired
    private ProjectDAO projectDAO;
    
    @Autowired
    private StoryDAO storyDAO;
    
    @Autowired
    private ProductBusiness productBusiness;
    
    @Autowired
    private IterationBusiness iterationBusiness;

    @Autowired
    private TransferObjectBusiness transferObjectBusiness;
    
    @Autowired
    private UserBusiness userBusiness;

    @SuppressWarnings("unchecked")
    public List<MenuDataNode> constructBacklogMenuData(User user) {
    	user = this.userBusiness.retrieve(user.getId());
    	
        List<MenuDataNode> nodes = new ArrayList<MenuDataNode>();
        List<Product> products = new ArrayList<Product>(productBusiness
                .retrieveAllOrderByName());
        Collections.sort(products, new PropertyComparator("name", true, true));
        
        Set<Product> allowedProducts = new HashSet<Product>();
        for(Team team : user.getTeams()){
            allowedProducts.addAll(team.getProducts());
        }
        
        for (Product prod : products) {
            //check if we have access
            if(allowedProducts.contains(prod)){
                nodes.add(constructMenuDataNode(prod));
            }
        }
        
        final List<Iteration> standAloneIterations = new ArrayList<Iteration>(iterationBusiness.retrieveAllStandAloneIterations());
        
        Set<Iteration> allowedIterations = new HashSet<Iteration>();
        for(Team team : user.getTeams()){
            allowedIterations.addAll(team.getIterations());
        }
        
        for (Iteration iteration: standAloneIterations) {
            if(allowedIterations.contains(iteration)){
                nodes.add(constructMenuDataNode(iteration));
            }
        }
        
        return nodes;
    }

    @SuppressWarnings("unchecked")
    private MenuDataNode constructMenuDataNode(Backlog backlog) {
        MenuDataNode mdn = new MenuDataNode();
        mdn.setTitle(backlog.getName());
        mdn.setId(backlog.getId());
        mdn.setScheduleStatus(transferObjectBusiness
                .getBacklogScheduleStatus(backlog));
        List<Backlog> children = Collections.EMPTY_LIST;
        if (!(backlog instanceof Iteration)) { // optimization
            children = new ArrayList<Backlog>();
            children.addAll(backlog.getChildren());
            Collections.sort(children, new PropertyComparator("startDate",
                    true, true));
        }
        
        final BacklogType backlogClassType = BacklogType.forBacklog(backlog);
        if (backlogClassType != null) {
            mdn.setType(backlogClassType);
        }
        
        for (Backlog child : children) {
            mdn.getChildren().add(constructMenuDataNode(child));
        }

        return mdn;
    }
    
    @Transactional(readOnly = true)
    public List<MenuDataNode> constructMyAssignmentsData(User user) {
        List<Project> projects = projectDAO.retrieveActiveWithUserAssigned(user.getId());
        List<Iteration> iterations = iterationDAO.retrieveActiveWithUserAssigned(user.getId());
        List<Story> stories = storyDAO.retrieveActiveIterationStoriesWithUserResponsible(user.getId());
        MyAssignmentsMenuBuilder builder = new MyAssignmentsMenuBuilder();
        
        for (Project project : projects) {
            builder.insert(project);
        }
        for (Iteration iteration : iterations) {
            builder.insert(iteration);
        }
        for (Story story : stories) {
            builder.insert(story);
        }
        
        return builder.getNodes();
    }

    public void setProductBusiness(ProductBusiness productBusiness) {
        this.productBusiness = productBusiness;
    }
    
    public void setIterationBusiness(IterationBusiness iterationBusiness) {
        this.iterationBusiness = iterationBusiness;
    }

    public void setTransferObjectBusiness(
            TransferObjectBusiness transferObjectBusiness) {
        this.transferObjectBusiness = transferObjectBusiness;
    }
    
    public void setIterationDAO(IterationDAO iterationDAO) {
        this.iterationDAO = iterationDAO;
    }
    
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
    
    public void setStoryDAO(StoryDAO storyDAO) {
        this.storyDAO = storyDAO;
    }
    
    public void setUserBusiness(UserBusiness userBusiness) {
    	this.userBusiness = userBusiness;
    }
    
}
