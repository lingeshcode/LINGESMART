<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="row justify-content-center py-5 text-center">
    <div class="col-md-6">
        <div class="card border-0 shadow-sm p-5 rounded-3">
            <h1 class="display-1 text-primary fw-bold">404</h1>
            <h4 class="fw-bold text-dark mb-3">Page Not Found</h4>
            <p class="text-muted mb-4">The page or resource you were looking for could not be found or has been moved.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary rounded-pill px-4">Back to LingeshMart Home</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
