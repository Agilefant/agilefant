package fi.hut.soberit.agilefant.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jndi.JndiLocatorDelegate;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public class DataSourceFactoryBean implements InitializingBean, DisposableBean, FactoryBean<DataSource> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Config config;

    private DataSource jndiDataSource;
    private BasicDataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(config, "config cannot be null");

        jndiDataSource = jndiLookup();
        if (jndiDataSource == null)
            dataSource = configureDataSource();
    }

    private BasicDataSource configureDataSource() {
        BasicDataSource bean = new BasicDataSource();

        bean.setDriverClassName(config.getString("agilefant.database.driver-class"));
        bean.setUsername(config.getString("agilefant.database.username"));
        bean.setPassword(config.getString("agilefant.database.password"));
        bean.setUrl(config.getString("agilefant.database.url"));
        bean.setTestWhileIdle(true);
        bean.setValidationQuery("/* ping */ SELECT 1");

        log.info("Using locally pooled data source '{}'", bean.getUrl());

        return bean;
    }

    /**
     * Attempts to lookup a JNDI data source using the name from configuration.
     * 
     * @return data source or null
     */
    private DataSource jndiLookup() {
        String jndiName = config.getString("agilefant.database.jndi-name");
        try {
            JndiLocatorDelegate jndi = new JndiLocatorDelegate();
            jndi.setResourceRef(true);

            DataSource dataSource = jndi.lookup(jndiName, DataSource.class);
            log.info("Using JNDI data source '{}'", jndiName);
            return dataSource;
        } catch (NamingException e) {
            log.info("JNDI data source '{}' could not be found", jndiName);
            return null;
        }
    }

    @Override
    public void destroy() throws Exception {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    @Override
    public DataSource getObject() throws Exception {
        return jndiDataSource != null ? jndiDataSource : dataSource;
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
