<template>
  <div class="meal-plan-creation">
    <h1>Create a New Meal Plan</h1>
    <form @submit.prevent="createMealPlan">
      <div>
        <label for="name">Meal Plan Name:</label>
        <input id="name" v-model="mealPlan.name" required />
      </div>
      <div>
        <label for="startDate">Start Date:</label>
        <input id="startDate" type="date" v-model="mealPlan.start_date" required />
      </div>
      <div>
        <label for="endDate">End Date:</label>
        <input id="endDate" type="date" v-model="mealPlan.end_date" required />
      </div>
      <button type="submit">Create Meal Plan</button>
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
      },
    };
  },
  methods: {
    createMealPlan() {
      axios
        .post(`${import.meta.env.VITE_REMOTE_API}/meal-plans`, this.mealPlan, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then(() => {
          alert("Meal Plan Created Successfully!");
          this.$router.push({ name: "meal-plan-list" });
        })
        .catch((error) => {
          console.error("Error creating meal plan:", error);
          alert("Failed to create meal plan.");
        });
    },
  },
};
</script>

<style scoped>
.meal-plan-creation {
  max-width: 600px;
  margin: auto;
}
</style>