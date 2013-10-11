package fi.hut.soberit.agilefant.exportimport;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.typesafe.config.Config;

import fi.hut.soberit.agilefant.business.ExportImportBusiness.OrganizationDumpTO;
import fi.hut.soberit.agilefant.model.AgilefantWidget;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogHistoryEntry;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.Holiday;
import fi.hut.soberit.agilefant.model.HolidayAnomaly;
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

@Component
public class ExportImport {

	private static final ObjectMapper objectMapper;

	private static final String VERSION = "agilefant.version";
	
	@Autowired
    private Config config;
	
	static {
		objectMapper = initObjectMapper();
	}
	
	private static ObjectMapper initObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
    	SimpleModule importExportModule = new ImportExportModule();
    	
    	objectMapper.registerModule(importExportModule);
    	objectMapper.registerModule(new JodaModule());

    	objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
    	objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);    	

    	return objectMapper;
	}

	private ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	private String getVersion() {
		return this.config.getString(VERSION);
	}
	
	public void toJson(OutputStream out, OrganizationDumpTO organizationTO) {
		try {
			ExportData exportData = new ExportData();
			exportData.version = this.getVersion();
			ObjectWriter objectWriter = this.getObjectMapper().writer(new DefaultPrettyPrinter());
			objectWriter.writeValue(out, exportData);
			objectWriter.writeValue(out, organizationTO);			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public OrganizationDumpTO fromJson(InputStream in) throws VersionMismatchException {
		try {
			JsonParser parser = this.getObjectMapper().getFactory().createParser(in);
	    	ExportData exportData = this.getObjectMapper().readValue(parser, ExportData.class);			
			if(!this.getVersion().equals(exportData.version)) {
				throw new VersionMismatchException("Current application version is " + this.getVersion() + " while import version is " + exportData.version);
			}
	    	OrganizationDumpTO organizationTO = this.getObjectMapper().readValue(parser, OrganizationDumpTO.class);
	    	return organizationTO;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("serial")
	public static class ImportExportModule extends SimpleModule {
		public ImportExportModule() {
			super("ImportExport", new Version(0,0,1,null,null,null));			
		}
		
		@Override
		public void setupModule(SetupContext context) {
			context.setMixInAnnotations(Backlog.class, ExportableBacklog.class);
			context.setMixInAnnotations(User.class, ExportableUser.class);
			context.setMixInAnnotations(Holiday.class, ExportableModel.class);
			context.setMixInAnnotations(Product.class, ExportableProduct.class);
			context.setMixInAnnotations(Project.class, ExportableProject.class);
			context.setMixInAnnotations(Iteration.class, ExportableIteration.class);
			context.setMixInAnnotations(Story.class, ExportableStory.class);
			context.setMixInAnnotations(Task.class, ExportableTask.class);
			context.setMixInAnnotations(Assignment.class, ExportableModel.class);
			context.setMixInAnnotations(BacklogHourEntry.class, ExportableModel.class);
			context.setMixInAnnotations(StoryHourEntry.class, ExportableModel.class);
			context.setMixInAnnotations(TaskHourEntry.class, ExportableModel.class);
			context.setMixInAnnotations(BacklogHistoryEntry.class, ExportableModel.class);
			context.setMixInAnnotations(IterationHistoryEntry.class, ExportableModel.class);
			context.setMixInAnnotations(Label.class, ExportableModel.class);
			context.setMixInAnnotations(StoryAccess.class, ExportableModel.class);
			context.setMixInAnnotations(StoryRank.class, ExportableModel.class);
			context.setMixInAnnotations(Team.class, ExportableModel.class);
			context.setMixInAnnotations(WhatsNextEntry.class, ExportableModel.class);
			context.setMixInAnnotations(WhatsNextStoryEntry.class, ExportableModel.class);
			context.setMixInAnnotations(WidgetCollection.class, ExportableWidgetCollection.class);
			context.setMixInAnnotations(AgilefantWidget.class, ExportableAgilefantWidget.class);
			context.setMixInAnnotations(Setting.class, ExportableModel.class);
		
		}
	}

	@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	public interface ExportableModel {
	    @JsonIgnore
	    public int getId();
	}
	
	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
	public interface ExportableBacklog extends ExportableModel {
	}
	
	public static abstract class ExportableUser extends User implements ExportableModel {
		@Override
		@JsonIgnore
		public abstract Collection<Team> getTeams();

		@Override
		@JsonIgnore
		public abstract Collection<Assignment> getAssignments();
		
		@Override
		@JsonIgnore
		public abstract Collection<Story> getStories();

		@Override
		@JsonIgnore
		public abstract Set<Task> getTasks();

		@Override
		@JsonIgnore
		public abstract Collection<Holiday> getHolidays();

		@Override
		@JsonIgnore
		public abstract Collection<HolidayAnomaly> getHolidayAnomalies();
		
	}
	
	public static abstract class ExportableProduct extends Product implements ExportableBacklog {

		@Override
		@JsonIgnore
		public abstract Collection<Team> getTeams();

		@Override
		@JsonIgnore
		public abstract Collection<Project> getProjects();

		@Override
		@JsonIgnore
		public abstract Collection<Iteration> getIterations();

		@Override
		@JsonIgnore
		public abstract Set<Backlog> getChildren();

		@Override
		@JsonIgnore
		public abstract Set<Story> getStories();

		@Override
		@JsonIgnore
		public abstract Set<BacklogHourEntry> getHourEntries();

		@Override
		@JsonIgnore
		public abstract Set<StoryRank> getStoryRanks();
	}
	
	public static abstract class ExportableProject extends Project implements ExportableBacklog {
		@Override
		@JsonIgnore
		public abstract Set<BacklogHistoryEntry> getBacklogHistoryEntries();
		
		@Override
		@JsonIgnore
		public abstract Set<Backlog> getChildren();

		@Override
		@JsonIgnore
		public abstract Set<Story> getStories();

		@Override
		@JsonIgnore
		public abstract Set<BacklogHourEntry> getHourEntries();

		@Override
		@JsonIgnore
		public abstract Set<StoryRank> getStoryRanks();

		@Override
		@JsonIgnore
		public abstract Set<Assignment> getAssignments();
	}
	
	public static abstract class ExportableIteration extends Iteration implements ExportableBacklog {

		@Override
		@JsonIgnore
		public abstract Set<Task> getTasks();

		@Override
		@JsonIgnore
		public abstract Set<Assignment> getAssignments();

		@Override
		@JsonIgnore
		public abstract Set<IterationHistoryEntry> getHistoryEntries();

		@Override
		@JsonIgnore
		public abstract Set<Story> getAssignedStories();

		@Override
		@JsonIgnore
		public abstract Collection<Team> getTeams();
		
		@Override
		@JsonIgnore
		public abstract Set<Backlog> getChildren();

		@Override
		@JsonIgnore
		public abstract Set<Story> getStories();

		@Override
		@JsonIgnore
		public abstract Set<BacklogHourEntry> getHourEntries();

		@Override
		@JsonIgnore
		public abstract Set<StoryRank> getStoryRanks();
	}

	public static abstract class ExportableStory extends Story implements ExportableModel {
		@Override
		@JsonIgnore
		public abstract Set<StoryAccess> getStoryAccesses();

		@Override
		@JsonIgnore
		public abstract Set<WhatsNextStoryEntry> getWhatsNextStoryEntries();

		@Override
		@JsonIgnore
		public abstract Set<Task> getTasks();

		@Override
		@JsonIgnore
		public abstract Set<StoryHourEntry> getHourEntries();

		@Override
		@JsonIgnore
		public abstract List<Story> getChildren();

		@Override
		@JsonIgnore
		public abstract Set<StoryRank> getStoryRanks();

		@Override
		@JsonIgnore
		public abstract Set<Label> getLabels();
	}
	
	public static abstract class ExportableTask extends Task implements ExportableModel {

		@Override
		@JsonIgnore
		public abstract Set<WhatsNextEntry> getWhatsNextEntries();

		@Override
		@JsonIgnore
		public abstract Set<TaskHourEntry> getHourEntries();
	}
	
	public static abstract class ExportableWidgetCollection extends WidgetCollection implements ExportableModel {

		@Override
		@JsonIgnore
		public abstract Collection<AgilefantWidget> getWidgets();
	}

	public static abstract class ExportableAgilefantWidget extends AgilefantWidget implements ExportableModel {

		@Override
		@JsonIgnore
		public abstract Integer getObjectId();
		
	}
	
	@SuppressWarnings("serial")
	public static class VersionMismatchException extends Exception {

		public VersionMismatchException(String message) {
			super(message);
		}
	}
	
	public static class ExportData {
		public String version;
	}
}
