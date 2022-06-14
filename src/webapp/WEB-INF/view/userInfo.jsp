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
        <h1><%= user.getPassword() %></h1>
        <form name="logout" method="post" action="/user/logout">
            <input type="submit" value="로그아웃">
        </form>
        <form name="leave" method="post" action="/user/leave">
            <input type="submit" value="사용자 계정 삭제">
            <input type="hidden" name="idUniq" value="<%= user.getIdUniq() %>">
        </form>
        <hr>
        <footer>BCSD-ToDo</footer>
    </div>
    </body>
</html>
