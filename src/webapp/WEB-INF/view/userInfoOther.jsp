<%@ page import="bcsd.todo.domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>사용자 정보</title>

        <%
            User user = (User) session.getAttribute("sessionUser");
        %>
    </head>
    <body>
    <div style="text-align: center">
        <h1><%= user.getId() %></h1>
        <hr>
        <footer>BCSD-ToDo</footer>
    </div>
    </body>
</html>
