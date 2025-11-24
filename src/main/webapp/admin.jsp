<%@ page import="com.gardencommunity.model.User" %>
<%@ page import="com.gardencommunity.service.UserService" %>
<%@ page import="com.gardencommunity.servlet.LoginServlet" %>
<%@ page import="com.gardencommunity.dao.TipDAO" %>
<%@ page import="com.gardencommunity.dao.TipDAOImpl" %>
<%@ page import="com.gardencommunity.model.Tip" %>
<%@ page import="com.gardencommunity.util.ActivityLogger" %>
<%@ page import="java.util.List" %>


<html>
<head>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 30px;
        line-height: 1.6;
        background: #f5f5f5;
    }
    h2, h3 {
        margin-bottom: 10px;
        color: #333;
    }
    .container {
        background: white;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        width: 80%;
        margin: auto;
    }
    a {
        color: #0077cc;
        text-decoration: none;
    }
    a:hover {
        text-decoration: underline;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 15px;
    }
    th, td {
        padding: 10px;
        border: 1px solid #ccc;
    }
    th {
        background: #e6e6e6;
    }
    button {
        padding: 6px 12px;
        background: #0077cc;
        border: none;
        color: white;
        border-radius: 5px;
        cursor: pointer;
    }
    button:hover {
        background: #005fa3;
    }
    .input-field, textarea {
        padding: 8px;
        width: 95%;
        margin-top: 5px;
        margin-bottom: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
</style>
</head>

<body>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.ADMIN) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>


<hr/>
<h3>All Tips</h3>
<%
    TipDAO tipDAO = new TipDAOImpl();
    List<Tip> allTips = tipDAO.findAll();
    if (allTips.isEmpty()) {
%>
    <p>No tips yet.</p>
<%
    } else {
%>
    <table border="1" cellpadding="4" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Author ID</th>
            <th>Title</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
    <%
        for (Tip t : allTips) {
    %>
        <tr>
            <td><%= t.getId() %></td>
            <td><%= t.getAuthorId() %></td>
            <td><%= t.getTitle() %></td>
            <td><%= t.getStatus() %></td>
            <td>
                <%
                    if (t.getStatus() == Tip.Status.PENDING) {
                %>
                    <form style="display:inline;"
                          action="<%=request.getContextPath()%>/admin-tips" method="post">
                        <input type="hidden" name="tipId" value="<%= t.getId() %>"/>
                        <input type="hidden" name="action" value="approve"/>
                        <button type="submit">Approve</button>
                    </form>
                    <form style="display:inline;"
                          action="<%=request.getContextPath()%>/admin-tips" method="post">
                        <input type="hidden" name="tipId" value="<%= t.getId() %>"/>
                        <input type="hidden" name="action" value="reject"/>
                        <button type="submit">Reject</button>
                    </form>
                <%
                    } else {
                %>
                    (no actions)
                <%
                    }
                %>
            </td>
        </tr>
    <%
        }
    %>
    </table>
<%
    }
%>

<hr/>
<h3>Recent Activity</h3>
<%
    ActivityLogger logger = ActivityLogger.getInstance();
    List<String> events = logger.snapshot();
    if (events.isEmpty()) {
%>
    <p>No activity yet.</p>
<%
    } else {
%>
    <ul>
    <%
        for (String e : events) {
    %>
        <li><%= e %></li>
    <%
        }
    %>
    </ul>
<%
    }
%>


<h2>Admin Dashboard</h2>
<p>Welcome, <b><%= user.getName() %></b> (ADMIN)</p>

<h3>All Users</h3>
<ul>
<%
    UserService us = LoginServlet.getUserService();
    for (User u : us.findAll()) {
%>
    <li><%= u.toString() %></li>
<%
    }
%>
</ul>

<a href="<%=request.getContextPath()%>/index.jsp">Back to Home</a>
<li><a href="<%=request.getContextPath()%>/discussions">View discussions</a></li>

</body>
</html>
