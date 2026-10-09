package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.dto.ApiResponseDTO;
import com.lingesh.lingeshmart.dto.ChatRequestDTO;
import com.lingesh.lingeshmart.dto.ChatResponseDTO;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.service.ChatService;
import com.lingesh.lingeshmart.util.JsonUtil;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/api/v1/chat", "/api/chat"})
public class ChatApiServlet extends HttpServlet {

    private final ChatService chatService = ServiceFactory.getChatService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");

        HttpSession session = req.getSession(true);
        String sessionId = session.getId();

        try (InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)) {
            ChatRequestDTO requestDTO = JsonUtil.fromJson(readAll(reader), ChatRequestDTO.class);
            if (requestDTO == null || requestDTO.getMessage() == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.error("BAD_REQUEST", "Message body is required")));
                return;
            }

            String reply = chatService.processChat(sessionId, requestDTO.getMessage());

            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.success(new ChatResponseDTO(reply))));
        } catch (ValidationException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.error("VALIDATION_ERROR", e.getMessage())));
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.error("INTERNAL_ERROR", "Chatbot service unavailable")));
        }
    }

    private String readAll(InputStreamReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int n;
        while ((n = reader.read(buf)) != -1) {
            sb.append(buf, 0, n);
        }
        return sb.toString();
    }
}
