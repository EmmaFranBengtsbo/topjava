<%--
  Created by IntelliJ IDEA.
  User: krkat
  Date: 07.06.2022
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <title>Create/Edit meal</title>
</head>
<body>
<section>
    <h2><a href="">Home</a></h2>
    <h2>Edit Meal</h2>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}"/>
        <dl>
            <dt>DateTime:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"/></dd>
        </dl>
        <dl>
            <dt>Description:</dt>
            <dd><input type="text" value="${meal.description}" size="40" name="description"/></dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><input id="calories" type="number" value="${meal.calories}" size="40" name="calories"/></dd>
        </dl>

        <button type="submit">Save</button>
        <button type="reset">Clean</button>
        <button onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>
