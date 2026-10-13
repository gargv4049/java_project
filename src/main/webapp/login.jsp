<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Sign In - Campus Lost & Found Portal"/>
</jsp:include>
<jsp:include page="/includes/alerts.jsp"/>

<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-5">
            <div class="text-center mb-4">
                <a href="${pageContext.request.contextPath}/login.jsp" class="d-inline-flex align-items-center gap-2 text-decoration-none text-primary mb-2">
                    <i class="bi bi-box-seam-fill fs-2"></i>
                    <span class="fs-4 fw-bold">Campus Lost &amp; Found</span>
                </a>
                <p class="text-muted small">Centralized Campus Property &amp; Claim Recovery Network</p>
            </div>

            <div class="card shadow border-0 rounded-4 overflow-hidden">
                <div class="card-header bg-white border-bottom p-4 text-center">
                    <div class="rounded-circle bg-primary-subtle text-primary mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 54px; height: 54px;">
                        <i class="bi bi-shield-lock-fill fs-4"></i>
                    </div>
                    <h4 class="fw-bold text-dark mb-1">Welcome to Campus Portal</h4>
                    <p class="text-muted small mb-0">Sign in with your registered username/email and password</p>
                </div>
                <div class="card-body p-4 p-md-5">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label for="identifier" class="form-label fw-semibold">Username or email</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                                <input type="text" class="form-control" id="identifier" name="identifier"
                                       value="<c:out value='${identifier}'/>" placeholder="Enter username or email"
                                       required minlength="3" maxlength="150" autocomplete="username">
                            </div>
                        </div>
                        <div class="mb-4">
                            <label for="password" class="form-label fw-semibold">Password</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-lock"></i></span>
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="Enter password" required autocomplete="current-password">
                            </div>
                        </div>
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary py-2 fw-semibold">
                                <i class="bi bi-box-arrow-in-right me-1"></i> Sign In
                            </button>
                        </div>
                    </form>
                    <div class="text-center mt-3 small">
                        <a href="${pageContext.request.contextPath}/forgot-password.jsp" class="text-decoration-none">Forgot your password?</a>
                    </div>
                    <hr class="my-4">
                    <div class="text-center small text-muted">
                        Don't have an account?
                        <a href="${pageContext.request.contextPath}/register.jsp" class="text-primary fw-semibold text-decoration-none">Register here</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/includes/footer.jsp"/>
