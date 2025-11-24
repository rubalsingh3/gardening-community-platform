<%@ page import="java.util.List" %>
<%@ page import="com.gardencommunity.model.Project" %>
<%@ page import="com.gardencommunity.model.User" %>

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
    if (user == null || user.getRole() != User.Role.GARDENER) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>

<h2>My Gardening Projects</h2>

<h3>Create new project</h3>
<form action="<%=request.getContextPath()%>/projects" method="post">
    <p>Title: <input name="title" size="40" /></p>
    <p>Description:<br/>
        <textarea name="description" rows="3" cols="60"></textarea>
    </p>
    <button type="submit">Create Project</button>
</form>

<hr/>

<h3>Your existing projects</h3>
<%
    List<Project> projects = (List<Project>) request.getAttribute("projects");
    if (projects == null || projects.isEmpty()) {
%>
    <p>No projects yet.</p>
<%
    } else {
%>
    <table border="1" cellpadding="4" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Progress (%)</th>
            <th>Update progress</th>
        </tr>
    <%
        for (Project p : projects) {
    %>
        <tr>
            <td><%= p.getId() %></td>
            <td><%= p.getTitle() %></td>
            <td><%= p.getDescription() %></td>
            <td><%= p.getProgress() %></td>
            <td>
                <form action="<%=request.getContextPath()%>/projects" method="post">
                    <input type="hidden" name="action" value="updateProgress"/>
                    <input type="hidden" name="projectId" value="<%= p.getId() %>"/>
                    <input type="number" name="progress" min="0" max="100"
                           value="<%= p.getProgress() %>" />
                    <button type="submit">Save</button>
                </form>
            </td>
        </tr>
    <%
        }
    %>
    </table>
<%
    }
%>

<p><a href="<%=request.getContextPath()%>/gardener.jsp">Back to Gardener Dashboard</a></p>

</body>
</html>
