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

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.ExportImportBusiness;
import fi.hut.soberit.agilefant.business.ExportImportBusiness.OrganizationDumpTO;
import fi.hut.soberit.agilefant.db.export.XmlBackupper;
import fi.hut.soberit.agilefant.exportimport.ExportImport;

@Component("dbExportAction")
@Scope("prototype")
public class DatabaseExportAction extends ActionSupport {

    private static final long serialVersionUID = -1639488740106383276L;

    private XmlBackupper takeDbBackup;
    private ByteArrayOutputStream databaseStream;
    @Autowired
    private ExportImportBusiness exportImportBusiness;
    @Autowired
    private ExportImport exportImport;
    
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
            e.printStackTrace();
            return Action.ERROR;
        }
    }
    
    public String databaseImport() {
        try {
            InputStream inputStream = new FileInputStream(fileUpload);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            zipInputStream.getNextEntry();
            OrganizationDumpTO organizationTO = exportImport.fromJson(zipInputStream);
            exportImportBusiness.importOrganization(organizationTO);
            return Action.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return Action.ERROR;
        }
    }

    public InputStream getDatabaseStream() {
        return new ByteArrayInputStream(this.databaseStream.toByteArray());
    }

    public void setDatabaseStream(ByteArrayOutputStream databaseStream) {
        this.databaseStream = databaseStream;
    }

}
