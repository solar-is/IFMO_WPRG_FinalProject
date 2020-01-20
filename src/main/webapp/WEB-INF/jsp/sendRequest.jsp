<%@ page import="com.prosolovich.ConnectionSource" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
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
            width:80px;
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
    <%
        String title = (String) session.getAttribute("movieTitle");
        ConnectionSource connectionSource = ConnectionSource.instance();
        int[] genrePrefs = new int[28];
        try {
            PreparedStatement preparedStatement = connectionSource.createConnection().prepareStatement("select * from main.movie inner join main.genre on " +
                    "movie.genresmapping=genre.id where title=?");
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            for (int i = 0;i<28; ++i){
                genrePrefs[i] = resultSet.getInt(7 + i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    %>
    <div class="block">
        <label class="item">Action:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[0] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Adult:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[1] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Adventure:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[2] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Animation:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[3] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Biography:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[4] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Comedy:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[5] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Crime:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[6] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Documentary:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[7] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Drama:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[8] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Family:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[9] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Fantasy:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[10] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Noir:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[11] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Game-Show:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[12] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">History:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[13] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Horror:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[14] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Musical:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[15] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Music:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[16] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Mystery:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[17] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">News:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[18] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Reality:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[19] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Romance:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[20] %>" path="genrePrefs"  required="true"/></div>
        <br><br>
        <label class="item">Sci-Fi:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[21] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Short:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[22] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Sport:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[23] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Talk-Show:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[24] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Thriller:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[25] %>" path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">War:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[26] %>"  path="genrePrefs" required="true"/></div>
        <br><br>
        <label class="item">Western:</label>
        <div class="item"><form:input type="number" min="1" max="10" placeholder="<%= genrePrefs[27] %>" path="genrePrefs" required="true"/></div>
        <br><br>
    </div>    <br>
    <input class="mybutton" type="submit" value="Submit"/>
</form:form>



</body>
</html>
