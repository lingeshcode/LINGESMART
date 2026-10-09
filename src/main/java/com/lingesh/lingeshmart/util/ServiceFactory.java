package com.lingesh.lingeshmart.util;

import com.lingesh.lingeshmart.dao.CartDAO;
import com.lingesh.lingeshmart.dao.CartDAOImpl;
import com.lingesh.lingeshmart.dao.OrderDAO;
import com.lingesh.lingeshmart.dao.OrderDAOImpl;
import com.lingesh.lingeshmart.dao.ProductDAO;
import com.lingesh.lingeshmart.dao.ProductDAOImpl;
import com.lingesh.lingeshmart.dao.ReviewDAO;
import com.lingesh.lingeshmart.dao.ReviewDAOImpl;
import com.lingesh.lingeshmart.dao.UserDAO;
import com.lingesh.lingeshmart.dao.UserDAOImpl;
import com.lingesh.lingeshmart.dao.WishlistDAO;
import com.lingesh.lingeshmart.dao.WishlistDAOImpl;
import com.lingesh.lingeshmart.service.ChatProvider;
import com.lingesh.lingeshmart.service.ChatService;
import com.lingesh.lingeshmart.service.ChatServiceImpl;
import com.lingesh.lingeshmart.service.GeminiChatProvider;
import com.lingesh.lingeshmart.service.MockChatProvider;
import com.lingesh.lingeshmart.service.CartService;
import com.lingesh.lingeshmart.service.CartServiceImpl;
import com.lingesh.lingeshmart.service.OrderService;
import com.lingesh.lingeshmart.service.OrderServiceImpl;
import com.lingesh.lingeshmart.service.ProductService;
import com.lingesh.lingeshmart.service.ProductServiceImpl;
import com.lingesh.lingeshmart.service.ReviewService;
import com.lingesh.lingeshmart.service.ReviewServiceImpl;
import com.lingesh.lingeshmart.service.UserService;
import com.lingesh.lingeshmart.service.UserServiceImpl;
import com.lingesh.lingeshmart.service.WishlistService;
import com.lingesh.lingeshmart.service.WishlistServiceImpl;
import java.io.InputStream;
import java.util.Properties;

public class ServiceFactory {

    private static final UserDAO userDAO = new UserDAOImpl();
    private static final ProductDAO productDAO = new ProductDAOImpl();
    private static final OrderDAO orderDAO = new OrderDAOImpl();
    private static final CartDAO cartDAO = new CartDAOImpl();
    private static final ReviewDAO reviewDAO = new ReviewDAOImpl();
    private static final WishlistDAO wishlistDAO = new WishlistDAOImpl();

    private static final UserService userService = new UserServiceImpl(userDAO);
    private static final ProductService productService = new ProductServiceImpl(productDAO);
    private static final CartService cartService = new CartServiceImpl(cartDAO, productDAO);
    private static final OrderService orderService = new OrderServiceImpl(orderDAO, cartDAO, productDAO);
    private static final ReviewService reviewService = new ReviewServiceImpl(reviewDAO);
    private static final WishlistService wishlistService = new WishlistServiceImpl(wishlistDAO);
    private static final ChatService chatService;

    static {
        Properties props = new Properties();
        try (InputStream input = ServiceFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) props.load(input);
        } catch (Exception ignored) {}

        String providerType = props.getProperty("ai.chatbot.provider", "mock");
        String apiKey = props.getProperty("ai.gemini.apiKey", "");
        String model = props.getProperty("ai.gemini.model", "gemini-1.5-flash");

        ChatProvider provider;
        if ("gemini".equalsIgnoreCase(providerType)) {
            provider = new GeminiChatProvider(apiKey, model);
        } else {
            provider = new MockChatProvider();
        }
        chatService = new ChatServiceImpl(provider);
    }

    private ServiceFactory() {}

    public static UserService getUserService() { return userService; }
    public static ProductService getProductService() { return productService; }
    public static CartService getCartService() { return cartService; }
    public static OrderService getOrderService() { return orderService; }
    public static ReviewService getReviewService() { return reviewService; }
    public static WishlistService getWishlistService() { return wishlistService; }
    public static ChatService getChatService() { return chatService; }
}
