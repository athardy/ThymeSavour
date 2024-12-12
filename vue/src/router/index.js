import { createRouter, createWebHistory } from "vue-router";
import { loadingState } from "@/App.vue"; // Import global loading state

// Import your views
import HomeView from "@/views/HomeView.vue";
import LandingPageView from "@/views/LandingPageView.vue";
import AboutView from "@/views/AboutView.vue";
import MyRecipesView from "@/views/MyRecipesView.vue";
import RecipeCreationView from "@/views/RecipeCreationView.vue";
import MealPlanListView from "@/views/MealPlanListView.vue";
import MealPlanCreationView from "@/views/MealPlanCreationView.vue";
import GroceryListView from "@/views/GroceryListView.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import MealPlanEditView from "@/views/MealPlanEditView.vue";
import RecipeDetailsView from "@/views/RecipeDetailsView.vue";

const routes = [
  { path: "/home", name: "home", component: HomeView, meta: { title: "Home - Thyme Savour" } },
  { path: "/landing", name: "landing", component: LandingPageView, meta: { title: "Welcome - Thyme Savour" } },
  { path: "/about", name: "about", component: AboutView, meta: { title: "About Us - Thyme Savour" } },
  { path: "/login", name: "login", component: LoginView, meta: { title: "Login - Thyme Savour" } },
  { path: "/register", name: "register", component: RegisterView, meta: { title: "Register - Thyme Savour" } },
  { path: "/my-recipes", name: "my-recipes", component: MyRecipesView, meta: { title: "My Recipes - Thyme Savour" } },
  { path: "/create-recipe", name: "create-recipe", component: RecipeCreationView, meta: { title: "Create Recipe - Thyme Savour" } },
  { path: "/meal-plan-list", name: "meal-plan-list", component: MealPlanListView, meta: { title: "Meal Plans - Thyme Savour" } },
  { path: "/create-meal-plan", name: "create-meal-plan", component: MealPlanCreationView, meta: { title: "Create Meal Plan - Thyme Savour" } },
  { path: "/grocery-list", name: "grocery-list", component: GroceryListView, meta: { title: "Grocery List - Thyme Savour" } },
  {
    path: "/meal-plan-edit/:id",
    name: "meal-plan-edit",
    component: MealPlanEditView,
    props: true,
    meta: { title: "Edit Meal Plan - Thyme Savour" },
  },
  {
    path: "/recipe-details/:id",
    name: "recipe-details",
    component: RecipeDetailsView,
    props: true,
    meta: { title: "Recipe Details - Thyme Savour" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  console.log("Navigation started: Showing spinner");

  // Update the document title dynamically
  document.title = to.meta.title || "Thyme Savour"; // Default title

  loadingState.isLoading = true; // Show spinner
  next();
});

router.afterEach(() => {
  console.log("Navigation ended: Hiding spinner");
  // Ensure spinner is displayed for at least 1 second
  setTimeout(() => {
    loadingState.isLoading = false; // Hide spinner
  }, 1000); // 1-second minimum spinner visibility
});

export default router;