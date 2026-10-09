<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row g-4">
    <!-- Product Image & CTA -->
    <div class="col-md-5">
        <div class="card border-0 shadow-sm p-4 text-center bg-white rounded-3">
            <img src="<c:out value="${product.imageUrl}"/>" class="img-fluid mx-auto mb-4" style="max-height: 350px; object-fit: contain;" alt="<c:out value="${product.name}"/>"
                 onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600'">

            <div class="d-flex gap-2">
                <form action="${pageContext.request.contextPath}/cart/add" method="post" class="flex-grow-1">
                    <input type="hidden" name="productId" value="${product.id}">
                    <input type="hidden" name="quantity" value="1">
                    <button type="submit" class="btn btn-cart btn-lg w-100 py-3 text-uppercase shadow-sm" <c:if test="${product.stockQty <= 0}">disabled</c:if>>
                        <i class="fa-solid fa-cart-shopping me-2"></i> Add to Cart
                    </button>
                </form>

                <c:choose>
                    <c:when test="${inWishlist}">
                        <form action="${pageContext.request.contextPath}/wishlist/remove" method="post">
                            <input type="hidden" name="productId" value="${product.id}">
                            <button type="submit" class="btn btn-outline-danger btn-lg py-3 px-4 shadow-sm" title="Remove from Wishlist">
                                <i class="fa-solid fa-heart fs-4"></i>
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="${pageContext.request.contextPath}/wishlist/add" method="post">
                            <input type="hidden" name="productId" value="${product.id}">
                            <button type="submit" class="btn btn-outline-secondary btn-lg py-3 px-4 shadow-sm" title="Add to Wishlist">
                                <i class="fa-regular fa-heart fs-4"></i>
                            </button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Product Info Details -->
    <div class="col-md-7">
        <div class="card border-0 shadow-sm p-4 rounded-3 bg-white">
            <span class="badge bg-secondary mb-2 w-auto me-auto px-3 py-2"><c:out value="${product.category}"/></span>
            <h3 class="fw-bold text-dark mb-2"><c:out value="${product.name}"/></h3>
            <div class="text-muted small mb-3"><i class="fa-solid fa-shop me-1 text-primary"></i>Sold by: <strong><c:out value="${product.sellerName}"/></strong></div>

            <div class="d-flex align-items-center gap-3 mb-4">
                <div class="badge bg-success px-3 py-2 fs-6">
                    <fmt:formatNumber value="${avgRating}" pattern="0.0"/> <i class="fa-solid fa-star ms-1"></i>
                </div>
                <span class="text-muted small">(<c:out value="${fn:length(reviews)}"/> Customer Reviews)</span>
            </div>

            <div class="mb-4">
                <span class="display-6 fw-bold text-dark me-3">₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
                <c:choose>
                    <c:when test="${product.stockQty > 0}">
                        <span class="badge bg-success fs-6"><i class="fa-solid fa-check me-1"></i>In Stock (<c:out value="${product.stockQty}"/> left)</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-danger fs-6">Currently Out of Stock</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <h6 class="fw-bold text-uppercase text-muted mb-2">Product Overview</h6>
            <p class="text-secondary leading-relaxed mb-4"><c:out value="${product.description}"/></p>

            <hr>

            <!-- Customer Reviews Section (F8 Requirement) -->
            <h5 class="fw-bold text-dark mb-3"><i class="fa-solid fa-star text-warning me-2"></i>Product Reviews & Ratings</h5>

            <!-- Submit Review Form -->
            <c:if test="${not empty sessionScope.user}">
                <div class="card bg-light border-0 mb-4">
                    <div class="card-body">
                        <h6 class="fw-bold mb-2">Leave a Rating & Review</h6>
                        <p class="text-muted small mb-3">Share your experience with this item (available for buyers of completed orders).</p>
                        <form action="${pageContext.request.contextPath}/reviews/add" method="post">
                            <input type="hidden" name="productId" value="${product.id}">
                            <div class="row g-2 mb-2">
                                <div class="col-md-3">
                                    <select name="rating" class="form-select form-select-sm" required>
                                        <option value="5">5 Stars (Excellent)</option>
                                        <option value="4">4 Stars (Good)</option>
                                        <option value="3">3 Stars (Average)</option>
                                        <option value="2">2 Stars (Poor)</option>
                                        <option value="1">1 Star (Terrible)</option>
                                    </select>
                                </div>
                                <div class="col-md-9">
                                    <input type="text" name="comment" class="form-control form-control-sm" placeholder="Write your review comments here..." required>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm rounded-pill px-4">Submit Review</button>
                        </form>
                    </div>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty reviews}">
                    <p class="text-muted small">No reviews yet for this product. Be the first to buy and review!</p>
                </c:when>
                <c:otherwise>
                    <div class="d-flex flex-column gap-3">
                        <c:forEach var="rev" items="${reviews}">
                            <div class="border-bottom pb-2">
                                <div class="d-flex align-items-center gap-2 mb-1">
                                    <span class="badge bg-success"><c:out value="${rev.rating}"/> <i class="fa-solid fa-star"></i></span>
                                    <strong class="small text-dark"><c:out value="${rev.userName}"/></strong>
                                    <span class="text-muted small ms-auto"><fmt:formatDate value="${rev.createdAt}" pattern="MMM dd, yyyy"/></span>
                                </div>
                                <p class="text-secondary small mb-0"><c:out value="${rev.comment}"/></p>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
