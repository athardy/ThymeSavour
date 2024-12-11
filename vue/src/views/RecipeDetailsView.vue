<template>
  <div class="recipe-details">
    <div class="recipe-card">
      <h2>{{ recipe.recipe_name }}</h2>

      <!-- If in editing mode, show form for editing, otherwise show static details -->
      <div v-if="isEditing">
        <form @submit.prevent="updateRecipe">
          <div class="form-group">
            <label for="recipe_name">Recipe Name:</label>
            <input type="text" v-model="recipe.recipe_name" class="input-field" required />
          </div>

          <div class="form-group">
            <label for="description">Description:</label>
            <textarea v-model="recipe.description" class="textarea-field" required></textarea>
          </div>

          <div class="form-group">
            <label for="instructions">Instructions:</label>
            <textarea v-model="recipe.instructions" class="textarea-field" required></textarea>
          </div>

          <div class="form-group">
            <h3>Ingredients:</h3>
            <div v-for="(ingredient, index) in recipe.ingredients" :key="index" class="ingredient-row">
              <input type="text" v-model="ingredient.ingredient_name" placeholder="Ingredient name" class="input-field" required />
              <input type="number" v-model="ingredient.quantity" placeholder="Quantity" class="input-field" required />
              <input type="text" v-model="ingredient.unit" placeholder="Unit" class="input-field" required />
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

      <!-- Otherwise, display recipe details -->
      <div v-else>
        <p class="description">{{ recipe.description }}</p>
        <p class="instructions"><strong>Instructions:</strong> {{ recipe.instructions }}</p>

        <div class="ingredients">
          <h3>Ingredients:</h3>
          <ul v-if="recipe.ingredients.length > 0">
            <li v-for="(ingredient, index) in recipe.ingredients" :key="index">
              {{ ingredient.ingredient_name }} - {{ ingredient.quantity }} {{ ingredient.unit }}
            </li>
          </ul>
          <p v-else>No ingredients available for this recipe.</p>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="action-buttons" v-if="!isEditing">
        <button @click="printRecipe" class="print-button">Print Recipe</button>
        <button @click="printGroceryList" class="print-button">Print Grocery List</button>
        <button @click="startEditing" class="edit-button">Edit Recipe</button>
        <button @click="deleteRecipe" class="delete-button">Delete Recipe</button>
      </div>

      <!-- Back Button -->
      <div class="back-button-container">
        <button @click="goBack" class="back-button">Back</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      recipe: {
        recipe_id: 0,
        recipe_name: '',
        description: '',
        instructions: '',
        ingredients: [],
      },
      isEditing: false,
    };
  },
  created() {
    this.fetchRecipeDetails();
  },
  methods: {
    fetchRecipeDetails() {
      const recipeId = this.$route.params.id;
      axios
        .get(`/recipes/${recipeId}`)
        .then((response) => {
          this.recipe = response.data;
        })
        .catch((error) => {
          console.error("Error fetching recipe details:", error);
        });
    },
    startEditing() {
      this.isEditing = true;
    },
    cancelEditing() {
      this.isEditing = false;
      this.fetchRecipeDetails(); // Reload recipe to reset changes
    },
    updateRecipe() {
      const recipeId = this.recipe.recipe_id;
      axios
        .put(`/recipes/${recipeId}`, this.recipe)
        .then(() => {
          alert('Recipe updated successfully!');
          this.isEditing = false;
        })
        .catch((error) => {
          console.error('Error updating recipe:', error);
          alert('Failed to update recipe.');
        });
    },
    addIngredient() {
      this.recipe.ingredients.push({
        ingredient_name: '',
        quantity: 0,
        unit: '',
      });
    },
    removeIngredient(index) {
      this.recipe.ingredients.splice(index, 1);
    },
    printRecipe() {
      const recipeDetails = `
        <h1>Thyme Savour</h1>
        <h2>${this.recipe.recipe_name}</h2>
        <p><strong>Description:</strong> ${this.recipe.description}</p>
        <p><strong>Instructions:</strong> ${this.recipe.instructions}</p>
        <h3>Ingredients:</h3>
        <ul>
          ${this.recipe.ingredients.map(
            (ingredient) => `<li>${ingredient.ingredient_name}: ${ingredient.quantity} ${ingredient.unit}</li>`
          ).join('')}
        </ul>
      `;

      const newWindow = window.open();
      newWindow.document.write(`
        <html>
          <head>
            <title>${this.recipe.recipe_name}</title>
            <style>
              body {
                font-family: Arial, Helvetica, sans-serif;
                background-color: #D3F1DF; /* Matches app background */
                color: #333;
                margin: 20px;
              }
              h1 {
                color: #64A472; /* Matches app button color */
                text-align: center;
              }
              h2 {
                color: #85A98F; /* Matches header color */
                text-align: center;
              }
              p {
                font-size: 1.1rem;
                margin-bottom: 10px;
              }
              ul {
                font-size: 1.1rem;
                list-style-type: none;
                padding: 0;
              }
              li {
                margin-bottom: 5px;
              }
              .footer {
                text-align: center;
                color: #64A472;
                font-size: 0.8rem;
                margin-top: 30px;
              }
            </style>
          </head>
          <body>
            ${recipeDetails}
            <div class="footer">
              <p>&copy; 2024 Thyme Savour. All Rights Reserved.</p>
            </div>
          </body>
        </html>
      `);
      newWindow.document.close();
      newWindow.print();
    },
    printGroceryList() {
      const groceryList = this.recipe.ingredients.map(
        (ingredient) =>
          `${ingredient.ingredient_name}: ${ingredient.quantity} ${ingredient.unit}`
      ).join('\n');

      const newWindow = window.open();
      
      // Add header with app name and recipe name
      newWindow.document.write(`
        <html>
          <head>
            <title>Grocery List</title>
            <style>
              body {
                font-family: Arial, Helvetica, sans-serif;
                background-color: #D3F1DF; /* Matches app background */
                color: #333;
                margin: 20px;
              }
              h1 {
                color: #64A472; /* Matches app button color */
                text-align: center;
              }
              h2 {
                color: #85A98F; /* Matches header color */
                text-align: center;
              }
              pre {
                font-size: 1.1rem;
                white-space: pre-wrap;
                word-wrap: break-word;
                margin: 20px 0;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 5px;
                background-color: #fff;
              }
              .footer {
                text-align: center;
                color: #64A472;
                font-size: 0.8rem;
                margin-top: 30px;
              }
            </style>
          </head>
          <body>
            <h1>Thyme Savour</h1>
            <h2>Grocery List for: ${this.recipe.recipe_name}</h2>
            <pre>${groceryList}</pre>
            <div class="footer">
              <p>&copy; 2024 Thyme Savour. All Rights Reserved.</p>
            </div>
          </body>
        </html>
      `);
      
      newWindow.document.close();
      newWindow.print();
    },
    deleteRecipe() {
      // Show a confirmation dialog before proceeding with the deletion
      const confirmed = window.confirm('Are you sure you want to delete this recipe? This action cannot be undone.');

      if (confirmed) {
        const recipeId = this.recipe.recipe_id;
        axios
          .delete(`/recipes/${recipeId}`)
          .then(() => {
            alert('Recipe deleted successfully!');
            this.$router.push('/my-recipes'); // Redirect back to the recipes list
          })
          .catch((error) => {
            console.error('Error deleting recipe:', error);
            alert('Failed to delete recipe.');
          });
      } else {
        alert('Recipe deletion canceled.');
      }
    },
    goBack() {
      this.$router.push('/my-recipes'); // Go back to the my-recipes page
    }
  },
};
</script>

<style scoped>
.recipe-card {
  background-color: #f9f9f9;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 1rem;
}

.input-field,
.textarea-field {
  width: 100%;
  padding: 0.8rem;
  margin-top: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.add-ingredient-btn,
.remove-btn,
.save-btn,
.cancel-btn,
.print-button,
.edit-button,
.delete-button,
.back-button {
  padding: 0.8rem 1.2rem;
  border: none;
  border-radius: 5px;
  background-color: #64a472;
  color: white;
  font-size: 1rem;
  cursor: pointer;
  margin: 5px;
}

.add-ingredient-btn {
  background-color: #ff9a9a;
}

.remove-btn {
  background-color: #ff7f7f;
}

.print-button {
  background-color: #85a98f;
}

.print-button:hover,
.save-btn:hover,
.cancel-btn:hover,
.edit-button:hover,
.delete-button:hover,
.back-button:hover {
  background-color: #4d8c5e;
}

.print-button:active,
.save-btn:active,
.cancel-btn:active,
.edit-button:active,
.delete-button:active,
.back-button:active {
  background-color: #3b6b46;
}

footer {
  margin-top: 20px;
}
</style>