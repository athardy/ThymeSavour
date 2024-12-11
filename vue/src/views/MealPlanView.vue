<template>
  <div class="meal-plan-list">
    <h1>Meal Plans</h1>

    <!-- Meal Plan Actions -->
    <div class="actions">
      <button @click="createNewMealPlan" class="create-button">Create New Meal Plan</button>
    </div>

    <!-- Meal Plan Selector -->
    <div v-if="mealPlans.length > 0">
      <h2>Your Meal Plans</h2>
      <ul class="meal-plan-items">
        <li v-for="mealPlan in mealPlans" :key="mealPlan.meal_plan_id" class="meal-plan-item">
          <div>
            <strong>{{ mealPlan.meal_plan_name }}</strong> 
            ({{ formatDate(mealPlan.start_date) }} - {{ formatDate(mealPlan.end_date) }})
          </div>
          <div class="actions">
            <button @click="viewMealPlan(mealPlan.meal_plan_id)" class="view-button">View</button>
            <button @click="shareMealPlan(mealPlan.meal_plan_id)" class="share-button">Share</button>
            <button @click="printMealPlan(mealPlan.meal_plan_id)" class="print-button">Print</button>
          </div>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>No meal plans found. Create one to get started!</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      mealPlans: [],
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
    createNewMealPlan() {
      this.$router.push({ name: "create-meal-plan" });
    },
    viewMealPlan(mealPlanId) {
      this.$router.push({ name: "meal-plan-details", params: { id: mealPlanId } });
    },
    shareMealPlan(mealPlanId) {
      axios
        .post(`${import.meta.env.VITE_REMOTE_API}/meal-plans/${mealPlanId}/share`, {}, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          alert(`Shareable link: ${response.data}`);
        })
        .catch((error) => console.error("Error sharing meal plan:", error));
    },
    printMealPlan(mealPlanId) {
      window.open(`${import.meta.env.VITE_REMOTE_API}/meal-plans/${mealPlanId}/print`, "_blank");
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString();
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
  text-align: center;
}

.actions {
  margin-bottom: 1rem;
}

.create-button {
  background-color: #28a745; /* Green */
  color: white;
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.create-button:hover {
  background-color: #218838;
}

.meal-plan-items {
  list-style: none;
  padding: 0;
}

.meal-plan-item {
  background-color: #f9f9f9;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 5px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.meal-plan-item strong {
  font-size: 1.3rem;
}

.meal-plan-item .actions {
  margin-top: 1rem;
  display: flex;
  gap: 0.5rem;
}

button {
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  transform: translateY(-2px);
}

button:active {
  transform: translateY(0);
}

.view-button {
  background-color: #007bff; /* Blue */
  color: white;
}

.view-button:hover {
  background-color: #0056b3;
}

.share-button {
  background-color: #ffc107; /* Yellow */
  color: white;
}

.share-button:hover {
  background-color: #e0a800;
}

.print-button {
  background-color: #dc3545; /* Red */
  color: white;
}

.print-button:hover {
  background-color: #c82333;
}
</style>