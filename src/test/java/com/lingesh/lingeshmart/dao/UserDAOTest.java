package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static HikariDataSource ds;
    private UserDAO userDAO;

    @BeforeAll
    static void setUpDatabase() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");

        ds = new HikariDataSource(config);
        DatabaseUtil.setDataSource(ds);

        try (InputStream is = UserDAOTest.class.getClassLoader().getResourceAsStream("schema.sql");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
             Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            StringBuilder sql = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                sql.append(buffer, 0, bytesRead);
            }
            for (String command : sql.toString().split(";")) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command);
                }
            }
        }
    }

    @AfterAll
    static void tearDown() {
        DatabaseUtil.closeDataSource();
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    void testCreateAndFindUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@lingeshmart.com");
        user.setPasswordHash("hashed_pass");
        user.setRole(User.Role.BUYER);

        User created = userDAO.create(user);
        assertNotNull(created.getId());

        Optional<User> found = userDAO.findByEmail("test@lingeshmart.com");
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getName());
        assertEquals(User.Role.BUYER, found.get().getRole());
    }
}
