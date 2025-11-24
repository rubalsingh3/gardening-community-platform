<%@ page import="java.util.List" %>
<%@ page import="com.gardencommunity.model.Discussion" %>
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
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
    }
%>

<h2>Discussions</h2>

<% if (user.getRole() == User.Role.GARDENER) { %>
<h3>Start a new discussion</h3>
<form action="<%=request.getContextPath()%>/discussions" method="post">
    <p>Title: <input name="title" size="40" /></p>
    <p>Content:<br/>
        <textarea name="content" rows="4" cols="60"></textarea>
    </p>
    <button type="submit">Create Discussion</button>
</form>
<% } %>

<hr/>

<h3>All discussions</h3>
<%
    List<Discussion> ds = (List<Discussion>) request.getAttribute("discussions");
    if (ds == null || ds.isEmpty()) {
%>
    <p>No discussions yet.</p>
<%
    } else {
%>
    <ul>
    <%
        for (Discussion d : ds) {
    %>
        <li>
            <b><%= d.getTitle() %></b>
            (by user <%= d.getAuthorId() %>)<br/>
            <pre style="font-family:inherit;"><%= d.getContent() %></pre>
            <hr/>
        </li>
    <%
        }
    %>
    </ul>
<%
    }
%>

<p>
<% if (user.getRole() == User.Role.GARDENER) { %>
    <a href="<%=request.getContextPath()%>/gardener.jsp">Back to Gardener Dashboard</a>
<% } else { %>
    <a href="<%=request.getContextPath()%>/admin.jsp">Back to Admin Dashboard</a>
<% } %>
</p>

</body>
</html>
