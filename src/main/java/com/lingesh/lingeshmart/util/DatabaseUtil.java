package com.lingesh.lingeshmart.util;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static HikariDataSource dataSource;

    private DatabaseUtil() {}

    public static synchronized void setDataSource(HikariDataSource ds) {
        dataSource = ds;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("HikariDataSource is not initialized by AppContextListener!");
        }
        return dataSource.getConnection();
    }

    public static synchronized void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Closing HikariCP DataSource...");
            dataSource.close();
            dataSource = null;
        }
    }
}
