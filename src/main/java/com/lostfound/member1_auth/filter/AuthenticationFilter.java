package com.lostfound.member1_auth.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login", "/login.jsp", "/api/login",
            "/register", "/register.jsp", "/api/register",
            "/forgot-password.jsp",
            "/search", "/api/search", "/matches", "/api/matches"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (isPublic(request, path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please+sign+in+to+continue");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isPublic(HttpServletRequest request, String path) {
        if (PUBLIC_PATHS.contains(path)
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/uploads/items/")
                || path.startsWith("/error/")) {
            return true;
        }

        if ("/items".equals(path) && "GET".equalsIgnoreCase(request.getMethod())) {
            String action = request.getParameter("action");
            return action == null || action.isBlank()
                    || "gallery".equalsIgnoreCase(action)
                    || "view".equalsIgnoreCase(action);
        }
        return false;
    }
}
