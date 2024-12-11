package com.techelevator.model;

public class RecipeIngredient {

    private int id;
    private int recipe_id;
    private int ingredient_id;
    private Ingredient ingredient; // Association with Ingredient class
    private double quantity;
    private String unit;

    // Default constructor
    public RecipeIngredient() {}

    // Constructor with parameters
    public RecipeIngredient(int id, int recipe_id, int ingredient_id, Ingredient ingredient, double quantity, String unit) {
        this.id = id;
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
        this.ingredient = ingredient; // Associated Ingredient
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for recipe_id
    public int getRecipe_id() {
        return recipe_id;
    }

    // Setter for recipe_id
    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    // Getter for ingredient_id
    public int getIngredient_id() {
        return ingredient_id;
    }

    // Setter for ingredient_id
    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    // Getter for ingredient (Ingredient object)
    public Ingredient getIngredient() {
        return ingredient;
    }

    // Setter for ingredient (Ingredient object)
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    // Getter for ingredient_name from the associated Ingredient object
    public String getIngredient_name() {
        return ingredient != null ? ingredient.getIngredient_name() : null; // Null-safe
    }

    // Getter for type_id from the associated Ingredient object
    public int getType_id() {
        return ingredient != null ? ingredient.getType_id() : 0; // Null-safe and default to 0 if ingredient is null
    }

    // Getter for quantity
    public double getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    // Getter for unit
    public String getUnit() {
        return unit;
    }

    // Setter for unit
    public void setUnit(String unit) {
        this.unit = unit;
    }
}