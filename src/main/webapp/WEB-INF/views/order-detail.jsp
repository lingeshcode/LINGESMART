<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row justify-content-center">
    <div class="col-md-9">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-header bg-primary text-white p-4 rounded-top-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h4 class="fw-bold mb-1">Order #LM-<c:out value="${order.id}"/></h4>
                        <span class="text-white-50 small">Placed on <fmt:formatDate value="${order.createdAt}" pattern="dd MMMM yyyy, HH:mm"/></span>
                    </div>
                    <span class="badge bg-light text-primary fs-6 px-3 py-2"><c:out value="${order.status}"/></span>
                </div>
            </div>

            <div class="card-body p-4">
                <!-- Status Timeline Bar -->
                <div class="bg-light p-3 rounded mb-4 text-center">
                    <h6 class="fw-bold text-dark mb-3">Order Status Workflow</h6>
                    <div class="d-flex justify-content-between position-relative px-4">
                        <span class="badge ${order.status eq 'PENDING' || order.status eq 'CONFIRMED' || order.status eq 'SHIPPED' || order.status eq 'DELIVERED' ? 'bg-primary' : 'bg-secondary'} rounded-circle p-3">1</span>
                        <span class="badge ${order.status eq 'CONFIRMED' || order.status eq 'SHIPPED' || order.status eq 'DELIVERED' ? 'bg-primary' : 'bg-secondary'} rounded-circle p-3">2</span>
                        <span class="badge ${order.status eq 'SHIPPED' || order.status eq 'DELIVERED' ? 'bg-primary' : 'bg-secondary'} rounded-circle p-3">3</span>
                        <span class="badge ${order.status eq 'DELIVERED' ? 'bg-success' : 'bg-secondary'} rounded-circle p-3">4</span>
                    </div>
                    <div class="d-flex justify-content-between small text-muted mt-2 px-2">
                        <span>Pending</span>
                        <span>Confirmed</span>
                        <span>Shipped</span>
                        <span>Delivered</span>
                    </div>
                </div>

                <!-- Seller Status Update Control (Optional Feature O2) -->
                <c:if test="${sessionScope.user.role eq 'SELLER' || sessionScope.user.role eq 'ADMIN'}">
                    <div class="card bg-warning-subtle border-warning p-3 mb-4">
                        <h6 class="fw-bold text-dark mb-2"><i class="fa-solid fa-pen-to-square me-1"></i> Update Order Status Workflow</h6>
                        <form action="${pageContext.request.contextPath}/orders/update-status" method="post" class="d-flex gap-2">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <select name="status" class="form-select form-select-sm" style="max-width: 220px;">
                                <option value="PENDING" <c:if test="${order.status eq 'PENDING'}">selected</c:if>>PENDING</option>
                                <option value="CONFIRMED" <c:if test="${order.status eq 'CONFIRMED'}">selected</c:if>>CONFIRMED</option>
                                <option value="SHIPPED" <c:if test="${order.status eq 'SHIPPED'}">selected</c:if>>SHIPPED</option>
                                <option value="DELIVERED" <c:if test="${order.status eq 'DELIVERED'}">selected</c:if>>DELIVERED</option>
                                <option value="CANCELLED" <c:if test="${order.status eq 'CANCELLED'}">selected</c:if>>CANCELLED</option>
                            </select>
                            <button type="submit" class="btn btn-warning btn-sm font-weight-bold">Update Status</button>
                        </form>
                    </div>
                </c:if>

                <h5 class="fw-bold text-dark mb-3">Items in Order</h5>
                <div class="table-responsive">
                    <table class="table align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Product</th>
                                <th>Unit Price</th>
                                <th>Quantity</th>
                                <th class="text-end">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <img src="<c:out value="${item.productImageUrl}"/>" style="width: 50px; height: 50px; object-fit: contain;" class="rounded" alt="Item"
                                                 onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">
                                            <span class="fw-bold text-dark"><c:out value="${item.productName}"/></span>
                                        </div>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                    <td><c:out value="${item.quantity}"/></td>
                                    <td class="text-end fw-bold">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="d-flex justify-content-between align-items-center bg-light p-3 rounded mt-3">
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary"><i class="fa-solid fa-arrow-left me-2"></i>Back to Orders</a>
                    <div class="fs-4 fw-bold text-dark">Total Amount: <span class="text-primary">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span></div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
