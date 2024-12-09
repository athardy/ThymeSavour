<template>
    <div class="recipe-creation-container">
      <h1>Create a New Recipe</h1>
      <form @submit.prevent="submitRecipe" class="recipe-form">
        <!-- Recipe Info -->
        <div class="form-group">
          <label for="recipe-name">Recipe Name:</label>
          <input
            id="recipe-name"
            v-model="recipe.recipe_name"
            placeholder="Enter recipe name..."
            required
          />
        </div>
        <div class="form-group">
          <label for="description">Description:</label>
          <input
            id="description"
            v-model="recipe.description"
            placeholder="Enter description..."
            required
          />
        </div>
        <div class="form-group">
          <label for="instructions">Preparation Instructions:</label>
          <textarea
            id="instructions"
            v-model="recipe.instructions"
            placeholder="Enter preparation instructions here"
            rows="8"
            required
          ></textarea>
        </div>
  
        <!-- Ingredients Section -->
        <div class="ingredients-section">
          <h2>Ingredients</h2>
          <div class="ingredient-form">
            <input
              type="text"
              v-model="newIngredient.name"
              @input="fetchIngredientSuggestions"
              placeholder="Ingredient name"
            />
            <input
              type="number"
              v-model="newIngredient.quantity"
              placeholder="Quantity"
            />
            <input
              type="text"
              v-model="newIngredient.unit"
              placeholder="Unit (e.g., cups, grams)"
            />
            <button type="button" @click="addIngredient">Add Ingredient</button>
          </div>
  
          <!-- Suggestions -->
          <ul v-if="ingredientSuggestions.length" class="suggestions-list">
            <li
              v-for="suggestion in ingredientSuggestions"
              :key="suggestion.ingredient_id"
              @click="selectSuggestedIngredient(suggestion)"
            >
              {{ suggestion.ingredient_name }}
            </li>
          </ul>
  
          <!-- Ingredients List -->
          <ul class="ingredients-list">
            <li v-for="(ingredient, index) in recipe.ingredients" :key="index">
              {{ ingredient.quantity }} {{ ingredient.unit }} of
              {{ ingredient.name }}
              <button type="button" @click="removeIngredient(index)">Remove</button>
            </li>
          </ul>
        </div>
  
        <div class="form-buttons">
          <button type="submit">Save Recipe</button>
          <button type="button" @click="cancelRecipe">Cancel</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        recipe: {
          recipe_name: "",
          description: "",
          instructions: "",
          ingredients: [], // List to store added ingredients
        },
        newIngredient: {
          name: "",
          quantity: 0,
          unit: "",
        },
        ingredientSuggestions: [],
      };
    },
    methods: {
      async fetchIngredientSuggestions() {
        if (this.newIngredient.name.length > 2) {
          try {
            const response = await axios.get(
              `${import.meta.env.VITE_REMOTE_API}/ingredients/name/${this.newIngredient.name}`,
              {
                headers: {
                  Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
              }
            );
            this.ingredientSuggestions = response.data;
          } catch (error) {
            console.error("Error fetching ingredient suggestions:", error);
          }
        } else {
          this.ingredientSuggestions = [];
        }
      },
      selectSuggestedIngredient(suggestion) {
        this.newIngredient.name = suggestion.ingredient_name;
        this.ingredientSuggestions = [];
      },
      addIngredient() {
        if (
          this.newIngredient.name &&
          this.newIngredient.quantity > 0 &&
          this.newIngredient.unit
        ) {
          this.recipe.ingredients.push({ ...this.newIngredient });
          this.newIngredient = { name: "", quantity: 0, unit: "" };
        } else {
          alert("Please provide valid ingredient details.");
        }
      },
      removeIngredient(index) {
        this.recipe.ingredients.splice(index, 1);
      },
      async submitRecipe() {
  try {
    console.log("Submitting recipe:", this.recipe);
    const response = await axios.post(
      `${import.meta.env.VITE_REMOTE_API}/recipes/create`,
      this.recipe,
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );

    alert(response.data); // Success message
    this.$router.push({ name: "my-recipes" });
  } catch (error) {
    console.error("Error saving recipe:", error.response?.data || error.message);
    alert("Failed to create your recipe.");
  }
},
      cancelRecipe() {
        this.recipe = { recipe_name: "", description: "", instructions: "", ingredients: [] };
        this.newIngredient = { name: "", quantity: 0, unit: "" };
        this.$router.push({ name: "home" });
      },
    },
  };
  </script>
  
  <style scoped>
  .recipe-creation-container {
    max-width: 800px;
    margin: auto;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .recipe-form {
    display: flex;
    flex-direction: column;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  input,
  textarea {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
    margin-bottom: 10px;
  }
  
  textarea {
    resize: none;
  }
  
  .ingredient-form {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
  }
  
  .ingredient-form input {
    flex: 1;
  }
  
  .ingredient-form button {
    padding: 8px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .ingredient-form button:hover {
    background-color: #0056b3;
  }
  
  .suggestions-list {
    list-style: none;
    padding: 0;
    margin-bottom: 15px;
  }
  
  .suggestions-list li {
    padding: 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
    cursor: pointer;
    margin-bottom: 5px;
  }
  
  .suggestions-list li:hover {
    background-color: #f0f0f0;
  }
  
  .ingredients-list {
    list-style: none;
    padding: 0;
  }
  
  .ingredients-list li {
    margin-bottom: 10px;
  }
  
  .form-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  button {
    padding: 10px 20px;
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #45a049;
  }
  
  button[type="button"]:last-child {
    background-color: #f44336;
  }
  
  button[type="button"]:last-child:hover {
    background-color: #d32f2f;
  }
  </style>