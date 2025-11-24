package com.gardencommunity.servlet;

import com.gardencommunity.dao.DataAccessException;
import com.gardencommunity.dao.TipDAO;
import com.gardencommunity.dao.TipDAOImpl;
import com.gardencommunity.model.Tip;
import com.gardencommunity.model.User;
import com.gardencommunity.util.ActivityLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin-tips")
public class AdminTipServlet extends HttpServlet {

    private final TipDAO tipDAO = new TipDAOImpl();
    private final ActivityLogger logger = ActivityLogger.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Only admin can moderate
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String action = req.getParameter("action");
        String tipIdStr = req.getParameter("tipId");

        if (action == null || tipIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/admin.jsp");
            return;
        }

        try {
            int tipId = Integer.parseInt(tipIdStr);
            Tip tip = tipDAO.findById(tipId);
            if (tip == null) {
                resp.sendRedirect(req.getContextPath() + "/admin.jsp");
                return;
            }

            if ("approve".equalsIgnoreCase(action)) {
                tip.setStatus(Tip.Status.APPROVED);
                tipDAO.save(tip);
                logger.log("Admin " + user.getId() + " approved tip #" + tipId);
            } else if ("reject".equalsIgnoreCase(action)) {
                tip.setStatus(Tip.Status.REJECTED);
                tipDAO.save(tip);
                logger.log("Admin " + user.getId() + " rejected tip #" + tipId);
            }

            resp.sendRedirect(req.getContextPath() + "/admin.jsp");
        } catch (NumberFormatException | DataAccessException e) {
            throw new ServletException(e);
        }
    }
}
