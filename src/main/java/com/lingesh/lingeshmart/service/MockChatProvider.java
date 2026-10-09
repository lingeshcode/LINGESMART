package com.lingesh.lingeshmart.service;

import java.util.HashMap;
import java.util.Map;

public class MockChatProvider implements ChatProvider {

    private final Map<String, String> faqDatabase = new HashMap<>();

    public MockChatProvider() {
        faqDatabase.put("shipping", "LingeshMart provides fast 2-3 business day shipping across India on all electronics and fashion orders.");
        faqDatabase.put("delivery", "Standard delivery takes 2 to 4 days. You can track your order status live in your Order History page.");
        faqDatabase.put("return", "We offer a hassle-free 7-day return policy for defective or incorrect products.");
        faqDatabase.put("payment", "LingeshMart currently supports Credit/Debit cards, NetBanking, UPI, and Cash on Delivery via mock payment checkout.");
        faqDatabase.put("seller", "To sell products on LingeshMart, register as a Seller and access your dedicated Seller Dashboard.");
        faqDatabase.put("cancel", "You can cancel any PENDING order directly from your Orders page before it gets shipped.");
    }

    @Override
    public String getReply(String userMessage, String context) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! How can I assist you with your LingeshMart shopping today?";
        }

        String msgLower = userMessage.toLowerCase();
        for (Map.Entry<String, String> entry : faqDatabase.entrySet()) {
            if (msgLower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "Welcome to LingeshMart Assistant! I can help you with product queries, order tracking, shipping info, returns, and seller registration.";
    }
}
