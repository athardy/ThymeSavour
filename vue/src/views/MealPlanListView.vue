<template>
  <div class="meal-plan-list">
    <h1>Meal Plans</h1>

    <div class="create-meal-plan">
      <h2 @click="toggleCreateMealPlan" class="dropdown-toggle">
        Create a New Meal Plan <span>{{ isCreatingMealPlan ? "▲" : "▼" }}</span>
      </h2>
      <div v-if="isCreatingMealPlan" class="drawer">
        <form @submit.prevent="createMealPlan" class="create-meal-plan-form">
          <div class="form-group">
            <label for="meal-plan-name">Meal Plan Name:</label>
            <input
              id="meal-plan-name"
              type="text"
              v-model="newMealPlanName"
              class="input-field"
              placeholder="Enter meal plan name"
              required
            />
          </div>
          <div class="form-group">
            <label for="start-date">Start Date:</label>
            <input
              id="start-date"
              type="date"
              v-model="newMealPlanStartDate"
              class="input-field"
              required
            />
          </div>
          <div class="form-group">
            <label for="end-date">End Date:</label>
            <input
              id="end-date"
              type="date"
              v-model="newMealPlanEndDate"
              class="input-field"
              required
            />
          </div>
          <button type="submit" class="nav-button action-button">
            Create Meal Plan
          </button>
        </form>
      </div>
    </div>

    <div class="meal-plan-dropdown">
      <label for="meal-plan" class="dropdown-label">Select a Meal Plan:</label>
      <select
        id="meal-plan"
        v-model="selectedMealPlanId"
        @change="fetchMealPlanDetails"
        class="dropdown"
      >
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

    <div v-if="selectedMealPlanId" class="calendar-container">
      <div class="calendar-sidebar">
        <button @click="randomlyFillMeals" class="small-button">
          Randomly Fill Meals
        </button>
        <button @click="generateGroceryList" class="small-button">
          Generate Grocery List
        </button>
        <button @click="shareMealPlan" class="small-button">
          Share Meal Plan
        </button>
        <button @click="printMealPlan" class="small-button">
          Print Meal Plan
        </button>
        <button @click="deleteMealPlan" class="small-button delete-button">
          Delete Meal Plan
        </button>
      </div>

      <div class="calendar-view">
        <h2>Meal Plan Calendar</h2>
        <div class="calendar-grid">
          <div
            v-for="day in calendarDays"
            :key="day.date"
            class="calendar-day"
          >
            <h3>{{ formatDate(day.date) }}</h3>
            <div
              class="meal-slot"
              v-for="slot in ['Breakfast', 'Lunch', 'Dinner', 'Snack 1', 'Snack 2']"
              :key="slot"
            >
              <strong>{{ slot }}:</strong>
              <span v-if="day.meals[slot]">
                <a
                  :href="'/recipes/' + day.meals[slot].recipe_id"
                  target="_blank"
                >
                  {{ day.meals[slot].recipe_name }}
                </a>
              </span>
              <span v-else>No meal</span>
            </div>
          </div>
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
      mealPlans: [],
      selectedMealPlanId: "",
      calendarDays: [],
      mealPlan: { start_date: "", end_date: "" },
      allRecipes: [],
      selectedMealDate: "",
      selectedMealType: "",
      selectedRecipeId: "",
      newMealPlanName: "",
      newMealPlanStartDate: "",
      newMealPlanEndDate: "",
      groceryList: [],
      isCreatingMealPlan: false,
    };
  },
  methods: {
    toggleCreateMealPlan() {
      this.isCreatingMealPlan = !this.isCreatingMealPlan;
    },
    
    addTwoDays(dateString) {
  if (!dateString) return null;

  // Convert the input string to a Date object
  const localDate = new Date(dateString);

  // Use the same local date without changing timezone
  const adjustedDate = new Date(
    localDate.getFullYear(),
    localDate.getMonth(),
    localDate.getDate() + 2
  );

  // Return the adjusted date in ISO format (yyyy-MM-dd)
  return adjustedDate.toISOString().split("T")[0];
},
  createMealPlan() {
    if (!this.newMealPlanName || !this.newMealPlanStartDate || !this.newMealPlanEndDate) {
      alert("Please fill out all fields: Name, Start Date, and End Date.");
      return;
    }

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
        this.isCreatingMealPlan = false;
      })
      .catch((error) => {
        console.error("Error creating meal plan:", error.response?.data || error.message);
        alert("Failed to create meal plan. Please check the input.");
      });
  },

    fetchMealPlans() {
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/meal-plans/all`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          this.mealPlans = response.data.filter(
            (plan) => plan.user_id === this.$store.state.user.id
          );
        })
        .catch((error) => {
          console.error("Error fetching meal plans:", error);
          alert("Failed to load meal plans.");
        });
    },
    formatDate(date) {
      const localDate = new Date(date + "T00:00:00");
      return localDate.toLocaleDateString(undefined, {
        weekday: "short",
        month: "short",
        day: "numeric",
      });
    },
    async fetchMealPlanDetails() {
      if (!this.selectedMealPlanId) return;

      try {
        const response = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/meals-with-recipes`,
          { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
        );
        const mealPlanResponse = await axios.get(
          `${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}`,
          { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
        );

        this.mealPlan = mealPlanResponse.data;
        this.processCalendarData(response.data);
      } catch (error) {
        console.error("Error fetching meal plan details:", error);
        alert("Failed to load meal plan details.");
      }
    },
    async deleteMealPlan() {
      if (confirm("Are you sure you want to delete this meal plan?")) {
        try {
          await axios.delete(
            `${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}`,
            { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
          );
          alert("Meal plan deleted successfully!");
          this.fetchMealPlans();
          this.selectedMealPlanId = "";
          this.calendarDays = [];
        } catch (error) {
          console.error("Error deleting meal plan:", error);
          alert("Failed to delete meal plan. Please try again.");
        }
      }
    },
    processCalendarData(meals) {
      const days = {};
      const startDate = new Date(this.mealPlan.start_date + "T00:00:00");
      const endDate = new Date(this.mealPlan.end_date + "T00:00:00");

      for (
        let date = new Date(startDate.getTime());
        date <= endDate;
        date.setDate(date.getDate() + 1)
      ) {
        const formattedDate = date.toLocaleDateString("en-CA");
        days[formattedDate] = { date: formattedDate, meals: {} };
      }

      meals.forEach((meal) => {
        const date = meal.meal_date;
        if (days[date]) {
          days[date].meals[meal.meal_type] = meal;
        }
      });

      this.calendarDays = Object.values(days).sort(
        (a, b) => new Date(a.date) - new Date(b.date)
      );
    },
  },
  mounted() {
    this.fetchMealPlans();
  },
};
</script>

<style scoped>
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

.calendar-container {
  display: flex;
  flex-direction: row;
  background-color: #f5f7f6;
  padding: 1rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 900px;
  margin: auto;
  gap: 1rem;
}

.calendar-sidebar {
  flex: 0 0 200px;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  background-color: #e7ecef;
  padding: 1rem;
  border-radius: 10px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

.small-button {
  font-size: 0.85rem;
  padding: 0.5rem 0.75rem;
  border-radius: 5px;
  background-color: #64a472;
  color: white;
  border: 1px solid #4d8c5e;
  cursor: pointer;
  transition: all 0.3s ease;
}

.small-button:hover {
  background-color: #4d8c5e;
}

.delete-button {
  background-color: #d9534f;
}

.delete-button:hover {
  background-color: #c9302c;
}

.calendar-view {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem;
  background-color: white;
  border-radius: 10px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.5rem;
  flex-grow: 1;
  overflow-y: auto;
}

.calendar-day {
  background-color: #d3f1df;
  padding: 0.5rem;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.meal-slot {
  font-size: 0.85rem;
  margin: 0.25rem 0;
}
</style>