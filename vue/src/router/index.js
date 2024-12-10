import { createRouter, createWebHistory } from 'vue-router';
import { useStore } from 'vuex';

// Import components
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import LogoutView from '../views/LogoutView.vue';
import RegisterView from '../views/RegisterView.vue';
import MyRecipesView from '../views/MyRecipesView.vue'; // Import MyRecipesView component
import RecipeCreationView from '../views/RecipeCreationView.vue'; // Import recipe creation view
import LandingPageView from '../views/LandingPageView.vue';
import MealPlanView from '../views/MealPlanView.vue';
import GroceryListView from '../views/GroceryListView.vue';

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login.
 * If they have (or don't need to) they're allowed to go about their way.
 */
const routes = [
  {
    path: '/meal-plan',
    name: 'meal-plan',
    component: MealPlanView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/grocery-list',
    name: 'grocery-list',
    component: GroceryListView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/',
    name: 'landing',
    component: LandingPageView,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/logout',
    name: 'logout',
    component: LogoutView,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/my-recipes', // Route for My Recipes
    name: 'my-recipes',
    component: MyRecipesView,
    meta: {
      requiresAuth: true, // Only accessible for logged-in users
    },
  },
  {
    path: '/create', // Route for creating a Recipe
    name: 'create',
    component: RecipeCreationView,
    meta: {
      requiresAuth: true, // Only accessible for logged-in users
    },
  },
];

// Create the router
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

router.beforeEach((to, from, next) => {
  const store = useStore();

  const requiresAuth = to.matched.some((route) => route.meta.requiresAuth);

  // If trying to access a protected route without being logged in
  if (requiresAuth && !store.state.token) {
    return next({ name: 'login' });
  }

  // Redirect logged-in users away from the landing page
  if (to.name === 'landing' && store.state.token) {
    return next({ name: 'home' });
  }

  // Proceed as normal
  next();
});

export default router;