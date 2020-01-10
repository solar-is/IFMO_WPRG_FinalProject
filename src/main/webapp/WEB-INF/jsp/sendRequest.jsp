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

        .block {
            padding-left: 40px;
            display: flex;
            flex-wrap: wrap;
        }

        .item {
            padding: 5px;
        }

        .mybutton {
            width: 130px;
            height: 23px;
        }
    </style>
</head>
<body>
<h2>Send Request</h2>
<%--@elvariable id="request" type="java"--%>
<%--@elvariable id="userLogin" type="java"--%>
<%--@elvariable id="movieTitle" type="java"--%>
<form:form action="/postRequest" modelAttribute="request">
    <h3>Fill the needed genres fields (integers 1..10)</h3>
    <br>
    <p> User Login: <form:label path="userLogin"> ${request.userLogin} </form:label></p>
    <p> Movie Title: <form:label path="movieTitle"> ${request.movieTitle} </form:label></p>
    <br>
    <div class="block">
        <div class="item"><form:input path="genrePrefs" placeholder="Action" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Adult" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Adventure" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Animation" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Biography" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Comedy" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Crime" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Documentary" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Drama" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Family" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Fantasy" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Noir" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Game-Show" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="History" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Horror" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Musical" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Music" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Mystery" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="News" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Reality" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Romance" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Sci-Fi" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Short" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Sport" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Talk-Show" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Thriller" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="War" required="true"/></div>
        <br><br>
        <div class="item"><form:input path="genrePrefs" placeholder="Western" required="true"/></div>
        <br><br>
    </div>
    <br>
    <input class="mybutton" type="submit" value="Submit"/>
</form:form>


</body>
</html>
