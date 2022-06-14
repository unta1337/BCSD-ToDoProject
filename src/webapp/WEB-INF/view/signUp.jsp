<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>회원가입</title>
    </head>
    <body>
        <div style="text-align: center">
            <form name="signUp" method="post" action="/user">
                <label>아아디:&nbsp;<input type="text" name="id"></label>
                <label>비밀번호:&nbsp;<input type="password" name="password"></label>
                <input type="submit" name="submit" value="회원가입">
            </form>
            <hr>
            <footer>BCSD-ToDo</footer>
        </div>
    </body>
</html>
