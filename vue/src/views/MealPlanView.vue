<template>
    <div class="meal-plan-view">
        <h1>Meal Plans</h1>

        <!-- Dropdown for Meal Plans-->
         <div class="dropdown">
            <label for="meal-plan-select">Select a Meal Plan: </label>
            <select 
            id="meal-plan-select"
            v-model="selectedMealPlan"
            @change="fetchMealsForSelectedPlan"
            >
            <option disbabled value="">Please select</option>
            <option v-for="plan in mealPlans" :key="plan.meal_plan.id" value="plan">
                {{ plan.meal_plan_name }}
            </option>
            </select>
         </div>

         <!-- Methods Section -->
        <div class="methods-section">
            <h2>Actions</h2>
            <div v-if="selectedMealPlan">
                <p>Selected Meal Plan: {{ selectedMealPlan.meal_plan_name }}</p>
                <button @click="createMeal">Create a New Meal</button>
                <button></button>
            </div>
            <div v-else>
                <p>Please select a meal plan to see available actions.</p>
            </div>
        </div>

    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: "MealPlanView",
        data() {
            return {
                mealPlans: [], //Hold the meals that we get from the server
                selectedMealPlan: null, //the selected meal plan including info
            };
        },
        methods: {
            fetchMealPlans() {
                // gets ALL  meal plans
                axios   
                    .get("/meal-plans/all")
                    .then((response) => {
                        this.mealPlans = response.data;
                    })
            },
            fetchMealsForSelectedPlan() {
                if (!this.selectedMealPlan) return;

                axios
                    .get(`/meal-plans/${this.selectedMealPlan.meal_plan_id}/meals`)
                    .then((response) => {
                        console.log("Meals for selected plan: ", response.data);
                    })
            }
        }
    }


</script>

<style>

</style>