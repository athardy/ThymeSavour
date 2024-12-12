<template>
  <div class="recipe-creation">
    <div class="recipe-card">
      <h2>Create a New Recipe</h2>
      <form @submit.prevent="createRecipe">
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

        <div class="form-group">
          <h3>Ingredients:</h3>
          <div v-for="(ingredient, index) in newRecipe.ingredients" :key="index" class="ingredient-row">
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
              placeholder="Unit (e.g., grams, cups)"
              class="input-field"
              required
            />
            <button @click.prevent="removeIngredient(index)" class="remove-btn">Remove</button>
          </div>
          <button @click.prevent="addIngredient" class="add-ingredient-btn">Add Ingredient</button>
        </div>

        <div class="button-group">
          <button type="submit" class="save-btn">Save Recipe</button>
          <router-link to="/my-recipes" class="cancel-btn">Cancel</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      newRecipe: {
        recipe_name: "",
        description: "",
        instructions: "",
        ingredients: [],
      },
    };
  },
  methods: {
    createRecipe() {
      // Post the new recipe to the server
      axios
        .post(`${import.meta.env.VITE_REMOTE_API}/recipes`, this.newRecipe)
        .then(() => {
          this.$router.push("/my-recipes");
        })
        .catch((error) => {
          console.error("Error creating recipe:", error);
        });
    },
    addIngredient() {
      this.newRecipe.ingredients.push({
        ingredient_name: "",
        quantity: 0,
        unit: "",
      });
    },
    removeIngredient(index) {
      this.newRecipe.ingredients.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.recipe-creation {
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

h2 {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.input-field,
.textarea-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

textarea {
  resize: vertical;
}

.ingredient-row {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 10px;
}

.remove-btn {
  background-color: #f8d7da;
  color: #721c24;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
}

.add-ingredient-btn {
  background-color: #28a745;
  color: #fff;
  border: none;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.button-group {
  display: flex;
  gap: 10px;
}

.save-btn {
  background-color: #28a745;
  color: #fff;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
}

.cancel-btn {
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px 15px;
  border-radius: 5px;
  text-align: center;
  text-decoration: none;
}

button:hover,
.cancel-btn:hover {
  opacity: 0.9;
}
</style>