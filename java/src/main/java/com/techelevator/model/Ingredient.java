package com.techelevator.model;

public class Ingredient {

    private int ingredient_id;
    private String ingredient_name;
    private int type_id;

    public Ingredient(int ingredient_id, String ingredient_name, int type_id) {
        this.ingredient_id = ingredient_id;
        this.ingredient_name = ingredient_name;
        this.type_id = type_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }
}
