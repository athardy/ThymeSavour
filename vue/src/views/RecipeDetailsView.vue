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
            <button type="submit" @click="saveIngredients" class="nav-button save-btn">Save Changes</button>
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
        const newIngredient = {
          ...ingredient,
          quantity: this.selectedIngredientQuantity,
          unit: this.selectedIngredientUnit,
        };
        console.log("Adding ingredient to recipe:", newIngredient);
        this.recipe.ingredients.push(newIngredient);
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
    if (!this.recipe.ingredients || this.recipe.ingredients.length === 0) {
      alert("No ingredients to save.");
      return;
    }

    for (const ingredient of this.recipe.ingredients) {
      const payload = {
        recipe_id: this.recipe.recipe_id,
        ingredient_id: ingredient.ingredient_id,
        quantity: ingredient.quantity,
        unit: ingredient.unit,
      };

      console.log("Saving ingredient:", payload);

      try {
        const response = await axios.post(
          `${import.meta.env.VITE_REMOTE_API}/recipes/link-ingredient`,
          payload,
          { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
        );
        console.log("Ingredient save response:", response.data);
      } catch (error) {
        console.error(
          "Error saving ingredient:",
          error.response?.data || error.message
        );
      }
    }

    alert("Ingredients saved successfully!");
  },

  },
};
</script>

<style scoped>
/* Recipe Card Container */
.recipe-card {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin: 20px auto;
  max-width: 800px;
}

/* Recipe Title */
.recipe-title {
  font-size: 1.8rem;
  font-family: 'Poppins', sans-serif;
  color: #3b5b3b;
  margin-bottom: 15px;
  text-align: center;
}

/* Form Styling */
.form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-field,
.textarea-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #d3d3d3;
  border-radius: 5px;
  font-size: 1rem;
  font-family: 'Poppins', sans-serif;
}

.input-field:focus,
.textarea-field:focus {
  border-color: #64a472;
  outline: none;
}

/* Ingredients List */
.attached-ingredients {
  margin-top: 20px;
}

.attached-ingredients h3 {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
  color: #3b5b3b;
}

.ingredient-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 10px;
  margin: 5px 0;
  background-color: #f9f9f9;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.ingredient-item button {
  background: none;
  border: none;
  color: #d9534f;
  cursor: pointer;
  font-size: 1.2rem;
}

/* Drawers */
.drawer {
  background-color: #f7f7f7;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  margin-top: 10px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
}

.dropdown-toggle {
  font-size: 1rem;
  font-weight: bold;
  color: #3b5b3b;
  cursor: pointer;
  display: inline-block;
  margin-bottom: 10px;
}

/* Action Buttons Group */
.action-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.nav-button {
  background-color: #64a472;
  color: white;
  padding: 10px 20px;
  font-size: 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.nav-button:hover {
  background-color: #4d8c5e;
}

/* Specific Button Styles */
.nav-button.print-btn {
  background-color: #64a472; /* Same green */
}

.nav-button.edit-btn {
  background-color: #64a472; /* Same green */
}

.nav-button.back-btn {
  background-color: #64a472; /* Same green */
}

/* Print Section */
.print-content {
  font-size: 1rem;
  color: #333;
}

.print-content ul {
  list-style-type: none;
  padding: 0;
}

.print-content li {
  margin-bottom: 5px;
}

/* Edit Button Group */
.edit-button-group {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  margin-top: 20px;
}

/* Responsive Design */
@media (max-width: 768px) {
  .recipe-card {
    padding: 15px;
  }

  .form {
    gap: 15px;
  }

  .nav-button {
    padding: 8px 15px;
    font-size: 0.9rem;
  }
}
</style>