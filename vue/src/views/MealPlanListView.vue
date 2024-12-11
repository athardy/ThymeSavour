<template>
  <div class="meal-plan-list">
    <h1>Meal Plans</h1>

    <!-- Meal Plan Dropdown -->
    <div class="meal-plan-dropdown">
      <label for="meal-plan">Select a Meal Plan</label>
      <select v-model="selectedMealPlanId" @change="fetchMealPlanDetails">
        <option value="" disabled selected>Select a meal plan</option>
        <option v-for="mealPlan in mealPlans" :key="mealPlan.meal_plan_id" :value="mealPlan.meal_plan_id">
          {{ mealPlan.meal_plan_name }}
        </option>
      </select>
    </div>

    <!-- Meal Plan Table -->
    <div v-if="selectedMealPlanId">
      <table>
        <thead>
          <tr>
            <th>Meal</th>
            <th>Type</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="meal in mealPlanMeals" :key="meal.meal_id">
            <td>{{ meal.recipe_name }}</td>
            <td>{{ meal.meal_type }}</td>
            <td>{{ meal.meal_date }}</td>
          </tr>
        </tbody>
      </table>

      <!-- Meal Plan Action Buttons -->
      <div class="actions">
        <button @click="editMealPlan">Edit Meal Plan</button>
        <button @click="generateGroceryList">Generate Grocery List</button>
        <button @click="shareMealPlan">Share Meal Plan</button>
        <button @click="printMealPlan">Print Meal Plan</button>
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
      mealPlanMeals: [],
    };
  },
  methods: {
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
        .get(`${import.meta.env.VITE_REMOTE_API}/meal-plans/${this.selectedMealPlanId}/meals`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          this.mealPlanMeals = response.data;
        })
        .catch((error) => console.error("Error fetching meal plan meals:", error));
    },
    editMealPlan() {
      this.$router.push({ name: "meal-plan-edit", params: { id: this.selectedMealPlanId } });
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
.meal-plan-list {
  max-width: 800px;
  margin: auto;
}

button {
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  transition: background-color 0.3s ease, transform 0.2s ease;
  width: 100%;
  max-width: 200px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button:active {
  background-color: #004494;
  transform: translateY(0);
}

.actions {
  margin-top: 1rem;
  display: flex;
  gap: 1rem;
  justify-content: center;
}
</style>