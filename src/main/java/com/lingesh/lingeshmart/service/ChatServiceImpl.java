package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.exception.ValidationException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceImpl implements ChatService {

    private final ChatProvider chatProvider;
    private final Map<String, Integer> sessionRequestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> sessionResetTimes = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> sessionCaches = new ConcurrentHashMap<>();

    public ChatServiceImpl(ChatProvider chatProvider) {
        this.chatProvider = chatProvider;
    }

    @Override
    public synchronized String processChat(String sessionId, String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            throw new ValidationException("Message cannot be empty");
        }
        if (userMessage.length() > 500) {
            throw new ValidationException("Message length exceeds limit of 500 characters");
        }

        // 1. Enforce Per-Session Rate Limit (10 msgs / min)
        long now = System.currentTimeMillis();
        Long resetTime = sessionResetTimes.getOrDefault(sessionId, 0L);
        if (now > resetTime) {
            sessionResetTimes.put(sessionId, now + 60000);
            sessionRequestCounts.put(sessionId, 1);
        } else {
            int count = sessionRequestCounts.getOrDefault(sessionId, 0) + 1;
            if (count > 10) {
                throw new ValidationException("Rate limit exceeded. Maximum 10 messages per minute allowed.");
            }
            sessionRequestCounts.put(sessionId, count);
        }

        // 2. Session In-Memory Cache Lookup for identical questions
        Map<String, String> cache = sessionCaches.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
        String key = userMessage.trim().toLowerCase();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // 3. Delegate to ChatProvider
        String reply = chatProvider.getReply(userMessage, "LingeshMart");
        cache.put(key, reply);
        return reply;
    }
}
