<template>
    <div class="meal-plan-view">
      <h1>Meal Plan Manager</h1>
  
      <!-- Dropdown for Meal Plans -->
      <div class="dropdown">
        <label for="meal-plan-select">Select a Meal Plan:</label>
        <select
          id="meal-plan-select"
          v-model="selectedMealPlan"
          @change="fetchMealsForSelectedPlan"
        >
          <option disabled value="">Please select</option>
          <option v-for="plan in mealPlans" :key="plan.meal_plan_id" :value="plan">
            {{ plan.meal_plan_name }}
          </option>
        </select>
      </div>
  
      <!-- Details Section -->
      <div v-if="selectedMealPlan" class="meal-plan-details">
        <h2>Meal Plan Details</h2>
        <p><strong>Start Date:</strong> {{ selectedMealPlan.start_date }}</p>
        <p><strong>End Date:</strong> {{ selectedMealPlan.end_date }}</p>
  
        <h3>Assigned Meals (Calendar View)</h3>
        <div v-if="meals.length">
          <table class="calendar-view">
            <thead>
              <tr>
                <th>Date</th>
                <th>Breakfast</th>
                <th>Lunch</th>
                <th>Dinner</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="date in uniqueMealDates" :key="date">
                <td>{{ formatDate(date) }}</td>
                <td>
                  {{ getMealByType(date, 'Breakfast') }}
                  <button v-if="getMealIdByType(date, 'Breakfast')" @click="deleteMeal(getMealIdByType(date, 'Breakfast'))">
                    Delete
                  </button>
                </td>
                <td>
                  {{ getMealByType(date, 'Lunch') }}
                  <button v-if="getMealIdByType(date, 'Lunch')" @click="deleteMeal(getMealIdByType(date, 'Lunch'))">
                    Delete
                  </button>
                </td>
                <td>
                  {{ getMealByType(date, 'Dinner') }}
                  <button v-if="getMealIdByType(date, 'Dinner')" @click="deleteMeal(getMealIdByType(date, 'Dinner'))">
                    Delete
                  </button>
                </td>
                <td>
                  <button @click="addRandomMeal">Add Random Meal</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else>
          <p>No meals assigned to this plan.</p>
        </div>
  
        <!-- Add Meal to Plan -->
        <div class="add-meal-section">
          <h3>Add a Meal to Plan</h3>
          <form @submit.prevent="addMealToPlan">
            <label for="meal-type">Meal Type:</label>
            <select id="meal-type" v-model="newMealType" required>
              <option disabled value="">Select Meal Type</option>
              <option>Breakfast</option>
              <option>Lunch</option>
              <option>Dinner</option>
            </select>
  
            <label for="recipe-select">Select Recipe:</label>
            <select id="recipe-select" v-model="newRecipeId" required>
              <option disabled value="">Select Recipe</option>
              <option v-for="recipe in recipes" :key="recipe.recipe_id" :value="recipe.recipe_id">
                {{ recipe.recipe_name }}
              </option>
            </select>
  
            <label for="meal-date">Date:</label>
            <input type="date" id="meal-date" v-model="newMealDate" required />
  
            <button type="submit">Add Meal</button>
          </form>
        </div>
      </div>
      <div v-else>
        <p>Please select a meal plan to see details.</p>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    name: "MealPlanView",
    data() {
      return {
        mealPlans: [], // Holds the meal plans fetched from the server
        selectedMealPlan: null, // Tracks the selected meal plan
        meals: [], // Meals for the selected plan
        recipes: [], // Recipes available to the user
        newMealType: "", // Meal type for new meal
        newRecipeId: "", // Recipe ID for new meal
        newMealDate: "", // Date for new meal
      };
    },
    computed: {
      // Extract unique meal dates
      uniqueMealDates() {
        const dates = this.meals.map((meal) => meal.meal_date);
        return [...new Set(dates)].sort();
      },
    },
    methods: {
      fetchMealPlans() {
        axios
          .get("/meal-plans/all")
          .then((response) => {
            this.mealPlans = response.data;
          })
          .catch((error) => {
            console.error("Error fetching meal plans:", error);
          });
      },
      fetchMealsForSelectedPlan() {
        if (!this.selectedMealPlan) return;
  
        axios
          .get(`/meal-plans/${this.selectedMealPlan.meal_plan_id}/meals`)
          .then((response) => {
            this.meals = response.data;
            this.fetchRecipes();
          })
          .catch((error) => {
            console.error("Error fetching meals for selected plan:", error);
          });
      },
      fetchRecipes() {
        axios
          .get("/recipes/all")
          .then((response) => {
            this.recipes = response.data;
          })
          .catch((error) => {
            console.error("Error fetching recipes:", error);
          });
      },
      formatDate(date) {
        const options = { year: "numeric", month: "short", day: "numeric" };
        return new Date(date).toLocaleDateString(undefined, options);
      },
      getMealByType(date, type) {
        const meal = this.meals.find(
          (m) => m.meal_date === date && m.meal_type === type
        );
        const recipe = this.recipes.find((r) => r.recipe_id === meal?.recipe_id);
        return recipe ? recipe.recipe_name : "—";
      },
      getMealIdByType(date, type) {
        const meal = this.meals.find(
          (m) => m.meal_date === date && m.meal_type === type
        );
        return meal ? meal.meal_id : null;
      },
      addRandomMeal() {
        if (!this.selectedMealPlan) return;
  
        axios
          .post(`/meal-plans/${this.selectedMealPlan.meal_plan_id}/add-random-recipe`)
          .then(() => {
            this.fetchMealsForSelectedPlan();
          })
          .catch((error) => {
            console.error("Error adding random meal:", error);
          });
      },
      addMealToPlan() {
        if (!this.selectedMealPlan || !this.newMealType || !this.newRecipeId || !this.newMealDate) return;
  
        const newMeal = {
          meal_plan_id: this.selectedMealPlan.meal_plan_id,
          recipe_id: this.newRecipeId,
          meal_date: this.newMealDate,
          meal_type: this.newMealType,
        };
  
        axios
          .post(`/meal-plans/${this.selectedMealPlan.meal_plan_id}/meals`, newMeal)
          .then(() => {
            this.fetchMealsForSelectedPlan();
            this.newMealType = "";
            this.newRecipeId = "";
            this.newMealDate = "";
          })
          .catch((error) => {
            console.error("Error adding meal to plan:", error);
          });
      },
      deleteMeal(mealId) {
        if (!mealId) return;
        axios
          .delete(`/meal-plans/meals/delete/${mealId}`)
          .then(() => {
            this.fetchMealsForSelectedPlan(); // Refresh meals
          })
          .catch((error) => {
            console.error("Error deleting meal:", error);
          });
      },
    },

    //hey, uuuh, what do you think you are doing, my man. this is really cool.
    mounted() {
      this.fetchMealPlans();
    },
  };
  </script>
  
  <style scoped>
  .meal-plan-view {
    max-width: 800px;
    margin: 0 auto;
  }
  
  .dropdown {
    margin-bottom: 1rem;
  }
  
  .dropdown select {
    width: 100%;
    padding: 0.5rem;
    font-size: 1rem;
    margin-top: 0.5rem;
  }
  
  .meal-plan-details {
    margin-top: 2rem;
    background: #f9f9f9;
    padding: 1rem;
    border-radius: 8px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  }
  
  .calendar-view {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }
  
  .calendar-view th,
  .calendar-view td {
    border: 1px solid #ddd;
    padding: 0.5rem;
    text-align: left;
  }
  
  .calendar-view th {
    background-color: #f4f4f4;
  }
  
  .calendar-view tr:nth-child(even) {
    background-color: #f9f9f9;
  }
  
  .actions {
    margin-top: 1rem;
    display: flex;
    justify-content: space-between;
  }
  
  .actions button {
    padding: 0.5rem 1rem;
    font-size: 1rem;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .actions button:hover {
    background-color: #0056b3;
  }
  </style>