package com.techelevator.model;

public class Ingredient {

    private int ingredient_id;
    private String ingredient_name;
    private int type_id;

    // Default constructor
    public Ingredient() {}

    // Constructor with parameters
    public Ingredient(int ingredient_id, String ingredient_name, int type_id) {
        this.ingredient_id = ingredient_id;
        this.ingredient_name = ingredient_name;
        this.type_id = type_id;
    }

    // Getter for ingredient_id
    public int getIngredient_id() {
        return ingredient_id;
    }

    // Setter for ingredient_id
    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    // Getter for ingredient_name
    public String getIngredient_name() {
        return ingredient_name;
    }

    // Setter for ingredient_name
    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    // Getter for type_id
    public int getType_id() {
        return type_id;
    }

    // Setter for type_id
    public void setType_id(int type_id) {
        this.type_id = type_id;
    }
}