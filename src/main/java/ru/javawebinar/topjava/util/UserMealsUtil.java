package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDates = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDates.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                int allCaloriesForThisDay = caloriesPerDates.get(meal.getDateTime().toLocalDate());
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), allCaloriesForThisDay > caloriesPerDay);
                result.add(mealWithExcess);
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        class MyCollector implements Collector<UserMeal, Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>> {
            @Override
            public Supplier<Map<LocalDate, List<UserMeal>>> supplier() {
                return HashMap:: new;
            }
            @Override
            public BiConsumer<Map<LocalDate, List<UserMeal>>, UserMeal> accumulator() {
                return (mapOfMealsPerDays, meal) -> {
                    if (!mapOfMealsPerDays.containsKey(meal.getDateTime().toLocalDate())) {
                        List<UserMeal> newList = new ArrayList<>();
                        newList.add(meal);
                        mapOfMealsPerDays.put(meal.getDateTime().toLocalDate(), newList);
                    } else {
                        List<UserMeal> existingList = mapOfMealsPerDays.get(meal.getDateTime().toLocalDate());
                        existingList.add(meal);
                    }
                };
            }
            @Override
            public BinaryOperator<Map<LocalDate, List<UserMeal>>> combiner() {
                return (l, r) -> {
                    for (LocalDate date : r.keySet()) {
                        if (!l.containsKey(date)) {
                            List<UserMeal> newList = r.get(date);
                            l.put(date, newList);
                        } else {
                            List<UserMeal> existingList = r.get(date);
                            for (UserMeal meal : existingList) {
                                l.get(date).add(meal);
                            }
                        }
                    }
                    return l;
                };
            }

            @Override
            public Function<Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>> finisher() {
                return (s) -> {
                    List<UserMealWithExcess> result = new ArrayList<>();
                    for (LocalDate date : s.keySet()) {
                        List<UserMeal> mealList = s.get(date);
                        int calories = 0;
                        for (UserMeal m : mealList) {
                            calories += m.getCalories();
                        }
                        boolean excess = calories > caloriesPerDay;
                        for (UserMeal m : mealList) {
                            if (TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(),startTime,endTime)) {
                                result.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), excess));
                            }
                        }
                    }
                    return result;
                };
            }
            @Override
            public Set<Collector.Characteristics> characteristics() {
                return EnumSet.of(Collector.Characteristics.CONCURRENT);
            }
        }
        return meals.stream()
                .collect(new MyCollector());
    }
}
