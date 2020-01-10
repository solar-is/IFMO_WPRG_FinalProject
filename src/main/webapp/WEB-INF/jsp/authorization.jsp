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
<h2>Authorization</h2>
<%--@elvariable id="user" type="java"--%>
<form:form action="/authorize" modelAttribute="user">
    <form:input required="true" path="login" placeholder="Login"/>
    <br><br>
    <form:input required="true" path="password" type="password" placeholder="Password"/>
    <br><br>
    <input class="mybutton" type="submit" value="Sign in"/>
</form:form>
<br>
<form:form action="/register">
    <input class="mybutton" type="submit" value="Sign up">
</form:form>
<br>

<%
    if (request.getAttribute("error") != null) {
        boolean isErrorHere = (boolean) request.getAttribute("error");
        if (isErrorHere) {
            out.println("Login/Password is invalid");
        }
    }
    if (request.getAttribute("successRegister") != null) {
        boolean isOk = (boolean) request.getAttribute("successRegister");
        if (isOk) {
            out.println("Successful Registration");
        }
    }
    if (request.getAttribute("equalUser") != null) {
        boolean isOk = (boolean) request.getAttribute("equalUser");
        if (isOk) {
            out.println("There is user with same login, please take another one");
        }
    }
%>

</body>
</html>
