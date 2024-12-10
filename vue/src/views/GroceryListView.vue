<template>
    <div class="grocery-list">
        <h1>Grocery List</h1>


        <!-- dropdown for meal-plans -->
      <div class="dropdown">
        <label for="meal-plan-select">Select a Meal Plan:</label>
        <select
          id="meal-plan-select"
          v-model="selectedMealPlan"
          @change="fetchGroceryList"
        >
          <option disabled value="">Please select</option>
          <option v-for="plan in mealPlans" :key="plan.meal_plan_id" :value="plan">
            {{ plan.meal_plan_name }}
          </option>
        </select>
      </div>
    </div>

      <!-- Details Section -->
      <div v-if="selectedMealPlan" class="meal-plan-details">
        <h2>Grocery List Details</h2>

        <div>
            Ingredient Name: {{ groceryList }}



        </div>
    





    </div>
</template>





<script>
import axios from "axios";
  
  export default {
    name: "GroceryListView",
    data() {
        return {
            mealPlans: [],
            groceryList: null,
            selectedMealPlan: null,
        };
    },
    methods: {
        fetchGroceryList() {
            axios
                .get("/grocery-list")
                .then((response) => {
                    this.groceryList = response.data;
                })
                .catch((error) => {
            console.error("Error fetching grocery list:", error);
          });
        },

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

        fetchGroceryListForSelectedPlan() {
        
        axios
          .get(`/grocery-list/${this.selectedMealPlan.meal_plan_id}/meals`)
          .then((response) => {
            this.meals = response.data;
            this.fetchGroceryList();
          })
          .catch((error) => {
            console.error("Error fetching meals for selected plan:", error);
          });
        }
    },
    mounted() {
        this.fetchMealPlans();
    }
};




</script>




<style>

</style>