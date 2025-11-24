package com.gardencommunity.servlet;

import com.gardencommunity.model.User;
import com.gardencommunity.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.gardencommunity.util.ActivityLogger;


import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final UserService userService = LoginServlet.getUserService();

    private static final ActivityLogger logger = ActivityLogger.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name  = req.getParameter("name");
        String email = req.getParameter("email");

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            req.setAttribute("error", "Name and email are required");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        User existing = userService.findByEmail(email);
        if (existing != null) {
            req.setAttribute("error", "User already exists. Try logging in.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        User newUser = userService.registerGardener(name, email);

        logger.log("New gardener registered: " + newUser.getEmail() + " (id=" + newUser.getId() + ")");

        HttpSession session = req.getSession(true);
        session.setAttribute("user", newUser);

        resp.sendRedirect(req.getContextPath() + "/gardener.jsp");

    }
}
