package com.lostfound.member4_claims.controller;

import com.lostfound.member1_auth.util.ValidationUtil;
import com.lostfound.member2_items.service.ItemService;
import com.lostfound.member2_items.service.ItemServiceImpl;
import com.lostfound.member4_claims.service.ClaimService;
import com.lostfound.member4_claims.service.ClaimServiceImpl;
import com.lostfound.member4_claims.util.QRCodeUtil;
import com.lostfound.member5_admin.dao.AdminDAO;
import com.lostfound.member5_admin.dao.AdminDAOImpl;
import com.lostfound.model.Claim;
import com.lostfound.model.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Verification Controller Servlet.
 * Handles OTP validation and QR code scanning verification.
 */
@WebServlet(name = "VerificationServlet", urlPatterns = {"/claims/verify", "/api/claims/verify"})
public class VerificationServlet extends HttpServlet {

    private final ClaimService claimService = new ClaimServiceImpl();
    private final ItemService itemService = new ItemServiceImpl();
    private final AdminDAO adminDAO = new AdminDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=Please+log+in+first");
            return;
        }

        Long claimId = ValidationUtil.parseLong(request.getParameter("claimId"));
        if (claimId == null) {
            response.sendRedirect(request.getContextPath() + "/claims?action=my-claims");
            return;
        }

        try {
            Claim claim = claimService.getClaimById(claimId);
            Item item = itemService.getItemById(claim.getItemId());
            if (!canAccessClaim(claim, item, session)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to view this claim's verification codes.");
                return;
            }
            if (!"APPROVED".equalsIgnoreCase(claim.getStatus()) && !"VERIFIED".equalsIgnoreCase(claim.getStatus())) {
                session.setAttribute("flashInfo", "OTP and QR verification become available after the item reporter or an administrator approves the claim.");
                response.sendRedirect(request.getContextPath() + "/claims?action=details&id=" + claimId);
                return;
            }
            showVerificationPage(request, response, claim, item);
        } catch (Exception e) {
            throw new ServletException("Unable to load claim verification.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=Please+log+in+first");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        Long claimId = ValidationUtil.parseLong(request.getParameter("claimId"));
        String verificationType = request.getParameter("verificationType");
        String code = request.getParameter("code");

        if (claimId == null) {
            response.sendRedirect(request.getContextPath() + "/claims?action=my-claims");
            return;
        }

        try {
            Claim claim = claimService.getClaimById(claimId);
            Item item = itemService.getItemById(claim.getItemId());
            if (!canAccessClaim(claim, item, session)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to verify this claim.");
                return;
            }
            if (!"APPROVED".equalsIgnoreCase(claim.getStatus()) && !"VERIFIED".equalsIgnoreCase(claim.getStatus())) {
                session.setAttribute("flashInfo", "OTP and QR verification become available after the item reporter or an administrator approves the claim.");
                response.sendRedirect(request.getContextPath() + "/claims?action=details&id=" + claimId);
                return;
            }

            boolean verified = false;
            if ("QR".equalsIgnoreCase(verificationType)) {
                verified = claimService.verifyQrToken(claimId, code);
                adminDAO.logAudit(userId, "VERIFY_QR: Claim #" + claimId, request.getRemoteAddr());
            } else {
                verified = claimService.verifyOtp(claimId, code);
                adminDAO.logAudit(userId, "VERIFY_OTP: Claim #" + claimId, request.getRemoteAddr());
            }

            if (verified) {
                request.getSession().setAttribute("flashSuccess", "Verification successful! You can now complete the item handover.");
                response.sendRedirect(request.getContextPath() + "/claims/handover?claimId=" + claimId);
            } else {
                request.setAttribute("error", "Verification failed. Please double-check the code.");
                showVerificationPage(request, response, claim, item);
            }

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            try {
                Claim claim = claimService.getClaimById(claimId);
                Item item = itemService.getItemById(claim.getItemId());
                if (!canAccessClaim(claim, item, session)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to verify this claim.");
                    return;
                }
                showVerificationPage(request, response, claim, item);
            } catch (Exception renderingException) {
                throw new ServletException("Unable to display claim verification.", renderingException);
            }
        }
    }

    private boolean canAccessClaim(Claim claim, Item item, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        return item != null && ("ADMIN".equalsIgnoreCase(role)
                || claim.getClaimantId().equals(userId)
                || item.getUserId().equals(userId));
    }

    private void showVerificationPage(HttpServletRequest request, HttpServletResponse response,
                                      Claim claim, Item item) throws ServletException, IOException {
        if (claim.getQrToken() != null && !claim.getQrToken().isEmpty()) {
            request.setAttribute("qrBase64", QRCodeUtil.generateQRCodeBase64(claim.getQrToken(), 200, 200));
        }
        request.setAttribute("claim", claim);
        request.setAttribute("item", item);
        request.getRequestDispatcher("/claims/otp-verification.jsp").forward(request, response);
    }
}
