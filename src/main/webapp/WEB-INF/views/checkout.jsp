<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row justify-content-center py-3">
    <div class="col-md-8">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-body p-4">
                <h3 class="fw-bold text-primary mb-3"><i class="fa-solid fa-credit-card me-2"></i>Checkout & Payment Confirmation</h3>
                <p class="text-muted small mb-4">Mock Payment Step (Per Specification Rule F5 - No real third-party payment gateway integration required).</p>

                <!-- Order Summary Breakdown -->
                <div class="bg-light p-3 rounded mb-4">
                    <h6 class="fw-bold text-dark border-bottom pb-2">Order Items Summary</h6>
                    <c:forEach var="item" items="${cartItems}">
                        <div class="d-flex justify-content-between align-items-center my-2 text-secondary">
                            <div>
                                <strong class="text-dark"><c:out value="${item.product.name}"/></strong> (x<c:out value="${item.quantity}"/>)
                            </div>
                            <span>₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></span>
                        </div>
                    </c:forEach>
                    <hr>
                    <div class="d-flex justify-content-between align-items-center fs-5 fw-bold text-dark">
                        <span>Grand Total Payable</span>
                        <span class="text-success">₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/></span>
                    </div>
                </div>

                <!-- Mock Payment Selection Form -->
                <form action="${pageContext.request.contextPath}/checkout" method="post">
                    <h6 class="fw-bold text-dark mb-3">Select Payment Method (Mock)</h6>

                    <div class="form-check mb-2 p-3 border rounded">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="payUpi" checked>
                        <label class="form-check-label fw-bold text-dark" for="payUpi">
                            <i class="fa-solid fa-qrcode text-primary me-2"></i> Instant UPI / Google Pay / PhonePe (Simulated)
                        </label>
                    </div>

                    <div class="form-check mb-2 p-3 border rounded">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="payCard">
                        <label class="form-check-label fw-bold text-dark" for="payCard">
                            <i class="fa-solid fa-credit-card text-success me-2"></i> Credit / Debit Card (Simulated)
                        </label>
                    </div>

                    <div class="form-check mb-4 p-3 border rounded">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="payCod">
                        <label class="form-check-label fw-bold text-dark" for="payCod">
                            <i class="fa-solid fa-hand-holding-dollar text-warning me-2"></i> Cash on Delivery (COD)
                        </label>
                    </div>

                    <button type="submit" class="btn btn-flipkart btn-lg w-100 py-3 text-uppercase font-weight-bold shadow-sm">
                        <i class="fa-solid fa-shield-check me-2"></i> Confirm Order & Pay ₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
