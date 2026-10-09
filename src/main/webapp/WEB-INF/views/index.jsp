<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<!-- Category Navigation Bar -->
<div class="card border-0 shadow-sm mb-4">
    <div class="card-body py-2">
        <div class="d-flex flex-wrap gap-2 align-items-center justify-content-center">
            <span class="fw-bold me-2 text-muted small"><i class="fa-solid fa-filter me-1"></i>Categories:</span>
            <a href="${pageContext.request.contextPath}/home?category=ALL" class="btn btn-sm ${selectedCategory eq 'ALL' ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">All Products</a>
            <a href="${pageContext.request.contextPath}/home?category=Electronics" class="btn btn-sm ${selectedCategory eq 'Electronics' ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">Electronics</a>
            <a href="${pageContext.request.contextPath}/home?category=Fashion" class="btn btn-sm ${selectedCategory eq 'Fashion' ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">Fashion</a>
            <a href="${pageContext.request.contextPath}/home?category=Home" class="btn btn-sm ${selectedCategory eq 'Home' ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">Home Appliances</a>
            <a href="${pageContext.request.contextPath}/home?category=Books" class="btn btn-sm ${selectedCategory eq 'Books' ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">Books</a>
        </div>
    </div>
</div>

<!-- Hero Banner -->
<div class="bg-primary text-white p-4 rounded-3 mb-4 shadow-sm" style="background: linear-gradient(135deg, #1e5799 0%,#2874f0 100%);">
    <div class="row align-items-center">
        <div class="col-md-8">
            <h2 class="fw-bold mb-2">Welcome to LingeshMart Big Billion Days!</h2>
            <p class="mb-0 text-white-50">Unbeatable prices on Electronics, Fashion, Home Decor & Books from trusted sellers nationwide.</p>
        </div>
        <div class="col-md-4 text-end d-none d-md-block">
            <i class="fa-solid fa-bag-shopping display-1 opacity-50"></i>
        </div>
    </div>
</div>

<!-- Product Grid -->
<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="fw-bold text-dark mb-0">
        <c:choose>
            <c:when test="${not empty query}">Search Results for "<c:out value="${query}"/>"</c:when>
            <c:when test="${selectedCategory ne 'ALL'}"><c:out value="${selectedCategory}"/> Products</c:when>
            <c:otherwise>Featured Products</c:otherwise>
        </c:choose>
    </h4>
    <span class="text-muted small"><c:out value="${fn:length(products)}"/> products found</span>
</div>

<c:choose>
    <c:when test="${empty products}">
        <div class="card border-0 shadow-sm text-center py-5">
            <div class="card-body">
                <i class="fa-solid fa-box-open display-1 text-muted mb-3"></i>
                <h5>No products found matching your search criteria.</h5>
                <p class="text-muted">Try changing category filters or search keywords.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary rounded-pill px-4">View All Products</a>
            </div>
        </div>
    </c:when>

    <c:otherwise>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
            <c:forEach var="p" items="${products}">
                <div class="col">
                    <div class="card h-100 card-product shadow-sm">
                        <div class="position-relative text-center p-3 bg-white" style="height: 220px; overflow: hidden; border-radius: 8px 8px 0 0;">
                            <img src="<c:out value="${p.imageUrl}"/>" class="img-fluid h-100" style="object-fit: contain;" alt="<c:out value="${p.name}"/>"
                                 onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">
                            <span class="position-absolute top-0 start-0 m-2 badge badge-category"><c:out value="${p.category}"/></span>
                        </div>
                        <div class="card-body d-flex flex-column justify-content-between">
                            <div>
                                <div class="text-muted small mb-1"><i class="fa-solid fa-shop me-1"></i>Seller: <c:out value="${p.sellerName}"/></div>
                                <h6 class="card-title fw-bold text-dark text-truncate mb-2" title="<c:out value="${p.name}"/>">
                                    <a href="${pageContext.request.contextPath}/products/detail?id=${p.id}" class="text-decoration-none text-dark"><c:out value="${p.name}"/></a>
                                </h6>
                            </div>

                            <div>
                                <div class="d-flex align-items-baseline gap-2 mb-3">
                                    <span class="price-tag">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></span>
                                    <c:choose>
                                        <c:when test="${p.stockQty > 0}">
                                            <span class="badge bg-success-subtle text-success small font-weight-bold">In Stock (<c:out value="${p.stockQty}"/>)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger-subtle text-danger small font-weight-bold">Out of Stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="d-grid gap-2">
                                    <a href="${pageContext.request.contextPath}/products/detail?id=${p.id}" class="btn btn-outline-primary btn-sm fw-bold">View Details</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/footer.jsp" />
