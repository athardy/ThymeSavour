<template>
  <div class="recipe-creation">
    <h1>Create a New Recipe</h1>

    <form @submit.prevent="createRecipe">
      <!-- Recipe Name -->
      <div class="form-group">
        <label for="recipeName">Recipe Name:</label>
        <input
          type="text"
          id="recipeName"
          v-model="recipe.recipe_name"
          required
          placeholder="Enter recipe name"
        />
      </div>

      <!-- Description -->
      <div class="form-group">
        <label for="description">Description:</label>
        <textarea
          id="description"
          v-model="recipe.description"
          required
          placeholder="Enter a brief description of the recipe"
        ></textarea>
      </div>

      <!-- Instructions -->
      <div class="form-group">
        <label for="instructions">Instructions:</label>
        <textarea
          id="instructions"
          v-model="recipe.instructions"
          required
          placeholder="Enter the cooking instructions"
        ></textarea>
      </div>

      <!-- Ingredients Section -->
      <div class="ingredient-section">
        <h2>Ingredients</h2>

        <!-- Ingredient Search -->
        <div class="ingredient-search">
          <label for="ingredientSearch">Search Ingredients:</label>
          <input
            type="text"
            id="ingredientSearch"
            v-model="ingredientSearch"
            @input="filterIngredients"
            placeholder="Search ingredients"
            list="ingredient-options"
          />
          <!-- Datalist for dynamically filtered ingredients -->
          <datalist id="ingredient-options">
            <option
              v-for="ingredient in filteredIngredients"
              :key="ingredient.ingredient_id"
              :value="ingredient.ingredient_name"
            />
          </datalist>
        </div>

        <!-- Ingredients List -->
        <div v-for="(ingredient, index) in recipe.ingredients" :key="index" class="ingredient-item">
          <input
            type="text"
            v-model="ingredient.ingredient_name"
            :placeholder="`Ingredient ${index + 1} Name`"
            required
          />
          <input
            type="number"
            v-model="ingredient.quantity"
            :placeholder="`Quantity ${index + 1}`"
            required
          />
          <input
            type="text"
            v-model="ingredient.unit"
            :placeholder="`Unit ${index + 1}`"
            required
          />
          <button @click.prevent="removeIngredient(index)" class="remove-ingredient-btn">Remove</button>
        </div>

        <!-- Add Ingredient Button -->
        <button @click.prevent="addIngredient" class="add-ingredient-btn">Add Ingredient</button>
      </div>

      <div class="submit-section">
        <button type="submit">Save Recipe</button>
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
        ingredients: [],
      },
      ingredientSearch: "",
      filteredIngredients: [],
    };
  },
  methods: {
    createRecipe() {
      axios
        .post(`${import.meta.env.VITE_REMOTE_API}/recipes`, this.recipe, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then(() => {
          alert("Recipe created successfully!");
          this.$router.push({ name: "my-recipes" });
        })
        .catch((error) => {
          console.error("Error creating recipe:", error);
          alert("Failed to create recipe.");
        });
    },
    addIngredient() {
      this.recipe.ingredients.push({ ingredient_name: "", quantity: 0, unit: "" });
    },
    removeIngredient(index) {
      this.recipe.ingredients.splice(index, 1);
    },
    filterIngredients() {
      // Your logic to filter ingredients from the API or a local list
    },
  },
};
</script>

<style scoped>
.recipe-creation {
  max-width: 800px;
  margin: auto;
  text-align: left;
}

.form-group {
  margin-bottom: 1rem;
}

button {
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  transition: background-color 0.3s ease, transform 0.2s ease;
  width: 100%;
  max-width: 200px;
}

button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button:active {
  background-color: #004494;
  transform: translateY(0);
}
</style>