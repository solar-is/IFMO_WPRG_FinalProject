<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>MyApp</title>
    <style>

        <%@include file="/WEB-INF/css/style.css"%>

        html, body {
            margin: 0;
            padding: 0;
            text-align: center;
        }

        .mybutton {
            width: 130px;
            height: 23px;
        }
    </style>
</head>
<body>
<h2>Change Role</h2>
<%--@elvariable id="user" type="java"--%>
<form action="/changeUserRole" method="post">
    <input type="text" required="true" name="userLogin" placeholder="Login"/>
    <br><br>
    <input type="radio" name="role" value="usual"/> Usual
    <br><br>
    <input type="radio" name="role" value="moderator"/> Moderator
    <br><br>
    <input class="mybutton" type="submit" value="Change Role"/>
</form>
<br>


</body>
</html>
