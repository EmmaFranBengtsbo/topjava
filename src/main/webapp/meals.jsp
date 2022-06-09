<%--
  Created by IntelliJ IDEA.
  User: krkat
  Date: 03.06.2022
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        .normal {color: green;}
        .excess {color: red;}
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>Meals</h3>
<table border=1 cellpadding="8" cellspacing="8">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Превышение</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${mealTos}" var="mealTo">
        <jsp:useBean id="mealTo" scope="page" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${mealTo.excess ? 'excess' : 'normal'}">
            <td>
                <fmt:parseDate value="${mealTo.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" />
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />
            </td>
            <td> <c:out value="${mealTo.description}"/></td>
            <td> <c:out value="${mealTo.calories}"/></td>
            <td> <c:out value="${mealTo.excess}"/></td>
            <td><a href="meals?action=update&id=${mealTo.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>

    <jsp:useBean id="mealTos" scope="request" type="java.util.List"/>
    <c:forEach items="${mealTos}" var="mealTo">
        <tr style="color : ${mealTo.excess ? 'red' : 'green'}">

        </tr>
    </c:forEach>
</table>
<p>
    <a href="meals?action=create">Create Meal</a>
</p>
</body>
</html>
