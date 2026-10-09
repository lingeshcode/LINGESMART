package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.service.ProductService;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private final ProductService productService = ServiceFactory.getProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String keyword = req.getParameter("q");
        String category = req.getParameter("category");

        List<Product> products = productService.searchProducts(keyword, category);

        req.setAttribute("products", products);
        req.setAttribute("query", keyword);
        req.setAttribute("selectedCategory", category != null ? category : "ALL");

        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, res);
    }
}
