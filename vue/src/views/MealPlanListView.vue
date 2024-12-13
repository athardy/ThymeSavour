<template>
  <div class="meal-plan-list">
    <h1>Meal Plans</h1>

    <div id="print-content" style="display: none;">
  <h1>Meal Plan: {{ mealPlan.meal_plan_name }}</h1>
  <p><strong>Start Date:</strong> {{ formatDate(mealPlan.start_date) }}</p>
  <p><strong>End Date:</strong> {{ formatDate(mealPlan.end_date) }}</p>
  <h2>Meal Plan Calendar</h2>
  <div class="calendar-view">
    <div
      v-for="day in calendarDays"
      :key="day.date"
      class="calendar-day"
    >
      <h3>{{ formatDate(day.date) }}</h3>
      <ul>
        <li
          v-for="slot in ['Breakfast', 'Lunch', 'Dinner', 'Snack 1', 'Snack 2']"
          :key="slot"
        >
          <strong>{{ slot }}:</strong>
          <span v-if="day.meals[slot]">
            {{ day.meals[slot].recipe_name }}
          </span>
          <span v-else>No meal</span>
        </li>
      </ul>
    </div>
  </div>
</div>
      <!-- Share Modal -->
    <div v-if="showShareModal" class="modal-overlay" @click.self="closeShareModal">
      <div class="modal-content">
        <h2>Share Meal Plan</h2>
        <p>
          Share this link with your friends:
        </p>
        <input
          type="text"
          :value="shareLink"
          readonly
          class="share-link-input"
        />
        <button @click="copyShareLink" class="nav-button">Copy Link</button>
        <button @click="closeShareModal" class="nav-button cancel-btn">Close</button>
      </div>
    </div>
    <!-- Create Meal Plan Drawer -->
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

    <!-- Meal Plan Dropdown -->
    <div class="meal-plan-dropdown">
      <label for="meal-plan" class="dropdown-label">Manage Your Meal Plans:</label>
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

    <!-- Add Meal to Meal Plan Form -->
    <div v-if="showAddMealForm" class="add-meal-container">
      <h3>Add a Meal to Meal Plan</h3>
      <form @submit.prevent="addMeal">
        <div class="form-group">
          <label for="meal-date">Select Date:</label>
          <input
            id="meal-date"
            type="date"
            v-model="selectedMealDate"
            class="input-field"
            required
          />
        </div>
        <div class="form-group">
          <label for="meal-type">Meal Type:</label>
          <select
            id="meal-type"
            v-model="selectedMealType"
            class="input-field"
            required
          >
            <option value="" disabled>Select a meal type</option>
            <option v-for="type in mealTypes" :key="type" :value="type">
              {{ type }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="recipe">Select Recipe:</label>
          <select
            id="recipe"
            v-model="selectedRecipeId"
            class="input-field"
            required
          >
            <option value="" disabled>Select a recipe</option>
            <option
              v-for="recipe in allRecipes"
              :key="recipe.recipe_id"
              :value="recipe.recipe_id"
            >
              {{ recipe.recipe_name }}
            </option>
          </select>
        </div>
        <button type="submit" class="nav-button action-button">Add Meal</button>
        <button type="button" @click="toggleAddMealForm" class="nav-button cancel-button">
          Cancel
        </button>
      </form>
    </div>

    <!-- Buttons Section -->
    <div class="buttons-container">
      <button @click="toggleAddMealForm" class="action-button">
        Add Meal to Meal Plan
      </button>
      <button @click="randomlyFillMeals" class="action-button">
        Auto-Fill Meals
      </button>
      <button @click="generateMealPlanGroceryList" class="action-button">
        Generate Grocery List
      </button>
      <button @click="shareMealPlan" class="action-button">
        Share Meal Plan
      </button>
      <button @click="printMealPlan" class="action-button">
        Print Meal Plan
      </button>
      <button @click="deleteMealPlan" class="action-button delete-button">
        Delete Meal Plan
      </button>
    </div>

    <!-- Display Grocery List -->
    <div v-if="mealPlanGroceryList.length > 0" class="recipe-ingredients">
      <h2>Grocery List for Meal Plan: {{ mealPlan.meal_plan_name }}</h2>
      <ul>
        <li
          v-for="(item, index) in mealPlanGroceryList"
          :key="index"
        >
          {{ item.total_quantity }} {{ item.unit }} - {{ item.ingredient_name }}
        </li>
      </ul>
      <button @click="printGroceryList" class="nav-button print-btn">Print Grocery List</button>
    </div>

    <!-- Meal Plan Calendar -->
    <div v-if="selectedMealPlanId" class="calendar-container">
      <h2>Meal Plan Calendar</h2>
      <div class="calendar-view">
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
                :href="'/recipe-details/' + day.meals[slot].recipe_id"
                target="_blank"
              >
                {{ day.meals[slot].recipe_name }}
              </a>
              <button
                class="remove-meal-button"
                @click.prevent="removeMeal(day.meals[slot].meal_id)"
              >
                🗑
              </button>
            </span>
            <span v-else>No meal</span>
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
      mealTypes: ["Breakfast", "Lunch", "Dinner", "Snack 1", "Snack 2"],
      showAddMealForm: false,
      showShareModal: false,
      shareLink: "",
      mealPlanGroceryList: [],
    };
  },
  methods: {
    toggleAddMealForm() {
      this.showAddMealForm = !this.showAddMealForm;
    },
    async generateMealPlanGroceryList() {
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
          this.mealPlanGroceryList = response.data;
          console.log("Generated Grocery List:", this.mealPlanGroceryList);
        } else {
          console.warn("Unexpected response structure:", response.data);
          this.mealPlanGroceryList = [];
        }
      } catch (error) {
        console.error("Error generating grocery list:", error.response?.data || error.message);
        alert("Failed to generate grocery list. Please try again.");
      }
    },
    printGroceryList() {
      const printContent = document.querySelector(".recipe-ingredients");
      const newWindow = window.open("", "_blank");
      newWindow.document.write(`
        <html>
          <head>
            <title>${this.mealPlan.meal_plan_name || "Grocery List"}</title>
            <style>
              body {
                font-family: Arial, sans-serif;
                margin: 20px;
                line-height: 1.6;
              }
              h2 {
                color: #3b5b3b;
                text-align: center;
              }
              ul {
                list-style-type: none;
                padding: 0;
              }
              li {
                margin-bottom: 5px;
              }
            </style>
          </head>
          <body>
            ${printContent.innerHTML}
          </body>
        </html>
      `);
      newWindow.document.close();
      newWindow.print();
    },
    printMealPlan() {
    const printContent = document.querySelector("#print-content");
    const newWindow = window.open("", "_blank");
    newWindow.document.write(`
      <html>
        <head>
          <title>${this.mealPlan.meal_plan_name || "Meal Plan"}</title>
          <style>
            body {
              font-family: Arial, sans-serif;
              margin: 20px;
              line-height: 1.6;
            }
            h1, h2 {
              color: #3b5b3b;
              text-align: center;
            }
            .calendar-view {
              display: grid;
              grid-template-columns: repeat(7, 1fr);
              gap: 10px;
              margin-top: 20px;
            }
            .calendar-day {
              border: 1px solid #ddd;
              padding: 10px;
              border-radius: 5px;
              text-align: center;
              background-color: #f9f9f9;
            }
            ul {
              list-style-type: none;
              padding: 0;
            }
            li {
              margin-bottom: 5px;
            }
            strong {
              color: #333;
            }
          </style>
        </head>
        <body>
          ${printContent.innerHTML}
        </body>
      </html>
    `);
    newWindow.document.close();
    newWindow.print();
  },
    shareMealPlan() {
      if (!this.selectedMealPlanId) {
        alert("Please select a meal plan first.");
        return;
      }

      // Generate a fake share link
      this.shareLink = `https://mymealplannerapp.com/meal-plan/${this.selectedMealPlanId}`;
      this.showShareModal = true; // Show the modal
    },
    copyShareLink() {
      navigator.clipboard.writeText(this.shareLink).then(
        () => alert("Link copied to clipboard!"),
        (error) => alert("Failed to copy link: " + error.message)
      );
    },
    closeShareModal() {
      this.showShareModal = false;
    },
  
    async removeMeal(mealId) {
    if (!mealId) {
      alert("Invalid meal ID.");
      return;
    }

    if (!confirm("Are you sure you want to remove this meal from the plan?")) {
      return;
    }

    try {
      await axios.delete(
        `${import.meta.env.VITE_REMOTE_API}/meal-plans/meals/${mealId}`,
        { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
      );
      alert("Meal removed successfully!");
      this.fetchMealPlanDetails(); // Refresh the calendar
    } catch (error) {
      console.error("Error removing meal:", error.response?.data || error.message);
      alert("Failed to remove meal. Please try again.");
    }
  },
    async randomlyFillMeals() {
  if (!this.selectedMealPlanId) {
    alert("Please select a meal plan first.");
    return;
  }

  try {
    const url = `${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/randomly-fill-meals`;
    await axios.post(url, {}, { headers: { Authorization: `Bearer ${this.$store.state.token}` } });
    alert("Meals filled successfully!");
    this.fetchMealPlanDetails(); // Refresh the calendar
  } catch (error) {
    console.error("Error randomly filling meals:", error.response?.data || error.message);
    alert("Failed to fill meals. Please try again.");
  }
},
    fetchRecipes() {
    axios
      .get(`${import.meta.env.VITE_REMOTE_API}/recipes/my-recipes`, {
        headers: { Authorization: `Bearer ${this.$store.state.token}` },
      })
      .then((response) => {
        this.allRecipes = response.data; // Populate allRecipes with API response
      })
      .catch((error) => {
        console.error("Error fetching recipes:", error);
        alert("Failed to load recipes.");
      });
  },
    addMealToPlan() {
    this.showAddMealForm = !this.showAddMealForm; // Toggle the visibility
  },
    async addMeal() {
    if (!this.selectedMealDate || !this.selectedMealType || !this.selectedRecipeId) {
      alert("Please fill out all fields.");
      return;
    }

    const mealData = {
      meal_date: this.selectedMealDate,
      meal_type: this.selectedMealType,
      recipe_id: this.selectedRecipeId,
    };

    try {
      await axios.post(
        `${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/add-meal`,
        mealData,
        { headers: { Authorization: `Bearer ${this.$store.state.token}` } }
      );
      alert("Meal added successfully!");
      this.fetchMealPlanDetails(); // Refresh calendar
      // Clear form inputs
      this.selectedMealDate = "";
      this.selectedMealType = "";
      this.selectedRecipeId = "";
    } catch (error) {
      console.error("Error adding meal:", error.response?.data || error.message);
      alert("Failed to add meal. Please try again.");
    }
  },
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
      start_date: this.addTwoDays(this.newMealPlanStartDate),
      end_date: this.addTwoDays(this.newMealPlanEndDate),
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
    this.fetchRecipes();
  },
};
</script>

