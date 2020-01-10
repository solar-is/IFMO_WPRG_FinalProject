<%@ page import="com.prosolovich.domain.User" %>
<%@ page import="com.prosolovich.domain.Role" %>
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
            display: inline-block;
            width: 300px;
        }

        .mybutton {
            width: 130px;
            height: 23px;
        }


        .mybutton2 {
            visibility: hidden;
            width: 130px;
            height: 23px;
        }
    </style>
</head>
<body>

<h2>Main page</h2>
<div class="block">
    <h1>Login: ${user.login} <br>
        Role: ${user.role} </h1>
    <form action="/editProfile" method="post">
        <input class="mybutton" type="submit" value="Edit profile">
    </form>
    <br>
    <br>
    <form action="/" method="post">
        <input class="mybutton" type="submit" value="Exit">
    </form>
</div>
<div class="block">

    <form action="/findFilmByIdOrTitle" method="post">
        <input required="true" placeholder="IMDBID or Title" type="text" name="idOrTitle"/>
        <br><br>
        <br>
        <input class="mybutton" type="submit" value="Find"/>
    </form>

    <br>
    <br>

    <form action="/getRecommendation" method="post">
        <input class="mybutton" type="submit" value="Recommendation">
    </form>
</div>

<%
    User user = (User) request.getAttribute("user");
    if (!user.getRole().equals(Role.USUAL)) {
%>
<div class="block">
    <%
        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
    %>
    <form action="/resolveReqs" method="post">
        <input class="mybutton" type="submit" value="Resolve requests">
    </form>
    <br>
    <% }
        if (user.getRole().equals(Role.ADMIN)) {%>
    <br>
    <form action="/changeRole" method="post">
        <input class="mybutton" type="submit" value="Change role of user">
    </form>
    <% }  else { %>
    <br>
    <form action="/changeRole" method="post">
        <input class="mybutton2" type="submit" value="Change role of user">
    </form>
    <% } %>
</div>
<% } %>
<%
    if (request.getAttribute("noSuchFilm") != null){
        boolean b = (boolean) request.getAttribute("noSuchFilm");
        if (b)
            out.println("<br><br>No such film on imdb");
    }
%>
<%
    if (request.getAttribute("deleted") != null){

        boolean b = (boolean) request.getAttribute("deleted");
        if (b)
            out.println("<br><br>Film '" + request.getAttribute("title") + "' was deleted successfully");
    }
%>
<%
    if (request.getAttribute("roleUpdated") != null){
        boolean b = (boolean) request.getAttribute("roleUpdated");
        if (b)
            out.println("<br><br>Role was updated successfully");
    }
%>
<%
    if (request.getAttribute("noSuchUser") != null){
        boolean b = (boolean) request.getAttribute("noSuchUser");
        if (b)
            out.println("<br><br>No such user in system");
    }
%>
<%
    if (request.getAttribute("noRequests") != null){
        boolean b = (boolean) request.getAttribute("noRequests");
        if (b)
            out.println("<br><br>There are no requests from users in storage");
    }
%>

</body>
</html>