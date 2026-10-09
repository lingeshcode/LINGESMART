package com.lingesh.lingeshmart.service;

import com.google.gson.JsonObject;
import com.lingesh.lingeshmart.util.JsonUtil;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeminiChatProvider implements ChatProvider {

    private static final Logger logger = LoggerFactory.getLogger(GeminiChatProvider.class);
    private final String apiKey;
    private final String model;

    public GeminiChatProvider(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = (model != null && !model.isEmpty()) ? model : "gemini-1.5-flash";
    }

    @Override
    public String getReply(String userMessage, String context) {
        if (apiKey == null || apiKey.trim().isEmpty() || "YOUR_GEMINI_API_KEY_HERE".equals(apiKey)) {
            logger.warn("Gemini API key not configured. Falling back to degraded mock response.");
            return new MockChatProvider().getReply(userMessage, context);
        }

        try {
            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            String prompt = "You are LingeshMart AI Assistant, a helpful customer service chatbot for the LingeshMart e-commerce marketplace.\n" +
                    "Answer user questions accurately and concisely regarding products, orders, shipping, and returns.\n" +
                    "User question: " + userMessage;

            String payload = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapeJson(prompt) + "\"}]}]}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject responseJson = JsonUtil.fromJson(readAll(isr), JsonObject.class);
                    String text = responseJson.getAsJsonArray("candidates")
                            .get(0).getAsJsonObject()
                            .getAsJsonObject("content")
                            .getAsJsonArray("parts")
                            .get(0).getAsJsonObject()
                            .get("text").getAsString();
                    return text.trim();
                }
            } else {
                logger.error("Gemini API returned error HTTP code: {}", responseCode);
            }
        } catch (Exception e) {
            logger.error("Error communicating with Gemini API", e);
        }

        // Degradation fallback per Section 11 rule #3
        return new MockChatProvider().getReply(userMessage, context);
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }

    private String readAll(InputStreamReader reader) throws Exception {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int n;
        while ((n = reader.read(buf)) != -1) {
            sb.append(buf, 0, n);
        }
        return sb.toString();
    }
}
