package fi.hut.soberit.agilefant.business.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.ExportImportBusiness;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.business.TeamBusiness;
import fi.hut.soberit.agilefant.db.AgilefantWidgetDAO;
import fi.hut.soberit.agilefant.db.AssignmentDAO;
import fi.hut.soberit.agilefant.db.BacklogHistoryEntryDAO;
import fi.hut.soberit.agilefant.db.BacklogHourEntryDAO;
import fi.hut.soberit.agilefant.db.HolidayDAO;
import fi.hut.soberit.agilefant.db.HourEntryDAO;
import fi.hut.soberit.agilefant.db.IterationDAO;
import fi.hut.soberit.agilefant.db.IterationHistoryEntryDAO;
import fi.hut.soberit.agilefant.db.LabelDAO;
import fi.hut.soberit.agilefant.db.ProductDAO;
import fi.hut.soberit.agilefant.db.ProjectDAO;
import fi.hut.soberit.agilefant.db.SettingDAO;
import fi.hut.soberit.agilefant.db.StoryAccessDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.StoryHourEntryDAO;
import fi.hut.soberit.agilefant.db.StoryRankDAO;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.db.TaskHourEntryDAO;
import fi.hut.soberit.agilefant.db.TeamDAO;
import fi.hut.soberit.agilefant.db.UserDAO;
import fi.hut.soberit.agilefant.db.WhatsNextEntryDAO;
import fi.hut.soberit.agilefant.db.WhatsNextStoryEntryDAO;
import fi.hut.soberit.agilefant.db.WidgetCollectionDAO;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.AgilefantWidget;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Setting;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.util.TokenGenerator;

/**
 * Implementation class for export / import service
 * 
 * @author jkorri
 */
@Service("exportBusiness")
public class ExportImportBusinessImpl implements ExportImportBusiness {

	private static final Logger LOG = LoggerFactory.getLogger(ExportImportBusinessImpl.class);	
	
	@Autowired AssignmentDAO assignmentDAO;
	@Autowired BacklogHistoryEntryDAO backlogHistoryEntryDAO;
	@Autowired BacklogHourEntryDAO backlogHourEntryDAO;
	@Autowired HolidayDAO holidayDAO;
	@Autowired HourEntryDAO hourEntryDAO;
	@Autowired IterationDAO iterationDAO;
	@Autowired IterationHistoryEntryDAO iterationHistoryEntryDAO;
	@Autowired LabelDAO labelDAO;
	@Autowired ProductDAO productDAO;
	@Autowired ProjectDAO projectDAO;
	@Autowired SettingDAO settingDAO;
	@Autowired StoryDAO storyDAO;
	@Autowired StoryAccessDAO storyAccessDAO;
	@Autowired StoryHourEntryDAO storyHourEntryDAO;
	@Autowired StoryRankDAO storyRankDAO;
	@Autowired TaskDAO taskDAO;
	@Autowired TaskHourEntryDAO taskHourEntryDAO;
	@Autowired TeamDAO teamDAO;
	@Autowired UserDAO userDAO;
	@Autowired WhatsNextEntryDAO whatsNextEntryDAO;
	@Autowired WhatsNextStoryEntryDAO whatsNextStoryEntryDAO;
	@Autowired WidgetCollectionDAO widgetCollectionDAO;
	@Autowired AgilefantWidgetDAO agilefantWidgetDAO;
	
	@Autowired
	private IterationBusiness iterationBusiness;
	
	@Autowired
	private UserBusiness userBusiness;
	
	@Autowired
	private SettingBusiness settingBusiness;
	
	@Autowired
	private TeamBusiness teamBusiness;
	
	@Autowired
	SessionFactory sessionFactory;
	
    @Autowired
    private TokenGenerator tokenGenerator;
	
	private void addInOrder(Story story, Collection<Story> stories) {
		Story parent = story.getParent();
		if(parent!=null && !stories.contains(parent) && story!=parent) {
			this.addInOrder(parent, stories);
		}
		stories.add(story);
	}
	
	public AgilefantWidgetAndRef addWidgetTypeInfo(AgilefantWidget widget) {
		if(widget.getType().startsWith("story")) {
			Story story = this.storyDAO.get(widget.getObjectId());
			return new AgilefantWidgetAndRef(widget, story);
		}
		if(widget.getType().startsWith("iteration")) {
			Iteration iteration = this.iterationDAO.get(widget.getObjectId());
			return new AgilefantWidgetAndRef(widget, iteration);			
		}
		if(widget.getType().startsWith("project")) {
			Project project = this.projectDAO.get(widget.getObjectId());
			return new AgilefantWidgetAndRef(widget, project);			
		}
		if(widget.getType().startsWith("user")) {
			User user = this.userDAO.get(widget.getObjectId());
			return new AgilefantWidgetAndRef(widget, user);			
		}
		throw new RuntimeException("Unknown widget type " + widget.getType());
	}
		
