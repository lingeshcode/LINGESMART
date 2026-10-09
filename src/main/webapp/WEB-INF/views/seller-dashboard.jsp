<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-4">
    <h3 class="fw-bold text-dark mb-0"><i class="fa-solid fa-store text-primary me-2"></i>Seller Hub & Sales Dashboard</h3>
    <a href="${pageContext.request.contextPath}/seller/product/add" class="btn btn-flipkart font-weight-bold shadow-sm">
        <i class="fa-solid fa-plus me-1"></i> Add New Product Listing
    </a>
</div>

<!-- Seller Analytics Metric Cards (Optional Feature O3) -->
<div class="row g-3 mb-4">
    <div class="col-md-3">
        <div class="card border-0 shadow-sm bg-primary text-white p-3 rounded-3">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h6 class="text-white-50 text-uppercase small font-weight-bold mb-1">Active Products</h6>
                    <h3 class="fw-bold mb-0"><c:out value="${metrics.totalProducts}"/></h3>
                </div>
                <i class="fa-solid fa-boxes-stacked display-6 opacity-50"></i>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card border-0 shadow-sm bg-success text-white p-3 rounded-3">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h6 class="text-white-50 text-uppercase small font-weight-bold mb-1">Total Revenue</h6>
                    <h3 class="fw-bold mb-0">₹<fmt:formatNumber value="${metrics.totalRevenue}" pattern="#,##0.00"/></h3>
                </div>
                <i class="fa-solid fa-indian-rupee-sign display-6 opacity-50"></i>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card border-0 shadow-sm bg-info text-white p-3 rounded-3">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h6 class="text-white-50 text-uppercase small font-weight-bold mb-1">Total Incoming Orders</h6>
                    <h3 class="fw-bold mb-0"><c:out value="${metrics.totalOrders}"/></h3>
                </div>
                <i class="fa-solid fa-cart-flatbed display-6 opacity-50"></i>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card border-0 shadow-sm bg-warning text-dark p-3 rounded-3">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h6 class="text-dark-50 text-uppercase small font-weight-bold mb-1">Pending Processing</h6>
                    <h3 class="fw-bold mb-0"><c:out value="${metrics.pendingOrdersCount}"/></h3>
                </div>
                <i class="fa-solid fa-clock display-6 opacity-50"></i>
            </div>
        </div>
    </div>
</div>

<!-- Seller Product Listings Table -->
<div class="card border-0 shadow-sm rounded-3">
    <div class="card-header bg-white py-3">
        <h5 class="fw-bold text-dark mb-0">My Listed Products</h5>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Product Info</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock Qty</th>
                        <th class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td>
                                <div class="d-flex align-items-center gap-3">
                                    <img src="<c:out value="${p.imageUrl}"/>" style="width: 50px; height: 50px; object-fit: contain;" class="rounded" alt="Prod"
                                         onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">
                                    <div>
                                        <h6 class="fw-bold mb-0"><c:out value="${p.name}"/></h6>
                                        <span class="text-muted small">ID: #<c:out value="${p.id}"/></span>
                                    </div>
                                </div>
                            </td>
                            <td><span class="badge bg-secondary"><c:out value="${p.category}"/></span></td>
                            <td class="fw-bold">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.stockQty > 0}">
                                        <span class="badge bg-success"><c:out value="${p.stockQty}"/> in stock</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Out of stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/seller/product/edit?id=${p.id}" class="btn btn-outline-primary btn-sm me-1"><i class="fa-solid fa-pen me-1"></i> Edit</a>
                                <form action="${pageContext.request.contextPath}/seller/product/delete" method="post" class="d-inline" onsubmit="return confirm('Delete product listing?')">
                                    <input type="hidden" name="id" value="${p.id}">
                                    <button type="submit" class="btn btn-outline-danger btn-sm"><i class="fa-solid fa-trash me-1"></i> Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
