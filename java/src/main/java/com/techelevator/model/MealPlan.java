package com.techelevator.model;

import java.util.Date;

public class MealPlan {

    private int meal_plan_id;
    private String meal_plan_name;
    private int user_id;
    private Date creation_date;
    private Date start_date;
    private Date end_date;


    public MealPlan(int meal_plan_id, String meal_plan_name, int user_id, Date creation_date, Date start_date, Date end_date) {
        this.meal_plan_id = meal_plan_id;
        this.meal_plan_name = meal_plan_name;
        this.user_id = user_id;
        this.creation_date = creation_date;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getMeal_plan_id() {
        return meal_plan_id;
    }

    public void setMeal_plan_id(int meal_plan_id) {
        this.meal_plan_id = meal_plan_id;
    }

    public String getMeal_plan_name() {
        return meal_plan_name;
    }

    public void setMeal_plan_name(String meal_plan_name) {
        this.meal_plan_name = meal_plan_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

}
