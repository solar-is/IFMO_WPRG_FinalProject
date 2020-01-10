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

        img {
            padding-top: 40px;
            height: 467px;
            width: 301px;
        }

        h5 {
            font-size: medium;
            line-height: 16px;
        }

        .block {
            display: inline-block;
            text-align: left;
            width: 320px;
        }

        .block2{
            display: inline-block;
            text-align: left;
            width: 400px;
        }

        .mybutton {
            width: 130px;
            height: 23px;
        }

    </style>
</head>
<body>


<div class="block">
    <img src=${film.poster}>
</div>
<div class="block2">
    <h5>Title: ${film.title}</h5> <br>
    <h5>Year: ${film.year}</h5> <br>
    <h5>Genres: ${film.genres}</h5> <br>
    <h5>Director: ${film.director}</h5> <br>
    <h5>Actors: ${film.actors}</h5> <br>
    <h5>Plot and Awards: ${film.plotAndAwards}</h5> <br>
    <h5>Country: ${film.country}</h5> <br>
    <h5>IMDB Rating: ${film.imdbRating}</h5> <br>
</div>

<br><br>
<div>
    <form action="/sendRequest/${film.title}" method="post">
        <input class="mybutton" type="submit" value="Send request">
    </form>
    <br>
    <%
        String role = (String) request.getSession().getAttribute("userRole");
        if (role.equals("ADMIN")) { %>
    <%--@elvariable id="film" type="java"--%>
    <form action="/deleteFilm/${film.title}" method="post">
        <input class="mybutton" type="submit" value="Delete this film">
    </form>
    <br>
    <% }
    %>
</div>
</body>
</html>