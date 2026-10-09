<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<h3 class="fw-bold text-dark mb-4"><i class="fa-solid fa-heart text-danger me-2"></i>My Saved Wishlist</h3>

<c:choose>
    <c:when test="${empty wishlistItems}">
        <div class="card border-0 shadow-sm text-center py-5 rounded-3">
            <div class="card-body">
                <i class="fa-regular fa-heart display-1 text-muted mb-3"></i>
                <h5>Your Wishlist is empty!</h5>
                <p class="text-muted">Save items that you like to view or purchase later.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary rounded-pill px-4">Explore Marketplace</a>
            </div>
        </div>
    </c:when>

    <c:otherwise>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
            <c:forEach var="w" items="${wishlistItems}">
                <div class="col">
                    <div class="card h-100 card-product shadow-sm">
                        <div class="position-relative text-center p-3 bg-white" style="height: 200px;">
                            <img src="<c:out value="${w.product.imageUrl}"/>" class="img-fluid h-100" style="object-fit: contain;" alt="Img"
                                 onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">
                        </div>
                        <div class="card-body d-flex flex-column justify-content-between">
                            <div>
                                <h6 class="fw-bold text-dark text-truncate mb-2"><c:out value="${w.product.name}"/></h6>
                                <div class="price-tag mb-3">₹<fmt:formatNumber value="${w.product.price}" pattern="#,##0.00"/></div>
                            </div>
                            <div class="d-grid gap-2">
                                <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                    <input type="hidden" name="productId" value="${w.product.id}">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn btn-cart btn-sm w-100 fw-bold"><i class="fa-solid fa-cart-shopping me-1"></i> Move to Cart</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/wishlist/remove" method="post">
                                    <input type="hidden" name="productId" value="${w.product.id}">
                                    <button type="submit" class="btn btn-outline-danger btn-sm w-100 fw-bold"><i class="fa-solid fa-trash me-1"></i> Remove</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/footer.jsp" />
