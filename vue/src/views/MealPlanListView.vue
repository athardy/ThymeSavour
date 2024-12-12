<template>
  <div class="meal-plan-list">
    <h1>Meal Plans</h1>

    <!-- Create Meal Plan Form -->
    <div class="create-meal-plan">
      <h2>Create a New Meal Plan</h2>
      <form @submit.prevent="createMealPlan">
        <input
          type="text"
          v-model="newMealPlanName"
          placeholder="Meal Plan Name"
          required
        />
        <input
          type="date"
          v-model="newMealPlanStartDate"
          required
        />
        <input
          type="date"
          v-model="newMealPlanEndDate"
          required
        />
        <button type="submit" class="nav-button action-button">
          Create Meal Plan
        </button>
      </form>
    </div>

    <!-- Meal Plan Dropdown -->
    <div class="meal-plan-dropdown">
      <label for="meal-plan" class="dropdown-label">Select a Meal Plan:</label>
      <select v-model="selectedMealPlanId" @change="fetchMealPlanDetails" class="dropdown">
        <option value="" disabled>Select a meal plan</option>
        <option v-for="mealPlan in mealPlans" :key="mealPlan.meal_plan_id" :value="mealPlan.meal_plan_id">
          {{ mealPlan.meal_plan_name }}
        </option>
      </select>
    </div>

    <!-- Randomly Fill Meals Button -->
    <button v-if="selectedMealPlanId" @click="randomlyFillMeals" class="nav-button random-fill-button">
      Randomly Fill Meals
    </button>

    <!-- Meal Plan Action Buttons -->
    <div v-if="selectedMealPlanId" class="actions">
      <button @click="generateGroceryList" class="nav-button action-button">Generate Grocery List</button>
      <button @click="shareMealPlan" class="nav-button action-button">Share Meal Plan</button>
      <button @click="printMealPlan" class="nav-button action-button">Print Meal Plan</button>
    </div>

    <!-- Add Meal Form -->
    <div v-if="selectedMealPlanId && allRecipes.length" class="add-meal">
      <h3>Add a Meal</h3>
      <form @submit.prevent="addMealToPlan">
        <select v-model="selectedRecipeId" required>
          <option disabled value="">Select a recipe</option>
          <option v-for="recipe in allRecipes" :key="recipe.recipe_id" :value="recipe.recipe_id">
            {{ recipe.recipe_name }}
          </option>
        </select>

        <select v-model="selectedMealType" required>
          <option value="" disabled>Select meal type</option>
          <option>Breakfast</option>
          <option>Lunch</option>
          <option>Dinner</option>
          <option>Snack</option>
        </select>

        <input type="date" v-model="selectedMealDate" required />

        <button type="submit" class="nav-button action-button">Add Meal</button>
      </form>
    </div>

    <!-- Calendar View -->
    <div v-if="calendarDays.length" class="calendar-view">
      <h2>Meal Plan Calendar</h2>
      <div class="calendar-grid">
        <div class="calendar-day" v-for="day in calendarDays" :key="day.date">
          <div class="day-header">
            <span>{{ formatDate(day.date) }}</span>
          </div>
          <div class="meals">
            <div v-for="meal in day.meals" :key="meal.meal_id" class="meal">
              <a
                class="meal-info"
                :href="'/recipes/' + meal.recipe_id"
                target="_blank"
              >
                <strong>{{ meal.meal_type }}</strong>: {{ meal.recipe_name || "No meal" }}
              </a>
              <button
                class="delete-meal-button"
                @click="deleteMeal(meal.meal_id)"
                aria-label="Delete meal"
              >
                🗑
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="groceryList.length" class="grocery-list-modal">
  <h2>Grocery List</h2>
  <ul>
    <li v-for="item in groceryList" :key="item.ingredient_id">
      {{ item.total_quantity }} {{ item.unit }} of {{ item.ingredient_name }}
    </li>
  </ul>
