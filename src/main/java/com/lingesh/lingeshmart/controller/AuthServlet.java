package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.User;
import com.lingesh.lingeshmart.service.UserService;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private final UserService userService = ServiceFactory.getUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getPathInfo();
        if ("/login".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
        } else if ("/register".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, res);
        } else if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            res.sendRedirect(req.getContextPath() + "/auth/login?msg=Logged+out+successfully");
        } else {
            res.sendRedirect(req.getContextPath() + "/auth/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            if ("/login".equals(path)) {
                String email = req.getParameter("email");
                String password = req.getParameter("password");

                User user = userService.loginUser(email, password);

                // Session Security: Invalidate old session and regenerate session ID on login
                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }
                HttpSession newSession = req.getSession(true);
                newSession.setMaxInactiveInterval(30 * 60); // 30 minutes explicit timeout
                newSession.setAttribute("user", user);

                if (user.getRole() == User.Role.SELLER) {
                    res.sendRedirect(req.getContextPath() + "/seller/dashboard");
                } else if (user.getRole() == User.Role.ADMIN) {
                    res.sendRedirect(req.getContextPath() + "/admin/dashboard");
                } else {
                    res.sendRedirect(req.getContextPath() + "/home");
                }
            } else if ("/register".equals(path)) {
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                String password = req.getParameter("password");
                String roleStr = req.getParameter("role");

                User.Role role = User.Role.BUYER;
                if ("SELLER".equalsIgnoreCase(roleStr)) {
                    role = User.Role.SELLER;
                }

                User user = userService.registerUser(name, email, password, role);

                HttpSession session = req.getSession(true);
                session.setMaxInactiveInterval(30 * 60);
                session.setAttribute("user", user);

                res.sendRedirect(req.getContextPath() + "/home?msg=Registration+successful!");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            if ("/login".equals(path)) {
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
            } else {
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, res);
            }
        }
    }
}
