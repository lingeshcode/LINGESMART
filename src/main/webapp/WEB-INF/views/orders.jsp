<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<h3 class="fw-bold text-dark mb-4"><i class="fa-solid fa-box-open text-primary me-2"></i>Order History</h3>

<c:choose>
    <c:when test="${empty orders}">
        <div class="card border-0 shadow-sm text-center py-5 rounded-3">
            <div class="card-body">
                <i class="fa-solid fa-receipt display-1 text-muted mb-3"></i>
                <h5>No orders found!</h5>
                <p class="text-muted">You haven't placed any orders on LingeshMart yet.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary rounded-pill px-4">Start Shopping</a>
            </div>
        </div>
    </c:when>

    <c:otherwise>
        <div class="d-flex flex-column gap-3">
            <c:forEach var="o" items="${orders}">
                <div class="card border-0 shadow-sm rounded-3">
                    <div class="card-header bg-light d-flex flex-wrap justify-content-between align-items-center py-3">
                        <div>
                            <span class="text-muted small">Order ID: <strong>#LM-<c:out value="${o.id}"/></strong></span>
                            <span class="text-muted small mx-2">•</span>
                            <span class="text-muted small">Placed on: <fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, HH:mm"/></span>
                            <c:if test="${not empty o.buyerName}">
                                <span class="text-muted small mx-2">•</span>
                                <span class="text-muted small">Buyer: <strong><c:out value="${o.buyerName}"/></strong></span>
                            </c:if>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <span class="badge ${o.status eq 'DELIVERED' ? 'bg-success' : o.status eq 'SHIPPED' ? 'bg-info' : o.status eq 'CANCELLED' ? 'bg-danger' : 'bg-warning text-dark'} px-3 py-2">
                                <c:out value="${o.status}"/>
                            </span>
                            <a href="${pageContext.request.contextPath}/orders/detail?id=${o.id}" class="btn btn-outline-primary btn-sm font-weight-bold">View Details</a>
                        </div>
                    </div>

                    <div class="card-body">
                        <c:forEach var="item" items="${o.items}">
                            <div class="d-flex align-items-center gap-3 py-2 border-bottom">
                                <img src="<c:out value="${item.productImageUrl}"/>" style="width: 60px; height: 60px; object-fit: contain;" class="rounded" alt="Item"
                                     onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">
                                <div class="flex-grow-1">
                                    <h6 class="fw-bold mb-0"><c:out value="${item.productName}"/></h6>
                                    <span class="text-muted small">Quantity: <c:out value="${item.quantity}"/> × ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></span>
                                </div>
                                <div class="fw-bold text-dark">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="card-footer bg-white d-flex justify-content-between align-items-center py-3">
                        <span class="text-muted small">Status Tracking Workflow: Pending → Confirmed → Shipped → Delivered</span>
                        <div class="fs-5 fw-bold text-dark">Total: <span class="text-primary">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></span></div>
                    </div>
                </div>
            </forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/footer.jsp" />
