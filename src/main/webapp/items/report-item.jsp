<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Report Campus Property - Campus Lost & Found Portal" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />
<jsp:include page="/includes/alerts.jsp" />

<main class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-9 col-xl-8">

            <!-- Mode Switcher Pill Control -->
            <div class="card border-0 shadow-sm rounded-4 mb-4 bg-white p-2">
                <div class="d-flex align-items-center justify-content-between p-1 bg-light rounded-pill border">
                    <button type="button"
                            id="btnModeLost"
                            class="btn flex-fill rounded-pill py-2 fw-bold text-center transition-all ${itemType ne 'FOUND' ? 'btn-danger shadow-sm text-white' : 'btn-light text-secondary'}"
                            onclick="switchReportMode('LOST')">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>I Lost Something (Report Lost)
                    </button>
                    <button type="button"
                            id="btnModeFound"
                            class="btn flex-fill rounded-pill py-2 fw-bold text-center transition-all ${itemType eq 'FOUND' ? 'btn-success shadow-sm text-white' : 'btn-light text-secondary'}"
                            onclick="switchReportMode('FOUND')">
                        <i class="bi bi-box2-heart-fill me-2"></i>I Found Something (Report Found)
                    </button>
                </div>
            </div>

            <!-- Main Form Card -->
            <div class="card shadow border-0 rounded-4 overflow-hidden" id="reportMainCard">
                <!-- Dynamic Card Header -->
                <div class="card-header py-3 px-4 border-bottom d-flex align-items-center justify-content-between transition-all ${itemType eq 'FOUND' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}" id="reportCardHeader">
                    <div>
                        <h5 class="mb-0 fw-bold" id="headerTitle">
                            <i class="bi ${itemType eq 'FOUND' ? 'bi-box2-heart-fill text-success' : 'bi-exclamation-triangle-fill text-danger'} me-2" id="headerIcon"></i>
                            <span id="headerText">${itemType eq 'FOUND' ? 'Report a Found Item' : 'Report a Lost Item'}</span>
                        </h5>
                        <small class="text-muted" id="headerSubtext">
                            ${itemType eq 'FOUND' ? 'Help return found belongings safely to fellow students or faculty.' : 'Broadcast missing property to the campus community for recovery.'}
                        </small>
                    </div>
                    <span class="badge ${itemType eq 'FOUND' ? 'bg-success' : 'bg-danger'} text-white px-3 py-2 rounded-pill fs-7 fw-bold" id="typeBadge">
                        ${itemType eq 'FOUND' ? 'FOUND PROPERTY' : 'LOST PROPERTY'}
                    </span>
                </div>

                <div class="card-body p-4 p-md-5">
                    <form id="itemReportForm" action="${pageContext.request.contextPath}/items" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="create">
                        <input type="hidden" id="itemTypeInput" name="itemType" value="${itemType eq 'FOUND' ? 'FOUND' : 'LOST'}">

                        <div class="row g-4">
                            <!-- Title -->
                            <div class="col-md-12">
                                <label for="title" class="form-label fw-semibold text-dark">
                                    Item Title <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light text-muted border-end-0">
                                        <i class="bi bi-tag-fill"></i>
                                    </span>
                                    <input type="text" class="form-control border-start-0 ps-0" id="title" name="title"
                                           placeholder="e.g. Black Leather Tommy Hilfiger Wallet with College ID" required minlength="3">
                                </div>
                                <div class="form-text small text-muted">Provide a concise, identifiable headline for your listing.</div>
                            </div>

                            <!-- Category -->
                            <div class="col-md-6">
                                <label for="categoryId" class="form-label fw-semibold text-dark">
                                    Category <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light text-muted border-end-0">
                                        <i class="bi bi-grid-fill"></i>
                                    </span>
                                    <select class="form-select border-start-0 ps-0" id="categoryId" name="categoryId" required>
                                        <option value="">-- Choose Category --</option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.categoryId}">${cat.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <!-- Date (Dynamic Label) -->
                            <div class="col-md-6">
                                <label for="itemDate" class="form-label fw-semibold text-dark">
                                    <span id="dateLabel">${itemType eq 'FOUND' ? 'Date Found' : 'Date Lost'}</span> <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light text-muted border-end-0">
                                        <i class="bi bi-calendar-event"></i>
                                    </span>
                                    <input type="date" class="form-control border-start-0 ps-0" id="itemDate" name="itemDate" required>
                                </div>
                            </div>

                            <!-- Campus Location -->
                            <div class="col-md-12">
                                <label for="location" class="form-label fw-semibold text-dark">
                                    <span id="locationLabel">${itemType eq 'FOUND' ? 'Where Was It Found on Campus?' : 'Campus Location Where Lost'}</span> <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light text-muted border-end-0">
                                        <i class="bi bi-geo-alt-fill text-danger"></i>
                                    </span>
                                    <input type="text" class="form-control border-start-0 ps-0" id="location" name="location"
                                           placeholder="e.g. Central Library 2nd Floor reading hall, near CS block" required>
                                </div>
                                <div class="form-text small text-muted">Specify the department, floor, classroom, cafeteria, or landmark.</div>
                            </div>

                            <!-- GPS Coordinates (Optional Auto-Detect) -->
                            <div class="col-md-5">
                                <label for="latitudeInput" class="form-label fw-semibold text-dark small text-muted">Latitude (Optional)</label>
                                <input type="number" step="any" class="form-control" id="latitudeInput" name="latitude" placeholder="28.613920">
                            </div>

                            <div class="col-md-5">
                                <label for="longitudeInput" class="form-label fw-semibold text-dark small text-muted">Longitude (Optional)</label>
                                <input type="number" step="any" class="form-control" id="longitudeInput" name="longitude" placeholder="77.209020">
                            </div>

                            <div class="col-md-2 d-flex align-items-end">
                                <button type="button" id="btnGetCoordinates" class="btn btn-outline-primary w-100" title="Auto-detect current GPS via Browser">
                                    <i class="bi bi-geo-alt"></i> GPS
                                </button>
                            </div>

                            <!-- Description (Dynamic Placeholder & Label) -->
                            <div class="col-md-12">
                                <label for="description" class="form-label fw-semibold text-dark">
                                    <span id="descriptionLabel">${itemType eq 'FOUND' ? 'Found Item Details &amp; Safe Custody' : 'Detailed Item Description'}</span> <span class="text-danger">*</span>
                                </label>
                                <textarea class="form-control" id="description" name="description" rows="4"
                                          placeholder="${itemType eq 'FOUND' ? 'Mention where the item is currently kept (e.g. handed to Security desk, Dean office, or kept with finder).' : 'Describe unique identifying marks, contents, branding, serial numbers, scratches, etc.'}"
                                          required minlength="5"></textarea>
                                <div class="form-text small text-muted" id="descriptionHelp">
                                    ${itemType eq 'FOUND' ? 'State who holds custody so the rightful owner knows where to claim.' : 'Be thorough so the matching algorithm can find high-confidence matches.'}
                                </div>
                            </div>

                            <!-- Photo Upload (Enhanced preview) -->
                            <div class="col-md-12">
                                <label for="imageFileInput" class="form-label fw-semibold text-dark">
                                    <span id="photoLabel">${itemType eq 'FOUND' ? 'Upload Photo of Found Item (Max 5MB)' : 'Upload Reference Photo (Optional, Max 5MB)'}</span>
                                </label>
                                <div class="p-3 border rounded-3 bg-light text-center" id="dropArea">
                                    <input type="file" class="form-control mb-2" id="imageFileInput" name="imageFile"
                                           accept=".jpg,.jpeg,.jfif,.png,.webp,image/jpeg,image/jfif,image/png,image/webp">
                                    <div class="small text-muted">
                                        <i class="bi bi-file-earmark-image me-1"></i> Accepted formats: JPG, JPEG, JFIF, PNG, WEBP (Max 5MB).
                                    </div>
                                    <div id="imagePreviewContainer" class="mt-3 d-none">
                                        <div class="position-relative d-inline-block">
                                            <img id="imagePreview" src="#" alt="Preview" class="img-thumbnail rounded-3 shadow-sm" style="max-height: 200px; max-width: 100%; object-fit: contain;">
                                            <button type="button" class="btn btn-sm btn-danger position-absolute top-0 end-0 m-1 rounded-circle p-1 lh-1" onclick="clearSelectedImage()" title="Remove image">
                                                <i class="bi bi-x fs-6"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- AI Item Analysis Box -->
                            <div class="col-md-12">
                                <div class="p-3 border rounded-3 bg-light shadow-sm">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <div>
                                            <h6 class="fw-bold mb-1 text-primary">
                                                <i class="bi bi-stars text-primary me-1"></i> Smart AI Analysis
                                            </h6>
                                            <small class="text-muted">Analyze your report details with local NLP heuristic to improve matching confidence.</small>
                                        </div>
                                        <button type="button" id="btnAIAnalyze" class="btn btn-primary btn-sm px-3 rounded-pill">
                                            <i class="bi bi-stars me-1"></i> Analyze with AI
                                        </button>
                                    </div>
                                    <div id="aiAnalysisResult" class="d-none mt-3">
                                        <div class="alert alert-primary mb-0 rounded-3">
                                            <div class="fw-bold mb-1"><i class="bi bi-robot me-1"></i> AI Analysis Summary:</div>
                                            <p class="mb-1 small" id="aiDetails"></p>
                                            <div class="d-flex gap-2 align-items-center mt-2 pt-2 border-top">
                                                <span class="badge bg-success" id="aiReadiness">AI OPTIMIZED</span>
                                                <span class="small text-muted" id="aiKeywords">Ready for algorithmic matching</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Submission Footer -->
                            <div class="col-md-12 d-flex justify-content-between align-items-center pt-3 border-top">
                                <a href="${pageContext.request.contextPath}/items?action=gallery" class="btn btn-outline-secondary px-4">
                                    <i class="bi bi-arrow-left me-1"></i> Cancel
                                </a>
                                <button type="submit" class="btn ${itemType eq 'FOUND' ? 'btn-success' : 'btn-danger'} px-4 py-2 fw-semibold shadow" id="btnSubmitReport">
                                    <i class="bi ${itemType eq 'FOUND' ? 'bi-send-check' : 'bi-send'} me-1" id="submitIcon"></i>
                                    <span id="submitBtnText">${itemType eq 'FOUND' ? 'Submit Found Report' : 'Submit Lost Report'}</span>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>
