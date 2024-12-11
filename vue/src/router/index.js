import { createRouter, createWebHistory } from 'vue-router';

// Import your views
import HomeView from '@/views/HomeView.vue';
import LandingPageView from '@/views/LandingPageView.vue'; // Import LandingPageView
import AboutView from '@/views/AboutView.vue'; // Import AboutView
import MyRecipesView from '@/views/MyRecipesView.vue';
import RecipeCreationView from '@/views/RecipeCreationView.vue';
import MealPlanListView from '@/views/MealPlanListView.vue';
import MealPlanCreationView from '@/views/MealPlanCreationView.vue';
import GroceryListView from '@/views/GroceryListView.vue';
import LoginView from '@/views/LoginView.vue';
import RegisterView from '@/views/RegisterView.vue';
import MealPlanEditView from '@/views/MealPlanEditView.vue';
import RecipeDetailsView from '@/views/RecipeDetailsView.vue';

const routes = [
  {
    path: '/home',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/landing',
    name: 'landing',
    component: LandingPageView,  // Ensure the LandingPageView is routed correctly
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView,
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
  },
  {
    path: '/my-recipes',
    name: 'my-recipes',
    component: MyRecipesView,
  },
  {
    path: '/create-recipe',
    name: 'create-recipe',
    component: RecipeCreationView,
  },
  {
    path: '/meal-plan-list',
    name: 'meal-plan-list',
    component: MealPlanListView,
  },
  {
    path: '/create-meal-plan',
    name: 'create-meal-plan',
    component: MealPlanCreationView,
  },
  {
    path: '/grocery-list',
    name: 'grocery-list',
    component: GroceryListView,
  },
  {
    path: '/meal-plan-edit/:id',
    name: 'meal-plan-edit',
    component: MealPlanEditView,
    props: true,
  },
  {
    path: '/recipe-details/:id',
    name: 'recipe-details',
    component: RecipeDetailsView,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;