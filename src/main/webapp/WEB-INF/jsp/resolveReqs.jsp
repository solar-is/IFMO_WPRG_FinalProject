<%--@elvariable id="reqId" type="java"--%>
<%--@elvariable id="title" type="java"--%>
<%--@elvariable id="userLogin" type="java"--%>
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

        .block {
            display: inline-block;
            text-align: center;
            width: 320px;
        }

        .block2 {
            display: inline-block;
            text-align: center;
            width: 200px;
        }

    </style>
</head>
<body>
<h2>Request</h2>
<h1>User Login: ${userLogin} <br>
    Movie Title: ${title} </h1>

<%
    int[] news = (int[]) request.getAttribute("news");
    int[] olds = (int[]) request.getAttribute("olds");
    String[] genre = new String[28];
    genre[0] = "Action: ";
    genre[1] = "Adult: ";
    genre[2] = "Adventure: ";
    genre[3] = "Animation: ";
    genre[4] = "Biography: ";
    genre[5] = "Comedy: ";
    genre[6] = "Crime: ";
    genre[7] = "Documentary: ";
    genre[8] = "Drama: ";
    genre[9] = "Family: ";
    genre[10] = "Fantasy: ";
    genre[11] = "Film-Noir: ";
    genre[12] = "GameShow: ";
    genre[13] = "History: ";
    genre[14] = "Horror: ";
    genre[15] = "Musical: ";
    genre[16] = "Music: ";
    genre[17] = "Mystery: ";
    genre[18] = "News: ";
    genre[19] = "Reality: ";
    genre[20] = "Romance: ";
    genre[21] = "Sci-Fi: ";
    genre[22] = "Short: ";
    genre[23] = "Sport: ";
    genre[24] = "Talk-Show: ";
    genre[25] = "Thriller: ";
    genre[26] = "War: ";
    genre[27] = "Western: ";
    String[] ans = new String[28];
    for (int i = 0; i < 28; i++) {
        ans[i] = genre[i] + olds[i] + " to " + news[i];
    } %>

<div class="block2">
    <%
        for (int i = 0; i < 28; i+=2) {
            out.println(ans[i] + "<br>");
        }
    %>
</div>

<div class="block2">
    <%
        for (int i = 1; i < 28; i+=2) {
            out.println(ans[i] + "<br>");
        }
    %>
</div>
<br>
<br>
<div class="block">
    <form action="/acceptReq/${reqId}" method="post">
        <input class="mybutton" type="submit" value="Accept">
    </form>
</div>
<div class="block">
    <form action="/declineReq/${reqId}" method="post">
        <input class="mybutton" type="submit" value="Decline">
    </form>
</div>
<br><br>
<form action="/mainPage" method="get">
    <input class="mybutton" type="submit" value="To the main page">
</form>

</body>
</html>
