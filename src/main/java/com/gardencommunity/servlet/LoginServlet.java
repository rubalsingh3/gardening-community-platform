package com.gardencommunity.servlet;

import com.gardencommunity.model.User;
import com.gardencommunity.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.gardencommunity.util.ActivityLogger;


import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final ActivityLogger logger = ActivityLogger.getInstance();

    private static final UserService userService = new UserService();

    public static UserService getUserService() {
        return userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        if (email == null || email.isBlank()) {
            req.setAttribute("error", "Email is required");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            req.setAttribute("error", "User not found. Please register first.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        logger.log("User logged in: " + user.getEmail() + " (" + user.getRole() + ")");

        if (user.getRole() == User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/admin.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/gardener.jsp");
        }

    }
}
