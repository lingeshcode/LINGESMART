<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row justify-content-center py-5 text-center">
    <div class="col-md-6">
        <div class="card border-0 shadow-sm p-5 rounded-3">
            <h1 class="display-1 text-danger fw-bold">500</h1>
            <h4 class="fw-bold text-dark mb-3">Internal Server Exception</h4>
            <p class="text-muted mb-4">An unexpected error occurred while processing your request. Our system logged this issue securely.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary rounded-pill px-4">Return to Marketplace Home</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
