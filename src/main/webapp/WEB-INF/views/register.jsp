<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row justify-content-center py-4">
    <div class="col-md-5">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-body p-4">
                <div class="text-center mb-4">
                    <h3 class="fw-bold text-primary"><i class="fa-solid fa-user-plus me-2"></i>Create an Account</h3>
                    <p class="text-muted small">Join LingeshMart as a Buyer or Seller</p>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger small"><c:out value="${errorMessage}"/></div>
                </c:if>

                <form action="${pageContext.request.contextPath}/auth/register" method="post">
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Full Name</label>
                        <input type="text" name="name" class="form-control" placeholder="John Doe" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Email Address</label>
                        <input type="email" name="email" class="form-control" placeholder="name@example.com" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Password (min 6 characters)</label>
                        <input type="password" name="password" class="form-control" placeholder="••••••••" minlength="6" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Account Role</label>
                        <select name="role" class="form-select">
                            <option value="BUYER">Buyer (Shop products)</option>
                            <option value="SELLER">Seller (List & sell products)</option>
                        </select>
                        <div class="form-text text-muted small">Admin accounts are seeded by system administrator.</div>
                    </div>

                    <button type="submit" class="btn btn-flipkart w-100 py-2 fs-6 shadow-sm mb-3">Register Now</button>
                </form>

                <hr class="my-3">
                <div class="text-center small text-muted">
                    Already have an account? <a href="${pageContext.request.contextPath}/auth/login" class="fw-bold text-primary text-decoration-none">Login here</a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
