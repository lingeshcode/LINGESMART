package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.dto.SellerDashboardDTO;
import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.OrderService;
import com.lingesh.lingeshmart.service.ProductService;
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

@WebServlet("/seller/*")
public class SellerServlet extends HttpServlet {

    private final ProductService productService = ServiceFactory.getProductService();
    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || (user.getRole() != User.Role.SELLER && user.getRole() != User.Role.ADMIN)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller role required.");
            return;
        }

        String path = req.getPathInfo();
        if ("/product/edit".equals(path)) {
            Long id = Long.parseLong(req.getParameter("id"));
            Product product = productService.getProductById(id);
            req.setAttribute("product", product);
            req.getRequestDispatcher("/WEB-INF/views/seller-product-form.jsp").forward(req, res);
        } else if ("/product/add".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/seller-product-form.jsp").forward(req, res);
        } else {
            // Dashboard
            SellerDashboardDTO metrics = orderService.getSellerDashboardMetrics(user.getId());
            List<Product> products = productService.getProductsBySeller(user.getId());

            req.setAttribute("metrics", metrics);
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/views/seller-dashboard.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || (user.getRole() != User.Role.SELLER && user.getRole() != User.Role.ADMIN)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = req.getPathInfo();
        try {
            if ("/product/add".equals(path)) {
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                int stockQty = Integer.parseInt(req.getParameter("stockQty"));
                String category = req.getParameter("category");
                String imageUrl = req.getParameter("imageUrl");

                productService.createProduct(user.getId(), name, description, price, stockQty, category, imageUrl);
                res.sendRedirect(req.getContextPath() + "/seller/dashboard?msg=Product+added+successfully!");
            } else if ("/product/edit".equals(path)) {
                Long id = Long.parseLong(req.getParameter("id"));
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                int stockQty = Integer.parseInt(req.getParameter("stockQty"));
                String category = req.getParameter("category");
                String imageUrl = req.getParameter("imageUrl");

                productService.updateProduct(id, user.getId(), name, description, price, stockQty, category, imageUrl);
                res.sendRedirect(req.getContextPath() + "/seller/dashboard?msg=Product+updated+successfully!");
            } else if ("/product/delete".equals(path)) {
                Long id = Long.parseLong(req.getParameter("id"));
                productService.deleteProduct(id);
                res.sendRedirect(req.getContextPath() + "/seller/dashboard?msg=Product+deleted");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/seller-product-form.jsp").forward(req, res);
        }
    }
}
