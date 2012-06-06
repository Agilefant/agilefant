package fi.hut.soberit.agilefant.db.export;

import fi.hut.soberit.agilefant.util.DbConnectionInfo;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class Sqlfilecontentgenerator {

    private static DataSource createDataSource() {
        DbConnectionInfo dbInfo = new DbConnectionInfo();

        Class<?> driverClass;
        try {
            driverClass = Class.forName(dbInfo.getDriver());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver class", e);
        }

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(dbInfo.getUrl());
        dataSource.setUsername(dbInfo.getUsername());
        dataSource.setPassword(dbInfo.getPassword());
        return dataSource;
    }

    private static List<String> extractTableNames(ResultSet rs) throws SQLException {
        List<String> names = new ArrayList<String>();
        while(rs.next()) {
            names.add(rs.getString("table_name"));
        }
        return names;
    }

    private static String createSqlString(List<String> names) {
        StringBuilder sb = new StringBuilder();

        for (String name : names) {
            sb.append("RENAME TABLE ");
            sb.append(name);
            sb.append(" TO ");
            sb.append(name.substring(name.indexOf('_') + 1));
            sb.append(';');
            sb.append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }

    public static String generateSqlScript() {
        JdbcTemplate jdbc = new JdbcTemplate(createDataSource());
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'agilefant' AND table_name LIKE 'anonym_%'";

        // Spring >3.x would make this cast unnecessary because ResultSetExtractor uses generics
        return (String) jdbc.query(query, new ResultSetExtractor() {
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                return createSqlString(extractTableNames(rs));
            }
        });
    }
}
