<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<h3 class="fw-bold text-dark mb-4"><i class="fa-solid fa-user-shield text-warning me-2"></i>System Admin Management Console</h3>

<!-- Admin Navigation Tabs -->
<ul class="nav nav-pills mb-4" id="adminTabs" role="tablist">
    <li class="nav-item">
        <button class="nav-link active font-weight-bold" data-bs-toggle="pill" data-bs-target="#tabListings">Product Listing Moderation (<c:out value="${fn:length(products)}"/>)</button>
    </li>
    <li class="nav-item">
        <button class="nav-link font-weight-bold" data-bs-toggle="pill" data-bs-target="#tabUsers">Registered Users (<c:out value="${fn:length(users)}"/>)</button>
    </li>
    <li class="nav-item">
        <button class="nav-link font-weight-bold" data-bs-toggle="pill" data-bs-target="#tabOrders">All System Orders (<c:out value="${fn:length(orders)}"/>)</button>
    </li>
</ul>

<div class="tab-content">
    <!-- 1. Listing Moderation Tab (F7) -->
    <div class="tab-pane fade show active" id="tabListings">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Listing Title</th>
                                <th>Seller</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th class="text-end">Moderation Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <img src="<c:out value="${p.imageUrl}"/>" style="width: 40px; height: 40px; object-fit: contain;" class="rounded" alt="Img"
                                                 onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">
                                            <span class="fw-bold text-dark"><c:out value="${p.name}"/></span>
                                        </div>
                                    </td>
                                    <td><c:out value="${p.sellerName}"/></td>
                                    <td><span class="badge bg-secondary"><c:out value="${p.category}"/></span></td>
                                    <td>₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                    <td class="text-end">
                                        <form action="${pageContext.request.contextPath}/admin/listing/delete" method="post" class="d-inline" onsubmit="return confirm('Remove this listing from marketplace?')">
                                            <input type="hidden" name="productId" value="${p.id}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm"><i class="fa-solid fa-ban me-1"></i> Moderate / Remove</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- 2. Registered Users Tab -->
    <div class="tab-pane fade" id="tabUsers">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-body p-0">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>User ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Joined Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>#<c:out value="${u.id}"/></td>
                                <td class="fw-bold"><c:out value="${u.name}"/></td>
                                <td><c:out value="${u.email}"/></td>
                                <td>
                                    <span class="badge ${u.role eq 'ADMIN' ? 'bg-danger' : u.role eq 'SELLER' ? 'bg-primary' : 'bg-success'}">
                                        <c:out value="${u.role}"/>
                                    </span>
                                </td>
                                <td><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy, HH:mm"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- 3. All System Orders Tab -->
    <div class="tab-pane fade" id="tabOrders">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-body p-0">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>Order ID</th>
                            <th>Buyer</th>
                            <th>Status</th>
                            <th>Total Amount</th>
                            <th>Placed At</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td>#LM-<c:out value="${o.id}"/></td>
                                <td><c:out value="${o.buyerName}"/></td>
                                <td><span class="badge bg-info"><c:out value="${o.status}"/></span></td>
                                <td class="fw-bold">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                <td><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, HH:mm"/></td>
                                <td><a href="${pageContext.request.contextPath}/orders/detail?id=${o.id}" class="btn btn-outline-primary btn-sm">Inspect</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
