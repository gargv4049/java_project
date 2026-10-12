<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Password Help - Campus Lost & Found Portal"/>
</jsp:include>
<jsp:include page="/includes/alerts.jsp"/>

<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow-sm border-0 rounded-4">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-3">
                        <i class="bi bi-shield-lock text-primary" style="font-size: 2.5rem;"></i>
                        <h4 class="fw-bold mt-2">Password help</h4>
                    </div>
                    <p class="text-muted">To change a password, sign in and use <strong>Change password</strong>. You must provide your current password.</p>
                    <div class="alert alert-info" role="alert">
                        If you cannot sign in, contact a campus portal administrator in person and verify your identity. The administrator can provision a temporary password using the secure administrator-assisted procedure documented in the project README. Password recovery is not available through email or a public reset form.
                    </div>
                    <div class="text-center">
                        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">Back to sign in</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/includes/footer.jsp"/>
