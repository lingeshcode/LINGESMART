<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row justify-content-center py-3">
    <div class="col-md-7">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-body p-4">
                <h4 class="fw-bold text-primary mb-3">
                    <c:choose>
                        <c:when test="${not empty product}"><i class="fa-solid fa-pen-to-square me-2"></i>Edit Product Listing</c:when>
                        <c:otherwise><i class="fa-solid fa-plus-circle me-2"></i>Create New Product Listing</c:otherwise>
                    </c:choose>
                </h4>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger small"><c:out value="${errorMessage}"/></div>
                </c:if>

                <form action="${pageContext.request.contextPath}/seller/product/${not empty product ? 'edit' : 'add'}" method="post">
                    <c:if test="${not empty product}">
                        <input type="hidden" name="id" value="${product.id}">
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Product Title</label>
                        <input type="text" name="name" class="form-control" value="<c:out value="${product.name}"/>" placeholder="e.g. Sony WH-1000XM5 Headphones" required>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Price (INR)</label>
                            <input type="number" step="0.01" name="price" class="form-control" value="${product.price}" placeholder="29990.00" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Stock Quantity</label>
                            <input type="number" name="stockQty" class="form-control" value="${product.stockQty}" placeholder="25" min="0" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Category</label>
                        <select name="category" class="form-select" required>
                            <option value="Electronics" <c:if test="${product.category eq 'Electronics'}">selected</c:if>>Electronics</option>
                            <option value="Fashion" <c:if test="${product.category eq 'Fashion'}">selected</c:if>>Fashion</option>
                            <option value="Home" <c:if test="${product.category eq 'Home'}">selected</c:if>>Home</option>
                            <option value="Books" <c:if test="${product.category eq 'Books'}">selected</c:if>>Books</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Image URL</label>
                        <input type="url" name="imageUrl" class="form-control" value="<c:out value="${product.imageUrl}"/>" placeholder="https://images.unsplash.com/...">
                        <div class="form-text small text-muted">Direct HTTP/HTTPS image URL link.</div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold">Product Description</label>
                        <textarea name="description" class="form-control" rows="4" placeholder="Enter key features, specifications, and details..."><c:out value="${product.description}"/></textarea>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-outline-secondary px-4">Cancel</a>
                        <button type="submit" class="btn btn-flipkart px-4 shadow-sm">Save Product</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
