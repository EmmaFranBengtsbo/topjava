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
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>Meals</h3>
<table border=1>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th>Превышение</th>
    </tr>
    <jsp:useBean id="mealsWithExcess" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsWithExcess}" var="meal">
        <tr style="color : ${meal.excess ? 'red' : 'greenyellow'}">
            <td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" />
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />
            </td>
            <td style="align-items: center" > <c:out value="${meal.description}"/></td>
            <td style="alignment: center"> <c:out value="${meal.calories}"/></td>
            <td style="alignment: center"> <c:out value="${meal.excess}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
