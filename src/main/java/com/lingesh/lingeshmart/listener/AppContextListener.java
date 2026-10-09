package com.lingesh.lingeshmart.listener;

import com.lingesh.lingeshmart.util.DatabaseUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing LingeshMart application context...");

        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                logger.warn("config.properties not found, falling back to default H2 configuration.");
            }
        } catch (Exception e) {
            logger.error("Failed to load config.properties", e);
        }

        String driver = props.getProperty("db.driver", "org.h2.Driver");
        String url = props.getProperty("db.url", "jdbc:h2:mem:lingeshmartdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        String user = props.getProperty("db.user", "sa");
        String password = props.getProperty("db.password", "");

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.maxSize", "10")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.minIdle", "2")));

        HikariDataSource ds = new HikariDataSource(config);
        DatabaseUtil.setDataSource(ds);
        logger.info("HikariCP Connection Pool initialized successfully.");

        // Initialize Database Schema and Seed Data
        runSqlScript(ds, "schema.sql");
        runSqlScript(ds, "seed.sql");
    }

    private void runSqlScript(HikariDataSource ds, String resourceName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                logger.warn("SQL script {} not found in classpath.", resourceName);
                return;
            }
            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                 Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement()) {

                StringBuilder sql = new StringBuilder();
                char[] buffer = new char[1024];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    sql.append(buffer, 0, bytesRead);
                }

                for (String command : sql.toString().split(";")) {
                    String trimmed = command.trim();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                        stmt.execute(trimmed);
                    }
                }
                logger.info("Successfully executed database script: {}", resourceName);
            }
        } catch (Exception e) {
            logger.error("Error executing database script {}", resourceName, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down LingeshMart application context...");
        DatabaseUtil.closeDataSource();
    }
}
