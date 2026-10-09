package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.CartItem;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.CartService;
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

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

    private final CartService cartService = ServiceFactory.getCartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login?error=Please+login+to+view+your+cart");
            return;
        }

        List<CartItem> items = cartService.getUserCart(user.getId());

        BigDecimal grandTotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            grandTotal = grandTotal.add(item.getItemTotal());
        }

        req.setAttribute("cartItems", items);
        req.setAttribute("grandTotal", grandTotal);

        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login?error=Please+login+to+manage+cart");
            return;
        }

        String path = req.getPathInfo();
        try {
            if ("/add".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                int qty = Integer.parseInt(req.getParameter("quantity"));
                cartService.addToCart(user.getId(), productId, qty);
                res.sendRedirect(req.getContextPath() + "/cart?msg=Item+added+to+cart");
            } else if ("/update".equals(path)) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                int qty = Integer.parseInt(req.getParameter("quantity"));
                cartService.updateItemQuantity(cartItemId, qty);
                res.sendRedirect(req.getContextPath() + "/cart");
            } else if ("/remove".equals(path)) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                cartService.removeItemFromCart(cartItemId, user.getId());
                res.sendRedirect(req.getContextPath() + "/cart?msg=Item+removed");
            } else {
                res.sendRedirect(req.getContextPath() + "/cart");
            }
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/cart?error=" + e.getMessage());
        }
    }
}