</main>

<script>
    // Set today's date by default if empty
    document.addEventListener("DOMContentLoaded", function () {
        const dateInput = document.getElementById("itemDate");
        if (dateInput && !dateInput.value) {
            dateInput.value = new Date().toISOString().split('T')[0];
        }
    });

    // Dynamic Switcher Function between LOST and FOUND
    function switchReportMode(type) {
        const itemTypeInput = document.getElementById("itemTypeInput");
        const btnLost = document.getElementById("btnModeLost");
        const btnFound = document.getElementById("btnModeFound");
        const header = document.getElementById("reportCardHeader");
        const headerTitle = document.getElementById("headerText");
        const headerIcon = document.getElementById("headerIcon");
        const headerSub = document.getElementById("headerSubtext");
        const typeBadge = document.getElementById("typeBadge");
        const dateLabel = document.getElementById("dateLabel");
        const locationLabel = document.getElementById("locationLabel");
        const descriptionLabel = document.getElementById("descriptionLabel");
        const description = document.getElementById("description");
        const descriptionHelp = document.getElementById("descriptionHelp");
        const photoLabel = document.getElementById("photoLabel");
        const submitBtn = document.getElementById("btnSubmitReport");
        const submitIcon = document.getElementById("submitIcon");
        const submitBtnText = document.getElementById("submitBtnText");

        if (type === 'FOUND') {
            itemTypeInput.value = 'FOUND';
            btnLost.className = 'btn flex-fill rounded-pill py-2 fw-bold text-center transition-all btn-light text-secondary';
            btnFound.className = 'btn flex-fill rounded-pill py-2 fw-bold text-center transition-all btn-success shadow-sm text-white';

            header.className = 'card-header py-3 px-4 border-bottom d-flex align-items-center justify-content-between transition-all bg-success-subtle text-success';
            headerIcon.className = 'bi bi-box2-heart-fill text-success me-2';
            headerTitle.textContent = 'Report a Found Item';
            headerSub.textContent = 'Help return found belongings safely to fellow students or faculty.';

            typeBadge.className = 'badge bg-success text-white px-3 py-2 rounded-pill fs-7 fw-bold';
            typeBadge.textContent = 'FOUND PROPERTY';

            dateLabel.textContent = 'Date Found';
            locationLabel.textContent = 'Where Was It Found on Campus?';
            descriptionLabel.innerHTML = 'Found Item Details &amp; Safe Custody <span class="text-danger">*</span>';
            description.placeholder = 'Mention where the item is currently kept (e.g. handed to Security desk, Dean office, or kept with finder).';
            descriptionHelp.textContent = 'State who holds custody so the rightful owner knows where to claim.';

            photoLabel.textContent = 'Upload Photo of Found Item (Max 5MB - Recommended for claimant verification)';

            submitBtn.className = 'btn btn-success px-4 py-2 fw-semibold shadow';
            submitIcon.className = 'bi bi-send-check me-1';
            submitBtnText.textContent = 'Submit Found Report';
        } else {
            itemTypeInput.value = 'LOST';
            btnFound.className = 'btn flex-fill rounded-pill py-2 fw-bold text-center transition-all btn-light text-secondary';
            btnLost.className = 'btn flex-fill rounded-pill py-2 fw-bold text-center transition-all btn-danger shadow-sm text-white';

            header.className = 'card-header py-3 px-4 border-bottom d-flex align-items-center justify-content-between transition-all bg-danger-subtle text-danger';
            headerIcon.className = 'bi bi-exclamation-triangle-fill text-danger me-2';
            headerTitle.textContent = 'Report a Lost Item';
            headerSub.textContent = 'Broadcast missing property to the campus community for recovery.';

            typeBadge.className = 'badge bg-danger text-white px-3 py-2 rounded-pill fs-7 fw-bold';
            typeBadge.textContent = 'LOST PROPERTY';

            dateLabel.textContent = 'Date Lost';
            locationLabel.textContent = 'Campus Location Where Lost';
            descriptionLabel.innerHTML = 'Detailed Item Description <span class="text-danger">*</span>';
            description.placeholder = 'Describe unique identifying marks, contents, branding, serial numbers, scratches, etc.';
            descriptionHelp.textContent = 'Be thorough so the matching algorithm can find high-confidence matches.';

            photoLabel.textContent = 'Upload Reference Photo (Optional, Max 5MB)';

            submitBtn.className = 'btn btn-danger px-4 py-2 fw-semibold shadow';
            submitIcon.className = 'bi bi-send me-1';
            submitBtnText.textContent = 'Submit Lost Report';
        }
    }

    function clearSelectedImage() {
        const fileInput = document.getElementById("imageFileInput");
        const previewContainer = document.getElementById("imagePreviewContainer");
        const previewImg = document.getElementById("imagePreview");
        if (fileInput) fileInput.value = "";
        if (previewImg) previewImg.src = "#";
        if (previewContainer) previewContainer.classList.add("d-none");
    }

    // AI Analysis handler
    const aiBtn = document.getElementById("btnAIAnalyze");
    if (aiBtn) {
        aiBtn.addEventListener("click", async function () {
            const title = document.getElementById("title").value.trim();
            const description = document.getElementById("description").value.trim();
            const location = document.getElementById("location").value.trim();

            if (!title || !description || !location) {
                alert("Please fill in Item Title, Location, and Description before running AI analysis.");
                return;
            }

            const button = this;
            button.disabled = true;
            button.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Analyzing...';

            try {
                const formData = new URLSearchParams();
                formData.append("title", title);
                formData.append("description", description);
                formData.append("location", location);

                const response = await fetch("${pageContext.request.contextPath}/api/ai-analysis", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: formData
                });

                const data = await response.json();
                if (!response.ok) throw new Error(data.error || "Analysis failed");

                document.getElementById("aiDetails").textContent = data.analysis || "Details successfully processed and indexed for high-accuracy scoring.";
                document.getElementById("aiKeywords").textContent = "Extracted Keywords: " + (data.keywords ? data.keywords.join(", ") : "Identified successfully");
                document.getElementById("aiReadiness").textContent = "AI READY (100%)";
                document.getElementById("aiAnalysisResult").classList.remove("d-none");
            } catch (err) {
                alert("AI Analysis: " + err.message);
            } finally {
                button.disabled = false;
                button.innerHTML = '<i class="bi bi-stars me-1"></i> Analyze with AI';
            }
        });
    }
</script>

<jsp:include page="/includes/footer.jsp" />
