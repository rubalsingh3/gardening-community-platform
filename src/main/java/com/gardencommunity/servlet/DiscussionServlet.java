package com.gardencommunity.servlet;

import com.gardencommunity.dao.DataAccessException;
import com.gardencommunity.dao.DiscussionDAO;
import com.gardencommunity.dao.DiscussionDAOImpl;
import com.gardencommunity.model.Discussion;
import com.gardencommunity.model.User;
import com.gardencommunity.util.ActivityLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/discussions")
public class DiscussionServlet extends HttpServlet {

    private final DiscussionDAO discussionDAO = new DiscussionDAOImpl();
    private final ActivityLogger logger = ActivityLogger.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // any logged-in user can view discussions; but creating is gardeners
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        try {
            List<Discussion> all = discussionDAO.findAll();
            req.setAttribute("discussions", all);
            req.getRequestDispatcher("/discussions.jsp").forward(req, resp);
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
        String content = req.getParameter("content");

        if (title == null || title.isBlank()) {
            req.setAttribute("error", "Title is required");
            doGet(req, resp);
            return;
        }

        Discussion d = new Discussion(0, user.getId(), title, content, null);

        try {
            discussionDAO.save(d);
            logger.log("User " + user.getId() + " created discussion: \"" + title + "\"");
            resp.sendRedirect(req.getContextPath() + "/discussions");
        } catch (DataAccessException e) {
            throw new ServletException(e);
        }
    }
}
