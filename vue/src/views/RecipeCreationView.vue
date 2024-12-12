<template>
  <div class="recipe-creation">
    <div class="recipe-card">
      <!-- Step 1: Recipe Creation -->
      <div v-if="step === 1">
        <h2 class="title">Create a New Recipe</h2>
        <form @submit.prevent="saveRecipe" class="form">
          <div class="form-group">
            <label for="recipe_name">Recipe Name:</label>
            <input
              type="text"
              v-model="newRecipe.recipe_name"
              class="input-field"
              required
              placeholder="Enter recipe name"
            />
          </div>

          <div class="form-group">
            <label for="description">Description:</label>
            <textarea
              v-model="newRecipe.description"
              class="textarea-field"
              required
              placeholder="Enter recipe description"
            ></textarea>
          </div>

          <div class="form-group">
            <label for="instructions">Instructions:</label>
            <textarea
              v-model="newRecipe.instructions"
              class="textarea-field"
              required
              placeholder="Enter recipe instructions"
            ></textarea>
          </div>

          <div class="button-group">
            <button type="submit" class="nav-button save-btn">Save Recipe</button>
          </div>
        </form>
      </div>

      <!-- Step 2: Add Ingredients -->
      <div v-if="step === 2">
        <h2 class="title">Adding Ingredients to Recipe</h2>
        <div class="recipe-summary">
          <p><strong>Recipe:</strong> {{ newRecipe.recipe_name }}</p>
          <p><strong>Description:</strong> {{ newRecipe.description }}</p>
          <p><strong>Instructions:</strong> {{ newRecipe.instructions }}</p>
        </div>

        <!-- Select from Pantry -->
        <div class="form-group">
          <label for="existing_ingredients">Select Ingredient from Your Pantry:</label>
          <select v-model="selectedIngredientId" class="input-field">
            <option value="" disabled>Select an ingredient</option>
            <option
              v-for="ingredient in availableIngredients"
              :value="ingredient.ingredient_id"
              :key="ingredient.ingredient_id"
            >
              {{ ingredient.ingredient_name }}
            </option>
          </select>
          <input
            v-if="selectedIngredientId"
            type="number"
            v-model="selectedIngredientQuantity"
            placeholder="Quantity"
            class="input-field"
          />
          <input
            v-if="selectedIngredientId"
            type="text"
            v-model="selectedIngredientUnit"
            placeholder="Unit (e.g., grams, cups)"
            class="input-field"
          />
          <button
            @click.prevent="addExistingIngredient"
            class="nav-button add-ingredient-btn"
          >
            Add Ingredient
          </button>
        </div>

        <!-- Add Ingredient to Pantry -->
        <div class="form-group">
          <label for="new_ingredient">Add Ingredient to Your Pantry:</label>
          <input
            type="text"
            v-model="newIngredient.ingredient_name"
            placeholder="Ingredient name"
            class="input-field"
          />
          <select v-model="newIngredient.type_id" class="input-field">
            <option value="" disabled>Select ingredient type</option>
            <option
              v-for="type in ingredientTypes"
              :value="type.type_id"
              :key="type.type_id"
            >
              {{ type.type_name }}
            </option>
          </select>
          <input
            type="text"
            v-model="newIngredient.unit"
            placeholder="Default Unit (e.g., grams, cups)"
            class="input-field"
          />
          <button
            @click.prevent="addNewIngredient"
            class="nav-button add-ingredient-btn"
          >
            Add to Pantry
          </button>
        </div>

        <!-- Added Ingredients -->
        <h3>Ingredients Added to Recipe</h3>
        <ul>
          <li v-for="(ingredient, index) in addedIngredients" :key="index">
            {{ ingredient.ingredient_name }} - {{ ingredient.quantity }}
            {{ ingredient.unit }}
            <button
              @click="removeIngredient(index)"
              class="remove-btn"
            >
              Remove
            </button>
          </li>
        </ul>

        <!-- Save Ingredients -->
        <div class="button-group">
          <button @click="saveIngredients" class="nav-button save-btn">
            Attach Ingredients to Recipe
          </button>
          <button
            @click="$router.push('/my-recipes')"
            class="nav-button finish-btn"
          >
            Finish
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      step: 1,
      newRecipe: {
        recipe_name: "",
        description: "",
        instructions: "",
        author: this.$store.state.userId,
      },
      availableIngredients: [],
      ingredientTypes: [],
      selectedIngredientId: null,
      selectedIngredientQuantity: 0,
      selectedIngredientUnit: "",
      newIngredient: { ingredient_name: "", unit: "", type_id: "" },
      addedIngredients: [],
      recipeId: null,
    };
  },
  methods: {
    async saveRecipe() {
      try {
        const payload = {
          recipe_name: this.newRecipe.recipe_name,
          description: this.newRecipe.description,
          instructions: this.newRecipe.instructions,
        };
        const response = await axios.post(
          `${import.meta.env.VITE_REMOTE_API}/recipes`,
          payload
        );
        this.recipeId = response.data.recipe_id;
        console.log("Recipe saved with ID:", this.recipeId);
        this.step = 2;
        await this.fetchIngredients();
        await this.fetchIngredientTypes();
      } catch (error) {
        console.error("Error saving recipe:", error);
        alert("Failed to save the recipe. Please try again.");
      }
    },
    async fetchIngredients() {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/ingredients/all`
        );
        this.availableIngredients = response.data.sort((a, b) =>
          a.ingredient_name.localeCompare(b.ingredient_name)
        );
      } catch (error) {
        console.error("Error fetching ingredients:", error);
        alert("Failed to fetch ingredients. Please try again.");
      }
    },
    async fetchIngredientTypes() {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/ingredients/types`
        );
        this.ingredientTypes = response.data;
      } catch (error) {
        console.error("Error fetching ingredient types:", error);
        alert("Failed to fetch ingredient types. Please try again.");
      }
    },
    async addNewIngredient() {
      if (
        !this.newIngredient.ingredient_name ||
        !this.newIngredient.unit ||
        !this.newIngredient.type_id
      ) {
        alert("Please enter all fields: name, type, and unit.");
        return;
      }
      try {
        await axios.post(
          `${import.meta.env.VITE_REMOTE_API}/ingredients/create`,
          this.newIngredient
        );
        alert("Ingredient added to pantry successfully!");
        this.newIngredient = { ingredient_name: "", unit: "", type_id: "" };
        await this.fetchIngredients();
      } catch (error) {
        console.error("Error adding ingredient to pantry:", error);
        alert("Failed to add ingredient to pantry.");
      }
    },
    addExistingIngredient() {
      if (
        this.selectedIngredientId &&
        this.selectedIngredientQuantity > 0 &&
        this.selectedIngredientUnit
      ) {
        const ingredient = this.availableIngredients.find(
          (ing) => ing.ingredient_id === this.selectedIngredientId
        );
        if (ingredient) {
          this.addedIngredients.push({
            ...ingredient,
            quantity: this.selectedIngredientQuantity,
            unit: this.selectedIngredientUnit,
          });
          this.selectedIngredientId = null;
          this.selectedIngredientQuantity = 0;
          this.selectedIngredientUnit = "";
        }
      } else {
        alert("Please select an ingredient, enter a quantity, and a unit.");
      }
    },
    async saveIngredients() {
      if (!this.recipeId || this.recipeId <= 0) {
        alert("Recipe ID is missing or invalid. Please save the recipe first.");
        return;
      }
      try {
        const ingredientPromises = this.addedIngredients.map((ingredient) => {
          const payload = {
            recipe_id: this.recipeId,
            ingredient_id: ingredient.ingredient_id,
            quantity: ingredient.quantity,
            unit: ingredient.unit,
          };
          console.log("Sending payload to backend:", payload);
          return axios.post(
            `${import.meta.env.VITE_REMOTE_API}/recipes/link-ingredient`,
            payload
          );
        });
        await Promise.all(ingredientPromises);
        alert("Ingredients saved successfully!");
      } catch (error) {
        console.error("Error saving ingredients:", error);
        alert("Failed to save ingredients. Please try again.");
      }
    },
    removeIngredient(index) {
      this.addedIngredients.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.recipe-card {
  max-width: 800px;
  margin: auto;
  padding: 20px;
  border-radius: 10px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.title {
  text-align: center;
  color: #4d8c5e;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.input-field,
.textarea-field {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.textarea-field {
  resize: none;
}

.nav-button {
  display: inline-block;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #64a472;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.nav-button:hover {
  background-color: #4d8c5e;
}

.button-group {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.recipe-summary {
  background-color: #f3f3f3;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 5px;
}

@media (max-width: 768px) {
  .recipe-card {
    padding: 15px;
  }
}
</style>