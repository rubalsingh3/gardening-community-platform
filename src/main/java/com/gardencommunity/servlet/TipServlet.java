package com.gardencommunity.servlet;

import com.gardencommunity.dao.DataAccessException;
import com.gardencommunity.dao.TipDAO;
import com.gardencommunity.dao.TipDAOImpl;
import com.gardencommunity.model.Tip;
import com.gardencommunity.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.gardencommunity.util.ActivityLogger;

;


import java.io.IOException;
import java.util.List;

@WebServlet("/tips")
public class TipServlet extends HttpServlet {

    private final TipDAO tipDAO = new TipDAOImpl();
    private final ActivityLogger logger = ActivityLogger.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getRole() != User.Role.GARDENER) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        try {
            List<Tip> myTips = tipDAO.findByAuthor(user.getId());
            req.setAttribute("myTips", myTips);
            req.getRequestDispatcher("/gardener_tips.jsp").forward(req, resp);
        } catch (DataAccessException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getRole() != User.Role.GARDENER) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");

        if (title == null || title.isBlank()) {
            req.setAttribute("error", "Title is required");
            doGet(req, resp); // redisplay list with error
            return;
        }

        Tip tip = new Tip(0, user.getId(), title, description,
                Tip.Status.PENDING, null);

        try {
            tipDAO.save(tip);
            logger.log("User " + user.getId() + " created tip: \"" + title + "\"");
            resp.sendRedirect(req.getContextPath() + "/tips");
        } catch (DataAccessException e) {
            throw new ServletException(e);
        }

    }
}
