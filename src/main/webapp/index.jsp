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
<h2>Welcome to Garden Community</h2>

<% String error = (String) request.getAttribute("error");
   if (error != null) { %>
   <p style="color:red;"><%= error %></p>
<% } %>

<h3>Login</h3>
<form action="login" method="post">
    Email: <input name="email" />
    <button type="submit">Login</button>
</form>

<h3>Register as Gardener</h3>
<form action="register" method="post">
    Name: <input name="name" />
    Email: <input name="email" />
    <button type="submit">Register</button>
</form>

<p>Admin demo account: <b>admin@garden.com</b></p>
</body>
</html>
