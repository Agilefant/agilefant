package fi.hut.soberit.agilefant.db.export;

import java.io.ByteArrayOutputStream;
import fi.hut.soberit.agilefant.business.ExportImportBusiness;
import fi.hut.soberit.agilefant.exportimport.ExportImport;

/**
 * Provides a zipped database dump in an output stream
 */
public class XmlBackupper {

    private ByteArrayOutputStream dbOutputStream;

    /**
     * 
     */
    public XmlBackupper() {
        dbOutputStream = new ByteArrayOutputStream();
    }

    /**
     * Calls Dbbackup to generate zipped stream Returns ByteArrayOutputStream
     * that is zipped DBdump from exportImportBusiness or
     * null if generating stream failed for some reason.
     */
    public ByteArrayOutputStream generateDBDumpStream(ExportImportBusiness exportImportBusiness, ExportImport exportImport) throws Exception {
        XmlBackupStreamGenerator dbbackup = new XmlBackupStreamGenerator();
        dbbackup.generateZippedDbOutputStream(exportImportBusiness, exportImport);

        this.dbOutputStream = dbbackup.getZippedDbOutputStream();
        return dbbackup.getZippedDbOutputStream();
    }

    /**
     * Returns and output stream that contains the zipped dbdump
     */
    public ByteArrayOutputStream getDbOutputStream() {
        return this.dbOutputStream;
    }

}
