package com.lostfound.member1_auth.controller;

import com.lostfound.member1_auth.service.UserService;
import com.lostfound.member1_auth.service.UserServiceImpl;
import com.lostfound.member5_admin.dao.AdminDAO;
import com.lostfound.member5_admin.dao.AdminDAOImpl;
import com.lostfound.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registration Controller Servlet.
 * Handles new account creation for students and faculty.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register", "/api/register"})
public class RegisterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    private final UserService userService = new UserServiceImpl();
    private final AdminDAO adminDAO = new AdminDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String identifier = request.getParameter("identifier");
        if (identifier == null) {
            identifier = request.getParameter("email");
        }
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        User user = new User();
        user.setName(name);
        user.setEmail(identifier);
        user.setPassword(password);

        try {
            User registeredUser = userService.register(user, confirmPassword);
            adminDAO.logAudit(registeredUser.getUserId(), "REGISTER", request.getRemoteAddr());

            request.getSession(true).setAttribute("flashSuccess", "Registration successful! You can now log in with your credentials.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                request.setAttribute("error", e.getMessage());
            } else {
                LOGGER.log(Level.SEVERE, "Registration failed due to a server error.", e);
                request.setAttribute("error", "Unable to register right now. Please try again.");
            }
            user.setPassword(null);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
