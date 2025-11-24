package com.gardencommunity.servlet;

import com.gardencommunity.dao.DataAccessException;
import com.gardencommunity.dao.ProjectDAO;
import com.gardencommunity.dao.ProjectDAOImpl;
import com.gardencommunity.model.Project;
import com.gardencommunity.model.User;
import com.gardencommunity.util.ActivityLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {

    private final ProjectDAO projectDAO = new ProjectDAOImpl();
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
            List<Project> projects = projectDAO.findByOwner(user.getId());
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/projects.jsp").forward(req, resp);
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

        String action = req.getParameter("action");
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        try {
            if ("updateProgress".equals(action)) {
                int projectId = Integer.parseInt(req.getParameter("projectId"));
                int progress = Integer.parseInt(req.getParameter("progress"));
                Project existing = projectDAO.findById(projectId);
                if (existing != null && existing.getOwnerId() == user.getId()) {
                    existing.setProgress(progress);
                    projectDAO.save(existing);
                    logger.log("User " + user.getId() + " updated project #" + projectId +
                            " progress to " + progress + "%");
                }
            } else { // create new project
                if (title != null && !title.isBlank()) {
                    Project p = new Project(0, user.getId(), title,
                            description, 0, null);
                    projectDAO.save(p);
                    logger.log("User " + user.getId() + " created project: \"" + title + "\"");
                }
            }
            resp.sendRedirect(req.getContextPath() + "/projects");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
