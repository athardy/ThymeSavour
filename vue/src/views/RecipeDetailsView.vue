<template>
  <div class="recipe-details">
    <div class="recipe-card">
      <h2 class="recipe-title">{{ recipe.recipe_name }}</h2>

      <!-- Editing Mode -->
      <div v-if="isEditing" class="edit-recipe">
        <form @submit.prevent="updateRecipe">
          <div class="form-group">
            <label for="recipe_name">Recipe Name:</label>
            <input type="text" v-model="editRecipe.recipe_name" class="input-field" required />
          </div>

          <div class="form-group">
            <label for="description">Description:</label>
            <textarea v-model="editRecipe.description" class="textarea-field" required></textarea>
          </div>

          <div class="form-group">
            <label for="instructions">Instructions:</label>
            <textarea v-model="editRecipe.instructions" class="textarea-field" required></textarea>
          </div>

          <div class="form-group">
            <h3>Ingredients:</h3>
            <div v-for="(ingredient, index) in editRecipe.ingredients" :key="index" class="ingredient-row">
              <input
                type="text"
                v-model="ingredient.ingredient_name"
                placeholder="Ingredient name"
                class="input-field"
                required
              />
              <input
                type="number"
                v-model="ingredient.quantity"
                placeholder="Quantity"
                class="input-field"
                required
              />
              <input
                type="text"
                v-model="ingredient.unit"
                placeholder="Unit"
                class="input-field"
                required
              />
              <button @click="removeIngredient(index)" class="remove-btn">Remove</button>
            </div>
            <button @click="addIngredient" class="add-ingredient-btn">Add Ingredient</button>
          </div>

          <div class="button-group">
            <button type="submit" class="save-btn">Save Changes</button>
            <button @click="cancelEditing" class="cancel-btn">Cancel</button>
          </div>
        </form>
      </div>

      <!-- View Mode -->
      <div v-else>
        <p class="recipe-description">{{ recipe.description }}</p>
        <p class="recipe-instructions"><strong>Instructions:</strong> {{ recipe.instructions }}</p>

        <div class="ingredients">
          <h3>Ingredients:</h3>
          <ul>
            <li v-for="(ingredient, index) in recipe.ingredients" :key="index">
              {{ ingredient.ingredient_name }} - {{ ingredient.quantity }} {{ ingredient.unit }}
            </li>
          </ul>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="action-buttons">
        <button @click="startEditing" class="edit-button">Edit</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      recipe: {
        recipe_id: 0,
        recipe_name: "",
        description: "",
        instructions: "",
        ingredients: [],
      },
      editRecipe: null,
      isEditing: false,
    };
  },
  created() {
    this.fetchRecipeDetails();
  },
  methods: {
    fetchRecipeDetails() {
      axios
        .get(`/recipes/${this.$route.params.id}`)
        .then((response) => {
          this.recipe = response.data;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    startEditing() {
      this.editRecipe = JSON.parse(JSON.stringify(this.recipe)); // Deep copy
      this.isEditing = true;
    },
    cancelEditing() {
      this.editRecipe = null; // Clear the editing copy
      this.isEditing = false;
    },
    updateRecipe() {
      axios
        .put(`/recipes/${this.editRecipe.recipe_id}`, this.editRecipe)
        .then(() => {
          this.recipe = JSON.parse(JSON.stringify(this.editRecipe));
          this.isEditing = false;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    addIngredient() {
      this.editRecipe.ingredients.push({
        ingredient_name: "",
        quantity: 0,
        unit: "",
      });
    },
    removeIngredient(index) {
      this.editRecipe.ingredients.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.recipe-details {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #f9f9f9;
  min-height: 100vh;
}

.recipe-card {
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 800px;
}

.recipe-title {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 20px;
}

.recipe-description,
.recipe-instructions {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 20px;
}

.ingredients ul {
  list-style: none;
  padding: 0;
}

.ingredients li {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  font-size: 1rem;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.edit-button,
.save-btn,
.cancel-btn {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.edit-button {
  background-color: #007bff;
  color: #fff;
}

.save-btn {
  background-color: #28a745;
  color: #fff;
}

.cancel-btn {
  background-color: #dc3545;
  color: #fff;
}

.edit-button:hover,
.save-btn:hover,
.cancel-btn:hover {
  opacity: 0.9;
}
</style>