<style scoped>
/* General Container */
.meal-plan-list {
  max-width: 1200px;
  margin: auto;
  padding: 2rem;
  background-color: #f9fafb;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* Headings */
h1,
h2 {
  color: #3b5b3b;
  font-family: 'Poppins', sans-serif;
  text-align: center;
}

/* Buttons Section */
.buttons-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: nowrap;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background-color: #e7ecef;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  gap: 10px; /* Add space between buttons */
}

.buttons-container .action-button {
  font-size: 0.9rem;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  background-color: #64a472;
  color: white;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.buttons-container .action-button:hover {
  background-color: #4d8c5e;
}

.buttons-container .delete-button {
  background-color: #d9534f;
}

.buttons-container .delete-button:hover {
  background-color: #c9302c;
}

/* Calendar Container */
.calendar-container {
  background-color: #ffffff;
  padding: 1rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

/* Calendar Grid */
.calendar-view {
  display: grid;
  grid-template-columns: repeat(7, 1fr); /* 7 days per row */
  gap: 1rem;
  margin-top: 1rem;
}

.calendar-day {
  background-color: #d3f1df;
  padding: 1rem;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* Calendar Day Header */
.calendar-day h3 {
  margin: 0 0 0.5rem 0;
  font-size: 1rem;
  color: #3b5b3b;
}

/* Meal Slot Styling */
.meal-slot {
  font-size: 0.85rem;
  margin: 0.25rem 0;
}

.meal-slot a {
  color: #4d8c5e;
  text-decoration: none;
}

.meal-slot a:hover {
  text-decoration: underline;
}
.add-meal-container {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #f9fafb;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.add-meal-container h3 {
  color: #3b5b3b;
}
.remove-meal-button {
  background: transparent;
  border: none;
  color: red;
  cursor: pointer;
  margin-left: 8px;
  font-size: 16px;
}

.remove-meal-button:hover {
  color: darkred;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 80%;
  max-width: 400px;
}

.share-link-input {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
}

.nav-button {
  padding: 10px 20px;
  margin: 5px;
  border: none;
  border-radius: 5px;
  background-color: #64a472;
  color: white;
  cursor: pointer;
}

.nav-button:hover {
  background-color: #4d8c5e;
}

.cancel-btn {
  background-color: #d9534f;
}

.cancel-btn:hover {
  background-color: #c9302c;
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

.nav-button.print-btn {
  margin-top: 20px;
  background-color: #64a472;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  border: none;
}

.nav-button.print-btn:hover {
  background-color: #4d8c5e;
}
.add-meal-container {
  margin: 1rem 0;
  padding: 1rem;
  background-color: #f9fafb;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-meal-container h3 {
  color: #3b5b3b;
}

.cancel-button {
  background-color: #d9534f;
  color: white;
}

.cancel-button:hover {
  background-color: #c9302c;
}

/* Adjustments for Calendar Section */
.calendar-container {
  margin-top: 1.5rem;
}

/* General Styling */
.buttons-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin-bottom: 1.5rem;
  gap: 10px;
}
</style>