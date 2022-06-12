<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>로그인</title>
    </head>
    <body>
        <div style="text-align: center">
            <form name="login" method="post" onsubmit="this.action = '/user/' + this.id.value">
                <label>아아디:&nbsp;<input type="text" name="id"></label>
                <label>비밀번호:&nbsp;<input type="password" name="password"></label>
                <input type="submit" name="submit" value="로그인">
            </form>
            <hr>
            <footer>BCSD-ToDo</footer>
        </div>
    </body>
</html>
