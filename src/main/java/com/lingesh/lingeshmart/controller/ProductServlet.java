package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.model.Review;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.ProductService;
import com.lingesh.lingeshmart.service.ReviewService;
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

@WebServlet("/products/detail")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = ServiceFactory.getProductService();
    private final ReviewService reviewService = ServiceFactory.getReviewService();
    private final WishlistService wishlistService = ServiceFactory.getWishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            res.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            Long productId = Long.parseLong(idStr);
            Product product = productService.getProductById(productId);
            List<Review> reviews = reviewService.getProductReviews(productId);
            double avgRating = reviewService.getAverageRating(productId);

            boolean inWishlist = false;
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                inWishlist = wishlistService.isItemInWishlist(user.getId(), productId);
            }

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("avgRating", avgRating);
            req.setAttribute("inWishlist", inWishlist);

            req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, res);
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/home?error=" + e.getMessage());
        }
    }
}
