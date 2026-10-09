<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row justify-content-center py-4">
    <div class="col-md-5">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-body p-4">
                <div class="text-center mb-4">
                    <h3 class="fw-bold text-primary"><i class="fa-solid fa-lock me-2"></i>Login to LingeshMart</h3>
                    <p class="text-muted small">Enter your credentials to access your account</p>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger small"><c:out value="${errorMessage}"/></div>
                </c:if>

                <form action="${pageContext.request.contextPath}/auth/login" method="post">
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Email Address</label>
                        <input type="email" name="email" class="form-control" placeholder="name@example.com" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Password</label>
                        <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                    </div>

                    <button type="submit" class="btn btn-flipkart w-100 py-2 fs-6 shadow-sm mb-3">Login</button>
                </form>

                <hr class="my-3">
                <div class="text-center small text-muted">
                    Don't have an account? <a href="${pageContext.request.contextPath}/auth/register" class="fw-bold text-primary text-decoration-none">Sign Up / Become a Seller</a>
                </div>

                <div class="mt-4 p-3 bg-light rounded text-muted small">
                    <strong class="text-dark d-block mb-1"><i class="fa-solid fa-key me-1 text-warning"></i> Demo Accounts (Password: password123)</strong>
                    <div>Buyer: <code>buyer1@lingeshmart.com</code></div>
                    <div>Seller: <code>seller1@lingeshmart.com</code></div>
                    <div>Admin: <code>admin@lingeshmart.com</code></div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
