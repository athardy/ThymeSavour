<template>
  <div class="grocery-list">
    <h1>Generate Grocery List</h1>
    <!-- Select Meal Plan -->
    <div class="form-group">
      <label for="meal-plan-select">Select a Meal Plan:</label>
      <select v-model="selectedMealPlanId" @change="fetchGroceryListByMealPlan" class="input-field">
  <option value="" disabled>Select a meal plan</option>
  <option
    v-for="mealPlan in mealPlans"
    :key="mealPlan.meal_plan_id"
    :value="mealPlan.meal_plan_id"
  >
    {{ mealPlan.meal_plan_name }}
  </option>
</select>
    </div>

    <!-- Display Grocery List -->
    <div v-if="groceryList.length > 0" class="recipe-ingredients">
      <h2>Grocery List</h2>
      <ul>
        <li
          v-for="(item, index) in groceryList"
          :key="index"
        >
          {{ item.total_quantity }} {{ item.unit }} - {{ item.ingredient_name }}
        </li>
      </ul>
      <button @click="printGroceryList" class="nav-button print-btn">Print Grocery List</button>
    </div>

    <div v-else-if="selectedMealPlanId">
      <p>No items found for this meal plan.</p>
    </div>
    <!-- Select Recipe -->
    <div class="form-group">
      <label for="recipe-select">Select a Recipe:</label>
      <select v-model="selectedRecipeId" @change="fetchRecipeDetails" class="input-field">
        <option value="" disabled>Select a recipe</option>
        <option
          v-for="recipe in recipes"
          :key="recipe.recipe_id"
          :value="recipe.recipe_id"
        >
          {{ recipe.recipe_name }}
        </option>
      </select>
    </div>

    <!-- Display Ingredients -->
    <div v-if="selectedRecipe && selectedRecipe.ingredients.length > 0" class="recipe-ingredients">
      <h2>Grocery List for {{ selectedRecipe.recipe_name }}</h2>
      <ul>
        <li
          v-for="(ingredient, index) in selectedRecipe.ingredients"
          :key="index"
        >
          {{ ingredient.quantity }} {{ ingredient.unit }} - {{ ingredient.ingredient_name }}
        </li>
      </ul>
      <button @click="printGroceryList" class="nav-button print-btn">Print Grocery List</button>
    </div>

    <div v-else-if="selectedRecipe">
      <p>This recipe has no ingredients listed.</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "GroceryListView",
  data() {
    return {
      recipes: [],
      selectedRecipeId: null,
      selectedRecipe: null,
      mealPlans: [],
      selectedMealPlanId: null,
      groceryList: [],
    };
  },
  methods: {
    async fetchGroceryListByMealPlan() {
  if (!this.selectedMealPlanId) {
    alert("Please select a meal plan first.");
    return;
  }

  try {
    const response = await axios.get(
      `${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/ingredients`,
      { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
    );

    if (response.data && Array.isArray(response.data)) {
      this.groceryList = response.data;
      console.log("Fetched grocery list:", this.groceryList);
    } else {
      console.warn("Unexpected response structure:", response.data);
      this.groceryList = [];
    }
  } catch (error) {
    console.error("Error fetching grocery list:", error.response?.data || error.message);
    alert("Failed to load grocery list. Please try again.");
  }
},
    async fetchMealPlans() {
  try {
    const response = await axios.get(
      `${import.meta.env.VITE_REMOTE_API}/meal-plans/all`,
      { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
    );

    if (response.data && Array.isArray(response.data)) {
      this.mealPlans = response.data.filter(
        (plan) => plan.user_id === this.$store.state.user.id
      );
    } else {
      console.warn("Unexpected response structure:", response.data);
    }
  } catch (error) {
    console.error("Error fetching meal plans:", error.response?.data || error.message);
    alert("Failed to load meal plans. Please try again.");
  }
    },
    async fetchRecipes() {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/recipes/my-recipes`,
          { params: { userId: this.$store.state.userId } }
        );
        this.recipes = response.data;
      } catch (error) {
        console.error("Error fetching recipes:", error);
        alert("Failed to load recipes. Please try again.");
      }
    },
    async fetchRecipeDetails() {
      if (!this.selectedRecipeId) return;

      try {
        const response = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/recipes/${this.selectedRecipeId}`
        );
        this.selectedRecipe = response.data;
      } catch (error) {
        console.error("Error fetching recipe details:", error);
        alert("Failed to load recipe details. Please try again.");
      }
    },
    printGroceryList() {
      const printContent = document.querySelector(".recipe-ingredients");
      const newWindow = window.open("", "_blank");
      newWindow.document.write(`<html><body>${printContent.innerHTML}</body></html>`);
      newWindow.print();
    },
  },
  mounted() {
    this.fetchMealPlans();
    this.fetchRecipes();
  },
};
</script>

<style scoped>
.grocery-list {
  max-width: 600px;
  margin: auto;
  text-align: center;
  font-family: Arial, sans-serif;
  padding: 2rem;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h1 {
  color: #4d8c5e;
  font-size: 2rem;
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 20px;
}

.input-field {
  padding: 10px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.recipe-ingredients {
  margin-top: 20px;
  text-align: left;
}

.recipe-ingredients ul {
  list-style: none;
  padding: 0;
}

.recipe-ingredients li {
  margin: 10px 0;
}

.nav-button {
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
</style>