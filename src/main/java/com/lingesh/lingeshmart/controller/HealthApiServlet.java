package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.dto.ApiResponseDTO;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import com.lingesh.lingeshmart.util.JsonUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/v1/health")
public class HealthApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("status", "UP");

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeQuery("SELECT 1");
            statusMap.put("db", "UP");
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.success(statusMap)));
        } catch (Exception e) {
            statusMap.put("db", "DOWN");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.error("DATABASE_DOWN", "Database connectivity check failed: " + e.getMessage())));
        }
    }
}
