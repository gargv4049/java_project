<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Register - Campus Lost & Found Portal"/>
</jsp:include>
<jsp:include page="/includes/alerts.jsp"/>

<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-9 col-lg-7">
            <div class="text-center mb-4">
                <a href="${pageContext.request.contextPath}/login.jsp" class="d-inline-flex align-items-center gap-2 text-decoration-none text-primary mb-2">
                    <i class="bi bi-box-seam-fill fs-2"></i>
                    <span class="fs-4 fw-bold">Campus Lost &amp; Found</span>
                </a>
                <p class="text-muted small">Centralized Campus Property &amp; Claim Recovery Network</p>
            </div>
            <div class="card shadow border-0 rounded-4 overflow-hidden">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <div class="rounded-circle bg-success-subtle text-success mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 52px; height: 52px;">
                            <i class="bi bi-person-plus-fill fs-4"></i>
                        </div>
                        <h4 class="fw-bold">Create Campus Account</h4>
                        <p class="text-muted small">Choose a username or email and password to get started</p>
                    </div>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <div class="mb-3">
                            <label for="name" class="form-label">Full name <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="name" name="name"
                                   value="<c:out value='${user.name}'/>" required minlength="2" maxlength="100"
                                   autocomplete="name">
                        </div>
                        <div class="mb-3">
                            <label for="identifier" class="form-label">Username or email <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="identifier" name="identifier"
                                   value="<c:out value='${user.email}'/>" required minlength="3" maxlength="150"
                                   autocomplete="username" aria-describedby="identifierHelp">
                            <div id="identifierHelp" class="form-text">Email verification is not required. Use this identifier to sign in.</div>
                        </div>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="password" class="form-label">Password <span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="password" name="password"
                                       required minlength="8" autocomplete="new-password">
                                <div class="form-text small">At least 8 characters, including a letter and a number or symbol.</div>
                            </div>
                            <div class="col-md-6">
                                <label for="confirmPassword" class="form-label">Confirm password <span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                                       required minlength="8" autocomplete="new-password">
                            </div>
                        </div>
                        <div class="d-grid gap-2 mt-4">
                            <button type="submit" class="btn btn-primary py-2 fw-semibold">
                                <i class="bi bi-person-check-fill me-1"></i> Register
                            </button>
                        </div>
                    </form>
                    <div class="text-center mt-3 small text-muted">
                        Already have an account?
                        <a href="${pageContext.request.contextPath}/login.jsp" class="text-primary fw-semibold text-decoration-none">Sign in here</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/includes/footer.jsp"/>
