package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.model.WishlistItem;
import com.lingesh.lingeshmart.service.WishlistService;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/wishlist/*")
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = ServiceFactory.getWishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login?error=Please+login+to+view+your+wishlist");
            return;
        }

        List<WishlistItem> wishlist = wishlistService.getUserWishlist(user.getId());
        req.setAttribute("wishlistItems", wishlist);
        req.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String path = req.getPathInfo();
        try {
            if ("/add".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                wishlistService.addToWishlist(user.getId(), productId);
                res.sendRedirect(req.getContextPath() + "/products/detail?id=" + productId + "&msg=Added+to+wishlist!");
            } else if ("/remove".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                wishlistService.removeFromWishlist(user.getId(), productId);
                res.sendRedirect(req.getContextPath() + "/wishlist?msg=Removed+from+wishlist");
            }
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/wishlist?error=" + e.getMessage());
        }
    }
}