	@Override
	@Transactional(readOnly=true)
	public OrganizationDumpTO exportOrganization() {

		OrganizationDumpTO organizationTO = new OrganizationDumpTO();
		
		organizationTO.assignments.addAll(this.assignmentDAO.getAll());
		organizationTO.backlogHistoryEntries.addAll(this.backlogHistoryEntryDAO.getAll());
		organizationTO.backlogHourEntries.addAll(this.backlogHourEntryDAO.getAll());
		organizationTO.holidays.addAll(this.holidayDAO.getAll());
		organizationTO.iterations.addAll(this.iterationDAO.getAll());
		organizationTO.iterationHistoryEntries.addAll(this.iterationHistoryEntryDAO.getAll());
		organizationTO.labels.addAll(this.labelDAO.getAll());
		organizationTO.products.addAll(this.productDAO.getAll());
		organizationTO.projects.addAll(this.projectDAO.getAll());
		organizationTO.settings.addAll(this.settingDAO.getAll());
		
		for(Story story : this.storyDAO.getAll()) {
			this.addInOrder(story, organizationTO.stories);
		}
		
		organizationTO.storyAccesses.addAll(this.storyAccessDAO.getAll());
		organizationTO.storyHourEntries.addAll(this.storyHourEntryDAO.getAll());
		organizationTO.storyRanks.addAll(this.storyRankDAO.getAll());
		organizationTO.tasks.addAll(this.taskDAO.getAll());
		organizationTO.taskHourEntries.addAll(this.taskHourEntryDAO.getAll());
		organizationTO.teams.addAll(this.teamDAO.getAll());
		organizationTO.users.addAll(this.userDAO.getAll());
		organizationTO.whatsNextEntries.addAll(this.whatsNextEntryDAO.getAll());
		organizationTO.whatsNextStoryEntries.addAll(this.whatsNextStoryEntryDAO.getAll());
		organizationTO.widgetCollections.addAll(this.widgetCollectionDAO.getAll());

		for(AgilefantWidget widget : this.agilefantWidgetDAO.getAll()) {
			organizationTO.widgets.add(this.addWidgetTypeInfo(widget));			
		}
		
		return organizationTO;
	}
	
	private String generateDuplicateIdentifier() {
		SecureRandom r = new SecureRandom();
		return "_DUPLICATE" + new BigInteger(50, r).toString();
	}
	
	void renameDuplicateData(OrganizationDumpTO organizationTO) {
		for(User user : organizationTO.users) {
			while (this.userBusiness.retrieveByLoginName(user.getLoginName())!=null) {
				user.setLoginName(user.getLoginName() + generateDuplicateIdentifier());				
			}
		}
		for(Team team : organizationTO.teams) {
			while (this.teamBusiness.getByTeamName(team.getName())!=null) {
				team.setName(team.getName() + generateDuplicateIdentifier());
			}
		}
		for(Iteration iteration : organizationTO.iterations) {
			if (iteration.getReadonlyToken() != null) {
				iteration.setReadonlyToken(tokenGenerator.generateReadonlyToken(iteration.getReadonlyToken()));
			}
		}
	}
	
	@Override
	@Transactional(rollbackFor=RuntimeException.class)
	public void importOrganization(OrganizationDumpTO organizationTO) {
		this.renameDuplicateData(organizationTO);
		
		Collection<Object> objects = new ArrayList<Object>();
		objects.addAll(organizationTO.users);
		objects.addAll(organizationTO.holidays);
		objects.addAll(organizationTO.products);
		objects.addAll(organizationTO.projects);
		objects.addAll(organizationTO.iterations);
		objects.addAll(organizationTO.stories);
		objects.addAll(organizationTO.tasks);
		objects.addAll(organizationTO.assignments);
		objects.addAll(organizationTO.backlogHourEntries);
		objects.addAll(organizationTO.storyHourEntries);
		objects.addAll(organizationTO.taskHourEntries);
		objects.addAll(organizationTO.backlogHistoryEntries);
		objects.addAll(organizationTO.iterationHistoryEntries);
		objects.addAll(organizationTO.labels);
		objects.addAll(organizationTO.storyAccesses);
		objects.addAll(organizationTO.storyRanks);
		objects.addAll(organizationTO.teams);
		objects.addAll(organizationTO.whatsNextEntries);
		objects.addAll(organizationTO.whatsNextStoryEntries);
		objects.addAll(organizationTO.widgetCollections);
		objects.addAll(organizationTO.widgets);
		if (settingDAO.count() == 0) {
			objects.addAll(organizationTO.settings);
		} else {
			for (Setting setting: organizationTO.settings) {
				settingBusiness.setValue(setting.getName(), setting.getValue());
			}
		}

		Session session = this.sessionFactory.getCurrentSession();
		try {
			int index = 0;
			for(Object object : objects) {
				if(object instanceof AgilefantWidgetAndRef) {
					// Deref the widget object here, will set proper object id as the referenced object is persisted by now
					object = ((AgilefantWidgetAndRef)object).getAgilefantWidget();
				}
				session.save(object);					
				if(index++ % 50 == 0) {
					session.flush();
					session.clear();
				}
			}
			
		} catch(Exception e) {
			LOG.error("Error importing data");
			throw new RuntimeException(e);
		}
	}
}
