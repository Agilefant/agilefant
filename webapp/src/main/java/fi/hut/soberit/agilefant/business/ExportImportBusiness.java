package fi.hut.soberit.agilefant.business;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.hut.soberit.agilefant.model.AgilefantWidget;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.BacklogHistoryEntry;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.Holiday;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.IterationHistoryEntry;
import fi.hut.soberit.agilefant.model.Label;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Setting;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryAccess;
import fi.hut.soberit.agilefant.model.StoryHourEntry;
import fi.hut.soberit.agilefant.model.StoryRank;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskHourEntry;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;
import fi.hut.soberit.agilefant.model.WhatsNextStoryEntry;
import fi.hut.soberit.agilefant.model.WidgetCollection;

/**
 * Interface for export/import services
 * 
 * @author jkorri
 */
public interface ExportImportBusiness {

	/**
	 * Exports organization's data
	 * 
	 * @return
	 */
	public OrganizationDumpTO exportOrganization();

	/**
	 * Imports data to an organization. Should only be done to a fresh organization, but will run and add incremental changes as well
	 * 
	 * @param organizationTO
	 */
	public void importOrganization(OrganizationDumpTO organizationTO);

	/**
	 * Transfer object for stuff that is exported from an organization
	 * 
	 * @author jkorri
	 */
	@JsonPropertyOrder({ "version", "users", "holidays", "products", "projects", "iterations", "stories", "tasks", "assignments", "backlogHourEntries",
		"storyHourEntries", "taskHourEntries", "backlogHistoryEntries", "iterationHistoryEntries", "labels", "storyAccesses", "storyRanks",
		"teams", "whatsNextEntries", "whatsNextStoryEntries", "widgetCollections", "widgets", "settings"})
	public static class OrganizationDumpTO {

		public Collection<User> users = new LinkedHashSet<User>();
		public Collection<Holiday> holidays = new LinkedHashSet<Holiday>();
		public Collection<Product> products = new LinkedHashSet<Product>();
		public Collection<Project> projects = new LinkedHashSet<Project>();
		public Collection<Iteration> iterations = new LinkedHashSet<Iteration>();
		public Collection<Story> stories = new LinkedHashSet<Story>();
		public Collection<Task> tasks = new LinkedHashSet<Task>();
		public Collection<Assignment> assignments = new LinkedHashSet<Assignment>();
		public Collection<BacklogHourEntry> backlogHourEntries = new LinkedHashSet<BacklogHourEntry>();
		public Collection<StoryHourEntry> storyHourEntries = new LinkedHashSet<StoryHourEntry>();
		public Collection<TaskHourEntry> taskHourEntries = new LinkedHashSet<TaskHourEntry>();		
		public Collection<BacklogHistoryEntry> backlogHistoryEntries = new LinkedHashSet<BacklogHistoryEntry>();
		public Collection<IterationHistoryEntry> iterationHistoryEntries = new LinkedHashSet<IterationHistoryEntry>();
		public Collection<Label> labels = new LinkedHashSet<Label>();
		public Collection<StoryAccess> storyAccesses = new LinkedHashSet<StoryAccess>();
		public Collection<StoryRank> storyRanks = new LinkedHashSet<StoryRank>();
		public Collection<Team> teams = new LinkedHashSet<Team>();
		public Collection<WhatsNextEntry> whatsNextEntries = new LinkedHashSet<WhatsNextEntry>();
		public Collection<WhatsNextStoryEntry> whatsNextStoryEntries = new LinkedHashSet<WhatsNextStoryEntry>();
		public Collection<WidgetCollection> widgetCollections = new LinkedHashSet<WidgetCollection>();		
		public Collection<AgilefantWidgetAndRef> widgets = new LinkedHashSet<AgilefantWidgetAndRef>();		
		public Collection<Setting> settings = new LinkedHashSet<Setting>();
	}
	
	/**
	 * Transfer object that attaches a reference to a specific story, user, project, iteration etc. in a widget.
	 * This is needed because widget's objectId field is a reference to different types of model objects depending
	 * on widget type
	 * 
	 * @author jkorri
	 */
 	public static class AgilefantWidgetAndRef {
		
		private AgilefantWidget agilefantWidget;
		private Story story;
		private User user;
		private Project project;
		private Iteration iteration;

		public AgilefantWidgetAndRef() {
			
		}
		
		public AgilefantWidgetAndRef(AgilefantWidget agilefantWidget, Story story) {
			this.agilefantWidget = agilefantWidget;
			this.story = story;
		}

		public AgilefantWidgetAndRef(AgilefantWidget agilefantWidget, User user) {
			this.agilefantWidget = agilefantWidget;
			this.user = user;
		}

		public AgilefantWidgetAndRef(AgilefantWidget agilefantWidget, Project project) {
			this.agilefantWidget = agilefantWidget;
			this.project = project;
		}
		
		public AgilefantWidgetAndRef(AgilefantWidget agilefantWidget, Iteration iteration) {
			this.agilefantWidget = agilefantWidget;
			this.iteration = iteration;
		}
		
		public AgilefantWidget getAgilefantWidget() {
			try {
				int objectId;
				if(this.story!=null) {
					objectId = this.story.getId();
				} else if(this.user!=null) {
					objectId = this.user.getId();
				} else if(this.project!=null) {
					objectId = this.project.getId();
				} else if(this.iteration!=null){
					objectId = this.iteration.getId();
				} else {
					throw new RuntimeException("Missing object id!");
				}
				this.agilefantWidget.setObjectId(objectId);
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
			return this.agilefantWidget;
		}
	}	
}
