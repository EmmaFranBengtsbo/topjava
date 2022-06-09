package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Meal get(int id);
    Meal save(Meal meal);
    void delete(int id);
    Collection<Meal> getAll();
}
