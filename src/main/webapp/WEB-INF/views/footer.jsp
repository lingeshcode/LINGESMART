</div> <!-- End Container -->

<footer class="bg-dark text-white py-4 mt-5">
    <div class="container">
        <div class="row">
            <div class="col-md-4 mb-3">
                <h5 class="fw-bold text-white mb-3"><i class="fa-solid fa-cart-shopping text-primary me-2"></i>LingeshMart</h5>
                <p class="text-muted small">India's Leading Multi-Seller E-Commerce Marketplace. Built for Anna University R2025 Standard with Java Servlets, JDBC, and H2 DB.</p>
            </div>
            <div class="col-md-2 mb-3">
                <h6 class="fw-bold text-uppercase text-light mb-3">Shop</h6>
                <ul class="list-unstyled text-muted small">
                    <li><a href="${pageContext.request.contextPath}/home?category=Electronics" class="text-muted text-decoration-none">Electronics</a></li>
                    <li><a href="${pageContext.request.contextPath}/home?category=Fashion" class="text-muted text-decoration-none">Fashion</a></li>
                    <li><a href="${pageContext.request.contextPath}/home?category=Home" class="text-muted text-decoration-none">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/home?category=Books" class="text-muted text-decoration-none">Books</a></li>
                </ul>
            </div>
            <div class="col-md-3 mb-3">
                <h6 class="fw-bold text-uppercase text-light mb-3">Account</h6>
                <ul class="list-unstyled text-muted small">
                    <li><a href="${pageContext.request.contextPath}/auth/login" class="text-muted text-decoration-none">Buyer Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/auth/register" class="text-muted text-decoration-none">Seller Signup</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders" class="text-muted text-decoration-none">Order Tracking</a></li>
                </ul>
            </div>
            <div class="col-md-3 mb-3">
                <h6 class="fw-bold text-uppercase text-light mb-3">Security & Compliance</h6>
                <p class="text-muted small"><i class="fa-solid fa-lock text-success me-2"></i>100% PreparedStatements<br><i class="fa-solid fa-key text-warning me-2"></i>BCrypt Password Hashing<br><i class="fa-solid fa-shield-halved text-info me-2"></i>Session Security & MDC Logging</p>
            </div>
        </div>
        <hr class="border-secondary">
        <div class="d-flex justify-content-between align-items-center text-muted small">
            <div>&copy; 2026 LingeshMart. All rights reserved.</div>
            <div>Built by Lingesh · Anna University R2025</div>
        </div>
    </div>
</footer>

<!-- Include AI Chatbot Drawer Widget (Phase 3 Spec) -->
<jsp:include page="/WEB-INF/views/chatbot-widget.jsp" />

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/bootstrap.bundle.min.js"></script>
</body>
</html>
