package fi.hut.soberit.agilefant.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.typesafe.config.Config;

import fi.hut.soberit.agilefant.business.ExportImportBusiness;
import fi.hut.soberit.agilefant.business.ExportImportBusiness.OrganizationDumpTO;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.ProductBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.db.export.XmlBackupper;
import fi.hut.soberit.agilefant.exportimport.ExportImport;
import fi.hut.soberit.agilefant.exportimport.ExportImport.VersionMismatchException;

@Component("dbExportAction")
@Scope("prototype")
public class DatabaseExportAction extends ActionSupport {

    private static final long serialVersionUID = -1639488740106383276L;

    private XmlBackupper takeDbBackup;
    private ByteArrayOutputStream databaseStream;
    private String errorStacktrace;
    @Autowired
    private ExportImportBusiness exportImportBusiness;
    @Autowired
    private ExportImport exportImport;
    
    @Autowired
    private ProductBusiness productBusiness;
    
    @Autowired
    private IterationBusiness iterationBusiness;
    
    @Autowired
    private UserBusiness userBusiness;
    
    @Autowired
    private Config config;
    
    private File fileUpload;

    public File getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String execute() throws Exception{
        return SUCCESS;
    }
    
    public String display() {
        return NONE;
    }

    public String edit() {
        this.takeDbBackup = new XmlBackupper();

        return Action.SUCCESS;
    }

    public String generateDatabaseExport() {
        try {
            this.takeDbBackup = new XmlBackupper();
            this.databaseStream = takeDbBackup.generateDBDumpStream(exportImportBusiness, exportImport);
            
            return Action.SUCCESS;
        } catch (Exception e) {
        	errorStacktrace = Throwables.getStackTraceAsString(e);
            return Action.ERROR;
        }
    }
    
    public String databaseImport() {
        try {
            if (fileUpload == null) {
                return Action.NONE;
            }
            InputStream inputStream = new FileInputStream(fileUpload);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            zipInputStream.getNextEntry();
            OrganizationDumpTO organizationTO = exportImport.fromJson(zipInputStream);
            exportImportBusiness.importOrganization(organizationTO);
            return Action.SUCCESS;
        } catch (Exception e) {
            if (e.getCause() instanceof VersionMismatchException) {
                errorStacktrace = e.getMessage();
            } else {
                errorStacktrace = Throwables.getStackTraceAsString(e);
            }
            return Action.ERROR;
        }
    }

    public InputStream getDatabaseStream() {
        return new ByteArrayInputStream(this.databaseStream.toByteArray());
    }

    public void setDatabaseStream(ByteArrayOutputStream databaseStream) {
        this.databaseStream = databaseStream;
    }
    
    public String getErrorStacktrace() {
    	return errorStacktrace;
    }
    
    public Boolean getDatabaseHasExistingData() {
    	if (productBusiness.countAll() > 0 || iterationBusiness.countAll() > 0 || userBusiness.countAll() > 2) {
    		return true;
    	}
    	return false;
    }
    
    public Boolean getImportEnabled() {
    	return this.config.getBoolean("agilefant.import.enabled");
    }

}
