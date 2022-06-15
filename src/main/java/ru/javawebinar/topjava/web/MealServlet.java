package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealRepository;
import ru.javawebinar.topjava.dao.MealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static String CREATE_OR_EDIT = "/edit.jsp";
    private static String MEALS_LIST = "/meals.jsp";
    private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to edit");
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info(meal.isNew() ? "create{}" : "update", meal);
        repository.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
       String action = request.getParameter("action");
       switch (action == null ? "all" : action) {
           case "delete": {
               int id = getId(request);
               log.info("delete {}", id);
               repository.delete(id);
               response.sendRedirect("meals");
               //request.getRequestDispatcher("meals.jsp").forward(request, response);
               break;
           }
           case "create":
           case "update": {
               final Meal meal = action.equals("create") ?
                       new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                       repository.get(getId(request));
               request.setAttribute("meal", meal);
               request.getRequestDispatcher("/edit.jsp").forward(request, response);
               break;
           }
           case "all":
           case "default": {
               log.info("getAll");
               request.setAttribute("mealTos", MealsUtil.getMealTos(new ArrayList<>(repository.getAll())));
               request.getRequestDispatcher("/meals.jsp").forward(request, response);
           }
       }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}