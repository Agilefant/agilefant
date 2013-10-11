package fi.hut.soberit.agilefant.db.export;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import fi.hut.soberit.agilefant.business.ExportImportBusiness;
import fi.hut.soberit.agilefant.business.ExportImportBusiness.OrganizationDumpTO;
import fi.hut.soberit.agilefant.exportimport.ExportImport;

/**
 * Generates a zipped database dump using ExportImportUtil and provides it in a ByteArrayOutputStream for access.
 */
public class XmlBackupStreamGenerator {

    private ByteArrayOutputStream zippedDbOutputStream;

    /**
     * Initializes the class for taking a xml dump
     */
    public XmlBackupStreamGenerator() {
        this.zippedDbOutputStream = new ByteArrayOutputStream();
    }

    /**
     * Returns dumped database as zipped ByteArrayOutputStream. It's usually a good idea to call generate first
     */
    public ByteArrayOutputStream getZippedDbOutputStream() {
        return this.zippedDbOutputStream;
    }


    public void generateZippedDbOutputStream(ExportImportBusiness exportImportBusiness, ExportImport exportImport) throws Exception {
        zippedDbOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
            
        OrganizationDumpTO organizationTO = exportImportBusiness.exportOrganization();
        exportImport.toJson(out, organizationTO);
            
        ZipOutputStream outzip = new ZipOutputStream(zippedDbOutputStream);
        outzip.putNextEntry(new ZipEntry("fantbackup.json"));
        out.writeTo(outzip);
        outzip.closeEntry();
        outzip.close();
    }

}
