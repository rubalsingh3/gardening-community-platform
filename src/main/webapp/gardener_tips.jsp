<%@ page import="java.util.List" %>
<%@ page import="com.gardencommunity.model.Tip" %>
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

    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
    }
%>

<h2>Your Gardening Tips</h2>

<form action="<%=request.getContextPath()%>/tips" method="post">
    <p>Title: <input name="title" size="40" /></p>
    <p>Description:<br/>
        <textarea name="description" rows="4" cols="60"></textarea>
    </p>
    <button type="submit">Submit Tip</button>
</form>

<hr/>

<%
    List<Tip> myTips = (List<Tip>) request.getAttribute("myTips");
    if (myTips == null || myTips.isEmpty()) {
%>
    <p>No tips yet.</p>
<%
    } else {
%>
    <ul>
    <%
        for (Tip t : myTips) {
    %>
        <li>
            <b><%= t.getTitle() %></b> -
            <i><%= t.getStatus() %></i><br/>
            <pre style="font-family:inherit;"><%= t.getDescription() %></pre>
        </li>
    <%
        }
    %>
    </ul>
<%
    }
%>

<p><a href="<%=request.getContextPath()%>/gardener.jsp">Back to Gardener Dashboard</a></p>

</body>
</html>
