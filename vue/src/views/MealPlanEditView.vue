<template>
    <div class="meal-plan-edit">
      <h2>Edit Meal Plan</h2>
  
      <!-- Meal Plan Form -->
      <form @submit.prevent="updateMealPlan">
        <div class="form-group">
          <label for="meal-plan-name">Meal Plan Name:</label>
          <input 
            type="text" 
            id="meal-plan-name" 
            v-model="mealPlan.name"
            placeholder="Enter meal plan name"
          />
        </div>
        <div class="form-group">
          <label for="start-date">Start Date:</label>
          <input 
            type="date" 
            id="start-date" 
            v-model="mealPlan.start_date"
            required
          />
        </div>
        <div class="form-group">
          <label for="end-date">End Date:</label>
          <input 
            type="date" 
            id="end-date" 
            v-model="mealPlan.end_date"
            required
          />
        </div>
  
        <!-- Calendar View of Meal Plan -->
        <div v-if="mealPlan.meals.length > 0" class="calendar-view">
          <h3>Meals</h3>
          <table>
            <thead>
              <tr>
                <th>Meal Date</th>
                <th>Meal Type</th>
                <th>Recipe</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(meal, index) in mealPlan.meals" :key="meal.meal_id">
                <td>{{ meal.meal_date }}</td>
                <td>{{ meal.meal_type }}</td>
                <td>{{ meal.recipe_name }}</td>
                <td>
                  <button @click="deleteMeal(meal.meal_id)" class="delete-button">Delete</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
  
        <!-- Add Meal Form Toggle -->
        <div class="add-meal">
          <button @click="toggleAddMealForm" class="add-meal-button">Add Meal</button>
          <div v-if="showAddMealForm" class="add-meal-form">
            <select v-model="selectedRecipeId">
              <option v-for="recipe in recipes" :key="recipe.recipe_id" :value="recipe.recipe_id">
                {{ recipe.recipe_name }}
              </option>
            </select>
            <input type="date" v-model="selectedMealDate" required />
            <select v-model="selectedMealType" required>
              <option value="Breakfast">Breakfast</option>
              <option value="Lunch">Lunch</option>
              <option value="Dinner">Dinner</option>
              <option value="Snack">Snack</option>
            </select>
            <button @click="addMeal" class="add-meal-btn">Add Meal</button>
          </div>
        </div>
  
        <!-- Submit Button -->
        <div>
          <button type="submit" class="save-button">Save Changes</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        mealPlan: {
          name: "",
          start_date: "",
          end_date: "",
          meals: [],
        },
        showAddMealForm: false,
        selectedRecipeId: null,
        selectedMealDate: "",
        selectedMealType: "Breakfast",
        recipes: [], // List of available recipes for meal plan
      };
    },
    methods: {
      toggleAddMealForm() {
        this.showAddMealForm = !this.showAddMealForm;
      },
      addMeal() {
        // Check if the selected fields are valid
        if (!this.selectedRecipeId || !this.selectedMealDate) {
          alert("Please fill in all fields to add a meal.");
          return;
        }
  
        const newMeal = {
          recipe_id: this.selectedRecipeId,
          meal_date: this.selectedMealDate,
          meal_type: this.selectedMealType,
        };
  
        // Optionally, send the new meal data to the server
        axios.post(`/meal-plans/${this.mealPlan.id}/meals`, newMeal)
          .then((response) => {
            // On success, push the new meal to the mealPlan.meals array
            this.mealPlan.meals.push(response.data);
            this.clearAddMealForm();
          })
          .catch((error) => {
            console.error("Error adding meal:", error);
            alert("Failed to add meal.");
          });
      },
      deleteMeal(mealId) {
        // Send a request to delete the meal
        axios.delete(`/meal-plans/${this.mealPlan.id}/meals/${mealId}`)
          .then(() => {
            // On success, remove the meal from the local array
            this.mealPlan.meals = this.mealPlan.meals.filter(meal => meal.meal_id !== mealId);
          })
          .catch((error) => {
            console.error("Error deleting meal:", error);
            alert("Failed to delete meal.");
          });
      },
      updateMealPlan() {
        // Make sure to validate and submit the updated meal plan to the backend
        axios.put(`/meal-plans/${this.mealPlan.id}`, this.mealPlan)
          .then(() => {
            alert("Meal Plan updated successfully!");
            this.$router.push({ name: "meal-plan-list" });
          })
          .catch((error) => {
            console.error("Error updating meal plan:", error);
            alert("Failed to update meal plan.");
          });
      },
      clearAddMealForm() {
        // Reset the add meal form
        this.selectedRecipeId = null;
        this.selectedMealDate = "";
        this.selectedMealType = "Breakfast";
        this.showAddMealForm = false;
      },
    },
    mounted() {
      // Fetch the recipes and meal plan data when the component is mounted
      const mealPlanId = this.$route.params.id;
  
      // Fetch the meal plan details
      axios.get(`/meal-plans/${mealPlanId}`)
        .then((response) => {
          this.mealPlan = response.data;
        })
        .catch((error) => {
          console.error("Error fetching meal plan:", error);
        });
  
      // Fetch the list of recipes
      axios.get(`/recipes`)
        .then((response) => {
          this.recipes = response.data;
        })
        .catch((error) => {
          console.error("Error fetching recipes:", error);
        });
    },
  };
  </script>
  
  <style scoped>
  .meal-plan-edit {
    max-width: 600px;
    margin: auto;
    padding: 1rem;
  }
  .form-group {
    margin-bottom: 1rem;
  }
  .add-meal-button {
    margin-top: 1rem;
  }
  .add-meal-form {
    margin-top: 1rem;
  }
  .save-button {
    margin-top: 2rem;
    background-color: #4CAF50;
    color: white;
    padding: 0.5rem 1rem;
    border: none;
    cursor: pointer;
  }
  .delete-button {
    background-color: red;
    color: white;
    padding: 0.5rem;
    border: none;
    cursor: pointer;
  }
  </style>