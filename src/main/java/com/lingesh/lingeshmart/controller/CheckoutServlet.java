package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.CartItem;
import com.lingesh.lingeshmart.model.Order;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.CartService;
import com.lingesh.lingeshmart.service.OrderService;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = ServiceFactory.getCartService();
    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        List<CartItem> cartItems = cartService.getUserCart(user.getId());
        if (cartItems.isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/cart?error=Cart+is+empty");
            return;
        }

        BigDecimal grandTotal = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            grandTotal = grandTotal.add(ci.getItemTotal());
        }

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("grandTotal", grandTotal);

        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        try {
            // Mock payment confirmation process
            Order order = orderService.placeOrderFromCart(user.getId());
            res.sendRedirect(req.getContextPath() + "/orders/detail?id=" + order.getId() + "&success=Order+placed+successfully!+Payment+Confirmed.");
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/cart?error=" + e.getMessage());
        }
    }
}
