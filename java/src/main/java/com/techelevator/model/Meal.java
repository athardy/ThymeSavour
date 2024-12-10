package com.techelevator.model;

import java.util.Date;
import java.time.LocalDate;

public class Meal {

    private int meal_id;
    private int meal_plan_id;
    private int recipe_id;
    private LocalDate meal_date;
    private String meal_type;

    public Meal(int meal_id, int meal_plan_id, int recipe_id, LocalDate meal_date, String meal_type) {
        this.meal_id = meal_id;
        this.meal_plan_id = meal_plan_id;
        this.recipe_id = recipe_id;
        this.meal_date = meal_date;
        this.meal_type = meal_type;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public int getMeal_plan_id() {
        return meal_plan_id;
    }

    public void setMeal_plan_id(int meal_plan_id) {
        this.meal_plan_id = meal_plan_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public LocalDate getMeal_date() {
        return meal_date;
    }

    public void setMeal_date(LocalDate meal_date) {
        this.meal_date = meal_date;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }
}
