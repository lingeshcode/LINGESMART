package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.Order;
import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.OrderService;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String path = req.getPathInfo();
        if ("/detail".equals(path)) {
            Long orderId = Long.parseLong(req.getParameter("id"));
            Order order = orderService.getOrderById(orderId);

            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/views/order-detail.jsp").forward(req, res);
        } else {
            List<Order> orders;
            if (user.getRole() == User.Role.SELLER) {
                orders = orderService.getSellerIncomingOrders(user.getId());
            } else if (user.getRole() == User.Role.ADMIN) {
                orders = orderService.getAllOrders();
            } else {
                orders = orderService.getBuyerOrders(user.getId());
            }

            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, res);
        }
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
        if ("/update-status".equals(path)) {
            try {
                Long orderId = Long.parseLong(req.getParameter("orderId"));
                Order.Status newStatus = Order.Status.valueOf(req.getParameter("status"));

                orderService.updateOrderStatus(orderId, newStatus);
                res.sendRedirect(req.getContextPath() + "/orders?msg=Status+updated+to+" + newStatus);
            } catch (Exception e) {
                res.sendRedirect(req.getContextPath() + "/orders?error=" + e.getMessage());
            }
        }
    }
}
