package com.lingesh.lingeshmart.service;

public interface ChatProvider {
    String getReply(String userMessage, String context);
}
