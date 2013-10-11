package fi.hut.soberit.agilefant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import fi.hut.soberit.agilefant.util.DbConnectionInfo;

@Configuration
@Import({ BusinessConfiguration.class, DaoConfiguration.class, RestConfiguration.class, TransactionConfiguration.class, WebConfiguration.class, ExportImportConfiguration.class, UtilConfiguration.class })
@ImportResource({ "/WEB-INF/applicationContext-core.xml", "/WEB-INF/applicationContext-email.xml", "/WEB-INF/applicationContext-notifications.xml",
        "/WEB-INF/applicationContext-security.xml" })
public class MasterConfiguration {

    @Bean
    public ConfigFactoryBean config() {
        return new ConfigFactoryBean();
    }

    @Bean
    public DataSourceFactoryBean dataSource() {
        return new DataSourceFactoryBean();
    }

    @Bean
    public DbConnectionInfo dbConnectionInfo() throws Exception {
        return DbConnectionInfo.create(config().getObject(), dataSource().getObject());
    }

}
