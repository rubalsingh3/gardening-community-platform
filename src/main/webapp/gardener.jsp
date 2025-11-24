<%@ page import="com.gardencommunity.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Gardener Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>

<body>

<%
    User u = (User) session.getAttribute("user");
    if (u == null || u.getRole() != User.Role.GARDENER) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>

<div class="app-header">
    <h1>Gardener Dashboard</h1>
    <small>Welcome, <%= u.getName() %> 🌿</small>
</div>

<div class="container">

    <h2 class="section-title">Your Gardening Tools</h2>
    <p class="sub-title">Choose where you want to go next</p>

    <ul>
        <li><a href="<%=request.getContextPath()%>/tips">🌱 Manage My Tips</a></li>
        <li><a href="<%=request.getContextPath()%>/discussions">💬 Community Discussions</a></li>
        <li><a href="<%=request.getContextPath()%>/projects">📌 My Gardening Projects</a></li>
    </ul>

    <br><br>
    <a href="<%=request.getContextPath()%>/index.jsp">⬅ Back to Home</a>

</div>

</body>
</html>
