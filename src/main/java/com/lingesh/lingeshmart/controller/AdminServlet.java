package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.dto.UserResponseDTO;
import com.lingesh.lingeshmart.model.Order;
import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.OrderService;
import com.lingesh.lingeshmart.service.ProductService;
import com.lingesh.lingeshmart.service.UserService;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final UserService userService = ServiceFactory.getUserService();
    private final ProductService productService = ServiceFactory.getProductService();
    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.ADMIN) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin role required.");
            return;
        }

        List<UserResponseDTO> users = userService.getAllUsers();
        List<Product> products = productService.getAllProducts();
        List<Order> orders = orderService.getAllOrders();

        req.setAttribute("users", users);
        req.setAttribute("products", products);
        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.ADMIN) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = req.getPathInfo();
        try {
            if ("/listing/delete".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                productService.deleteProduct(productId);
                res.sendRedirect(req.getContextPath() + "/admin/dashboard?msg=Listing+moderated+and+removed");
            }
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/admin/dashboard?error=" + e.getMessage());
        }
    }
}
