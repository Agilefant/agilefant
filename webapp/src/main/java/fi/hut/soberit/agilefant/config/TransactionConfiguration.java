package fi.hut.soberit.agilefant.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement
public class TransactionConfiguration implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SessionFactory sessionFactory;

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager bean = new HibernateTransactionManager();
        bean.setDataSource(dataSource);
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }

}
