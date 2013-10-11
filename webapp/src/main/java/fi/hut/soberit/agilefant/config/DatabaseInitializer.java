package fi.hut.soberit.agilefant.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseInitializer implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        if (jdbc.queryForInt("SELECT COUNT(*) FROM users") > 0) {
            log.info("Users table has data -> skipping initialization");
            return;
        }

        jdbc.update("INSERT INTO users (fullName, loginName, password, initials, enabled, recentItemsNumberOfWeeks) VALUES (?, ?, ?, ?, ?, ?)",
                "Administrator", "admin", "5ebe2294ecd0e0f08eab7690d2a6ee69", "Admin", 1, 16);
        jdbc.update("INSERT INTO users (admin, fullName, loginName, password, initials, enabled, recentItemsNumberOfWeeks) VALUES (?, ?, ?, ?, ?, ?, ?)", 0,
                "readonly", "readonly", "5ebe2294ecd0e0f08eab7690d2a6ee69", "readonly", 1, 0);

        log.info("Initialized users table with default users");
    }

}
