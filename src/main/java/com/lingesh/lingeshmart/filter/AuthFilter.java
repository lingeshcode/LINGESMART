package com.lingesh.lingeshmart.filter;

import com.lingesh.lingeshmart.model.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Protected paths
        boolean requiresAuth = path.startsWith("/seller") || path.startsWith("/admin") ||
                path.startsWith("/buyer") || path.startsWith("/checkout") ||
                path.startsWith("/wishlist") || path.startsWith("/orders");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (requiresAuth) {
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/auth/login?error=Unauthorized+access.+Please+login.");
                return;
            }

            // Role based access checks
            if (path.startsWith("/seller") && user.getRole() != User.Role.SELLER && user.getRole() != User.Role.ADMIN) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Seller role required.");
                return;
            }

            if (path.startsWith("/admin") && user.getRole() != User.Role.ADMIN) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Admin role required.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
