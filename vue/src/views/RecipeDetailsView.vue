<template>
  <div class="recipe-details">
    <div class="recipe-card">
      <h2 class="recipe-title">
        <span v-if="!isEditing">{{ recipe.recipe_name }}</span>
        <span v-else>Edit Recipe</span>
      </h2>

      <!-- Edit Recipe Form -->
      <div v-if="isEditing" class="edit-recipe card-view">
        <form @submit.prevent="saveEditedRecipe" class="form">
          <!-- Recipe Details -->
          <div class="form-group">
            <label for="recipe_name">Recipe Name:</label>
            <input
              type="text"
              v-model="editRecipe.recipe_name"
              class="input-field"
              required
              placeholder="Enter recipe name"
            />
          </div>

          <div class="form-group">
            <label for="description">Description:</label>
            <textarea
              v-model="editRecipe.description"
              class="textarea-field"
              required
              placeholder="Enter recipe description"
            ></textarea>
          </div>

          <div class="form-group">
            <label for="instructions">Instructions:</label>
            <textarea
              v-model="editRecipe.instructions"
              class="textarea-field"
              required
              placeholder="Enter recipe instructions"
            ></textarea>
          </div>

          <!-- Display Attached Ingredients -->
          <div class="form-group attached-ingredients">
            <h3>Ingredients Attached to Recipe</h3>
            <ul>
              <li
                v-for="(ingredient, index) in recipe.ingredients"
                :key="ingredient.ingredient_id"
                class="ingredient-item"
              >
                {{ ingredient.ingredient_name }} - {{ ingredient.quantity }}
                {{ ingredient.unit }}
                <button
                  @click.prevent="removeIngredientFromRecipe(ingredient)"
                  class="remove-btn"
                >
                  🗑
                </button>
              </li>
            </ul>
          </div>

          <!-- Select Ingredient from Pantry Drawer -->
          <div class="form-group select-from-pantry">
            <h3 @click="toggleSelectIngredient" class="dropdown-toggle">
              Select Ingredient from Pantry <span>{{ isSelectingIngredient ? "▲" : "▼" }}</span>
            </h3>
            <div v-if="isSelectingIngredient" class="drawer">
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
                Add to Recipe
              </button>
            </div>
          </div>

          <!-- Add Ingredient to Pantry Drawer -->
          <div class="form-group add-to-pantry">
            <h3 @click="toggleAddIngredient" class="dropdown-toggle">
              Add Ingredient to Pantry <span>{{ isAddingIngredient ? "▲" : "▼" }}</span>
            </h3>
            <div v-if="isAddingIngredient" class="drawer">
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
          </div>

          <!-- Save and Cancel Buttons -->
          <div class="edit-button-group">
            <button type="submit" class="nav-button save-btn">Save Changes</button>
            <button @click="cancelEditing" class="nav-button cancel-btn">Cancel</button>
          </div>
        </form>
      </div>

      <!-- View Recipe Details -->
      <div v-else>
        <div class="print-content">
          <p><strong>Description:</strong> {{ recipe.description }}</p>
          <p><strong>Instructions:</strong> {{ recipe.instructions }}</p>
          <h3>Ingredients:</h3>
          <ul>
            <li v-for="(ingredient, index) in recipe.ingredients" :key="index">
              {{ ingredient.ingredient_name }} - {{ ingredient.quantity }}
              {{ ingredient.unit }}
            </li>
          </ul>
        </div>
      </div>

      <!-- Action Buttons -->
      <div v-if="!isEditing" class="action-buttons">
        <button @click="startEditing" class="nav-button edit-btn">Edit</button>
        <button @click="goBack" class="nav-button back-btn">Back</button>
        <button @click="printRecipe" class="nav-button print-btn">Print</button>
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
        recipe_id: null,
        recipe_name: "",
        description: "",
        instructions: "",
        ingredients: [],
      },
      editRecipe: null,
      isEditing: false,
      isSelectingIngredient: false,
      isAddingIngredient: false,
      availableIngredients: [],
      ingredientTypes: [],
      selectedIngredientId: null,
      selectedIngredientQuantity: 0,
      selectedIngredientUnit: "",
      newIngredient: { ingredient_name: "", unit: "", type_id: "" },
    };
  },
  created() {
    this.fetchRecipeDetails();
    this.fetchAvailableIngredients();
    this.fetchIngredientTypes();
  },
  methods: {
    startEditing() {
      this.isEditing = true;
      this.editRecipe = { ...this.recipe };
    },
    async fetchRecipeDetails() {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/recipes/${this.$route.params.id}`
        );
        this.recipe = response.data;
      } catch (error) {
        console.error("Error fetching recipe details:", error);
      }
    },
    async fetchAvailableIngredients() {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/ingredients/all`
        );
        this.availableIngredients = response.data.sort((a, b) =>
          a.ingredient_name.localeCompare(b.ingredient_name)
        );
      } catch (error) {
        console.error("Error fetching available ingredients:", error);
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
      }
    },
    toggleSelectIngredient() {
      this.isSelectingIngredient = !this.isSelectingIngredient;
    },
    toggleAddIngredient() {
      this.isAddingIngredient = !this.isAddingIngredient;
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
        await this.fetchAvailableIngredients();
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
          this.recipe.ingredients.push({
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
    async removeIngredientFromRecipe(ingredient) {
      try {
        await axios.delete(
          `${import.meta.env.VITE_REMOTE_API}/recipes/${this.recipe.recipe_id}/ingredients/${ingredient.ingredient_id}`
        );
        this.recipe.ingredients = this.recipe.ingredients.filter(
          (ing) => ing.ingredient_id !== ingredient.ingredient_id
        );
        alert("Ingredient removed successfully!");
      } catch (error) {
        console.error("Error removing ingredient:", error);
        alert("Failed to remove ingredient. Please try again.");
      }
    },
    async saveEditedRecipe() {
  try {
    // Log the payload before sending
    const payload = { ...this.editRecipe, ingredients: this.recipe.ingredients };
    console.log("Payload being sent:", payload);

    const response = await axios.put(
      `${import.meta.env.VITE_REMOTE_API}/recipes/${this.recipe.recipe_id}`,
      payload
    );

    alert("Recipe updated successfully!");
    this.isEditing = false;
  } catch (error) {
    console.error("Error saving edited recipe:", error);
    alert("Failed to save changes. Please try again.");
  }
},
    cancelEditing() {
      this.isEditing = false;
      this.editRecipe = null;
    },
    goBack() {
      this.$router.push("/my-recipes");
    },
    printRecipe() {
      const printContent = document.querySelector(".print-content");
      const newWindow = window.open("", "_blank");
      newWindow.document.write(`<html><body>${printContent.innerHTML}</body></html>`);
      newWindow.print();
    },
    async saveEditedRecipe() {
  const payload = {
    recipe_name: this.editRecipe.recipe_name,
    description: this.editRecipe.description,
    instructions: this.editRecipe.instructions,
  };
  try {
    await axios.put(`${import.meta.env.VITE_REMOTE_API}/recipes/${this.recipe.recipe_id}`, payload);
    alert("Recipe updated successfully!");
    this.isEditing = false; // Exit editing mode
  } catch (error) {
    console.error("Error saving edited recipe:", error);
    alert("Failed to save changes. Please try again.");
  }
},
async saveIngredients() {
  const ingredientPromises = this.recipe.ingredients.map((ingredient) => {
    const payload = {
      recipe_id: this.recipe.recipe_id,
      ingredient_id: ingredient.ingredient_id,
      quantity: ingredient.quantity,
      unit: ingredient.unit,
    };
    return axios.post(`${import.meta.env.VITE_REMOTE_API}/recipes/link-ingredient`, payload);
  });
  try {
    await Promise.all(ingredientPromises);
    alert("Ingredients updated successfully!");
  } catch (error) {
    console.error("Error updating ingredients:", error);
    alert("Failed to update ingredients. Please try again.");
  }
},

  },
};
</script>

<style scoped>
.card-view {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.attached-ingredients ul {
  list-style-type: none;
  padding: 0;
}

.ingredient-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 0;
}

.remove-btn {
  background: transparent;
  border: none;
  color: red;
  cursor: pointer;
}

.nav-button {
  padding: 10px 20px;
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

.card-view {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  max-width: 600px; /* Adjust card width */
  margin: auto;
}

.ingredient-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 0;
}

.remove-btn {
  margin-left: 10px; /* Bring closer to the ingredient */
  background: transparent;
  border: none;
  color: red;
  cursor: pointer;
}

.textarea-field {
  resize: none; /* Make textareas non-resizable */
}

.form-group {
  margin-bottom: 20px; /* Consistent spacing */
}

.edit-button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>