</div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      mealPlans: [],
      selectedMealPlanId: "",
      calendarDays: [],
      allRecipes: [],
      selectedMealDate: "",
      selectedMealType: "",
      selectedRecipeId: "",
      newMealPlanName: "",
      newMealPlanStartDate: "",
      newMealPlanEndDate: "",
      groceryList: [],
    };
  },
  methods: {
    generateGroceryList() {
    if (!this.selectedMealPlanId) {
      alert("Please select a meal plan first.");
      return;
    }

    axios
      .get(`${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/grocery-list`, {
        headers: { Authorization: `Bearer ${this.$store.state.token}` },
      })
      .then((response) => {
        this.groceryList = response.data;
        this.showGroceryListModal(); // Show the grocery list
      })
      .catch((error) => {
        console.error("Error fetching grocery list:", error);
        alert("Failed to generate grocery list.");
      });
  },

  showGroceryListModal() {
    // Logic to display grocery list in a modal or other component
    alert("Grocery list generated! Check the modal for details."); // Placeholder
  },
    createMealPlan() {
    const adjustedMealPlan = {
      meal_plan_name: this.newMealPlanName,
      start_date: this.addOneDay(this.newMealPlanStartDate),
      end_date: this.addOneDay(this.newMealPlanEndDate),
    };

    axios
      .post(`${import.meta.env.VITE_REMOTE_API}/meal-plans`, adjustedMealPlan, {
        headers: { Authorization: `Bearer ${this.$store.state.token}` },
      })
      .then(() => {
        alert("Meal Plan Created Successfully!");
        this.fetchMealPlans();
        this.newMealPlanName = "";
        this.newMealPlanStartDate = "";
        this.newMealPlanEndDate = "";
      })
      .catch((error) => {
        console.error("Error creating meal plan:", error);
        alert("Failed to create meal plan. Please check the input.");
      });
  },
    randomlyFillMeals() {
      if (!this.allRecipes.length || !this.calendarDays.length) {
        alert("No recipes available or calendar days found.");
        return;
      }

      const mealTypes = ["Breakfast", "Lunch", "Dinner", "Snack"];
      const randomMeals = [];

      this.calendarDays.forEach((day) => {
        mealTypes.forEach((mealType) => {
          const isMealPresent = day.meals.some((meal) => meal.meal_type === mealType);

          if (!isMealPresent) {
            const randomRecipe = this.allRecipes[Math.floor(Math.random() * this.allRecipes.length)];
            randomMeals.push({
              meal_plan_id: this.selectedMealPlanId,
              recipe_id: randomRecipe.recipe_id,
              meal_date: day.date,
              meal_type: mealType,
            });
          }
        });
      });

      if (randomMeals.length === 0) {
        alert("No empty meal slots to fill.");
        return;
      }

      axios
        .post(`${import.meta.env.VITE_REMOTE_API}/meal-plans/meals/bulk-create`, randomMeals, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then(() => {
          alert("Empty meal slots filled successfully!");
          this.fetchMealPlanDetails();
        })
        .catch((error) => console.error("Error saving meals:", error));
    },
    fetchMealPlans() {
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/meal-plans/all`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          this.mealPlans = response.data;
        })
        .catch((error) => console.error("Error fetching meal plans:", error));
    },
    fetchMealPlanDetails() {
      if (!this.selectedMealPlanId) return;

      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/meals-with-recipes`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          this.prepareCalendar(response.data);
        })
        .catch((error) => console.error("Error fetching meal plan meals:", error));

      this.fetchAllRecipes();
    },
    fetchAllRecipes() {
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/my-recipes`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          this.allRecipes = response.data;
        })
        .catch((error) => console.error("Error fetching recipes:", error));
    },
    prepareCalendar(meals) {
      const selectedMealPlan = this.mealPlans.find(
        (plan) => plan.meal_plan_id === this.selectedMealPlanId
      );

      if (!selectedMealPlan) return;

      const startDate = new Date(selectedMealPlan.start_date);
      const endDate = new Date(selectedMealPlan.end_date);
      const calendar = [];

      for (
        let date = new Date(startDate);
        date <= endDate;
        date.setDate(date.getDate() + 1)
      ) {
        const formattedDate = date.toISOString().split("T")[0];

        calendar.push({
          date: formattedDate,
          meals: meals.filter((meal) => {
            const adjustedDate = new Date(meal.meal_date);
            adjustedDate.setDate(adjustedDate.getDate() + 1);
            return adjustedDate.toISOString().split("T")[0] === formattedDate;
          }),
        });
      }

      this.calendarDays = calendar;
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString(undefined, { weekday: "short", month: "short", day: "numeric" });
    },
    addMealToPlan() {
      if (!this.selectedMealDate || !this.selectedMealType || !this.selectedRecipeId) {
        alert("Please fill all fields.");
        return;
      }

      const newMeal = {
        meal_date: this.selectedMealDate,
        meal_type: this.selectedMealType,
        recipe_id: this.selectedRecipeId,
      };

      axios
        .post(
          `${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/add-meal`,
          newMeal,
          { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
        )
        .then(() => {
          alert("Meal added successfully!");
          this.fetchMealPlanDetails();
          this.selectedMealDate = "";
          this.selectedMealType = "";
          this.selectedRecipeId = "";
        })
        .catch((error) => console.error("Error adding meal:", error));
    },
    deleteMeal(mealId) {
      axios
        .delete(`${import.meta.env.VITE_REMOTE_API}/meal-plans/meals/${mealId}`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then(() => {
          alert("Meal deleted successfully!");
          this.fetchMealPlanDetails();
        })
        .catch((error) => console.error("Error deleting meal:", error));
    },
    generateGroceryList() {
      alert("Grocery list generation functionality to be implemented.");
    },
    shareMealPlan() {
      alert("Meal plan share functionality to be implemented.");
    },
    printMealPlan() {
      alert("Meal plan print functionality to be implemented.");
    },
  },
  mounted() {
    this.fetchMealPlans();
  },
};
</script>

<style scoped>
/* General Page Layout */
.meal-plan-list {
  max-width: 900px;
  margin: auto;
  text-align: center;
  background-color: #f5f7f6;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.create-meal-plan {
  margin-bottom: 2rem;
}

h1,
h2 {
  color: #4d8c5e;
}

/* Dropdown */
.meal-plan-dropdown {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 2rem;
}

.dropdown-label {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.dropdown {
  padding: 0.8rem;
  font-size: 1rem;
  border: 2px solid #4d8c5e;
  border-radius: 5px;
  width: 100%;
  max-width: 300px;
}

/* Action Buttons */
.actions {
  margin-top: 1.5rem;
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.nav-button {
  text-decoration: none;
  color: white;
  font-size: 1.2rem;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  background-color: #64a472;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 2px solid black;
}

.nav-button:hover {
  background-color: #4d8c5e;
  transform: translateY(-2px);
}

.nav-button:active {
  background-color: #3b6b46;
  transform: translateY(0);
}

/* Calendar View */
.calendar-view {
  margin-top: 2rem;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 1rem;
}

.calendar-day {
  background-color: #d3f1df;
  padding: 1rem;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.day-header {
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #4d8c5e;
}

.meals {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.meal {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
  color: #333;
}

.meal a {
  color: #4d8c5e;
  text-decoration: none;
}

.meal a:hover {
  text-decoration: underline;
}

.delete-meal-button {
  background: none;
  border: none;
  color: red;
  font-size: 1.2rem;
  cursor: pointer;
}

.delete-meal-button:hover {
  color: darkred;
}

/* Responsive Design */
@media (max-width: 768px) {
  .calendar-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
.grocery-list-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
  z-index: 1000;
}

.grocery-list-modal h2 {
  margin-bottom: 1rem;
  color: #4d8c5e;
}

.grocery-list-modal ul {
  list-style: none;
  padding: 0;
}

.grocery-list-modal li {
  margin-bottom: 0.5rem;
}
</style>