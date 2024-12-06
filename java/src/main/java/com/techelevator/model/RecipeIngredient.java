package com.techelevator.model;

public class RecipeIngredient {

    private int id;
    private int recipe_id;
    private int ingredient_id;
    private double quantity;
    private String unit;

    public RecipeIngredient(int id, int recipe_id, int ingredient_id, double quantity, String unit) {
        this.id = id;
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
