<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<h3 class="fw-bold text-dark mb-4"><i class="fa-solid fa-cart-shopping text-primary me-2"></i>My Shopping Cart</h3>

<c:choose>
    <c:when test="${empty cartItems}">
        <div class="card border-0 shadow-sm text-center py-5 rounded-3">
            <div class="card-body">
                <i class="fa-solid fa-basket-shopping display-1 text-muted mb-3"></i>
                <h5>Your shopping cart is empty!</h5>
                <p class="text-muted">Explore our wide range of products and add items to your cart.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-flipkart rounded-pill px-4 mt-2">Shop Now</a>
            </div>
        </div>
    </c:when>

    <c:otherwise>
        <div class="row g-4">
            <!-- Cart Items List -->
            <div class="col-md-8">
                <div class="card border-0 shadow-sm rounded-3">
                    <div class="card-body p-0">
                        <c:forEach var="item" items="${cartItems}">
                            <div class="d-flex align-items-center p-3 border-bottom gap-3">
                                <img src="<c:out value="${item.product.imageUrl}"/>" style="width: 90px; height: 90px; object-fit: contain;" class="rounded" alt="Product"
                                     onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">
                                <div class="flex-grow-1">
                                    <h6 class="fw-bold mb-1"><a href="${pageContext.request.contextPath}/products/detail?id=${item.product.id}" class="text-dark text-decoration-none"><c:out value="${item.product.name}"/></a></h6>
                                    <div class="text-muted small mb-2">Category: <c:out value="${item.product.category}"/></div>
                                    <div class="fw-bold text-dark fs-5">₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></div>
                                </div>

                                <!-- Quantity Controls -->
                                <div class="d-flex align-items-center gap-2">
                                    <form action="${pageContext.request.contextPath}/cart/update" method="post" class="d-flex align-items-center">
                                        <input type="hidden" name="cartItemId" value="${item.id}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stockQty}" class="form-control form-control-sm text-center" style="width: 70px;" onchange="this.form.submit()">
                                    </form>

                                    <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                        <input type="hidden" name="cartItemId" value="${item.id}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm text-uppercase fw-bold"><i class="fa-solid fa-trash me-1"></i> Remove</button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <!-- Price Summary Side Panel -->
            <div class="col-md-4">
                <div class="card border-0 shadow-sm rounded-3">
                    <div class="card-body">
                        <h5 class="fw-bold text-uppercase text-muted mb-3 border-bottom pb-2">Price Details</h5>
                        
                        <div class="d-flex justify-content-between mb-2 text-secondary">
                            <span>Price (<c:out value="${fn:length(cartItems)}"/> items)</span>
                            <span>₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/></span>
                        </div>
                        <div class="d-flex justify-content-between mb-2 text-secondary">
                            <span>Delivery Charges</span>
                            <span class="text-success fw-bold">FREE</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between fs-5 fw-bold text-dark mb-4">
                            <span>Total Amount</span>
                            <span class="text-primary">₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/></span>
                        </div>

                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-flipkart btn-lg w-100 py-3 text-uppercase font-weight-bold shadow-sm">
                            Proceed to Checkout <i class="fa-solid fa-arrow-right ms-2"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/footer.jsp" />
