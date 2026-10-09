package com.lingesh.lingeshmart.controller;

import com.lingesh.lingeshmart.dto.ApiResponseDTO;
import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.service.ProductService;
import com.lingesh.lingeshmart.util.JsonUtil;
import com.lingesh.lingeshmart.util.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/v1/products/*")
public class ProductsApiServlet extends HttpServlet {

    private final ProductService productService = ServiceFactory.getProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        String path = req.getPathInfo();

        try {
            if (path == null || "/".equals(path)) {
                String q = req.getParameter("q");
                String category = req.getParameter("category");
                List<Product> products = productService.searchProducts(q, category);
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.success(products)));
            } else {
                Long id = Long.parseLong(path.substring(1));
                Product product = productService.getProductById(id);
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.success(product)));
            }
        } catch (NumberFormatException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.error("INVALID_INPUT", "Invalid product ID format")));
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(JsonUtil.toJson(ApiResponseDTO.error("RESOURCE_NOT_FOUND", e.getMessage())));
        }
    }
}
