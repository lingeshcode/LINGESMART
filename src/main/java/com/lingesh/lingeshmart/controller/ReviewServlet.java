package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.ReviewService;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/reviews/*")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = ServiceFactory.getReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login?error=Please+login+to+leave+a+review");
            return;
        }

        String path = req.getPathInfo();
        if ("/add".equals(path)) {
            try {
                Long productId = Long.parseLong(req.getParameter("productId"));
                Integer rating = Integer.parseInt(req.getParameter("rating"));
                String comment = req.getParameter("comment");

                reviewService.addReview(productId, user.getId(), rating, comment);
                res.sendRedirect(req.getContextPath() + "/products/detail?id=" + productId + "&msg=Review+submitted+successfully!");
            } catch (Exception e) {
                String productId = req.getParameter("productId");
                res.sendRedirect(req.getContextPath() + "/products/detail?id=" + productId + "&error=" + e.getMessage());
            }
        }
    }
}
