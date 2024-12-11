<template>
  <div id="capstone-app">
    <header>
      <img src="src/assets/main_logo.png" alt="Thyme Savour Logo" class="logo">

      <!-- Main Navigation -->
      <nav class="main-nav">
        <!-- Home button -->
        <router-link :to="homeLink" class="nav-button">
          Home<img src="src/assets/banner_home.png" alt="Home Icon" />
        </router-link>

        <!-- Other buttons -->
        <router-link v-if="$store.state.token" to="/my-recipes" class="nav-button">
          Recipes<img src="src/assets/banner_recipes.png" alt="Recipes Icon" />
        </router-link>

        <router-link v-if="$store.state.token" to="/meal-plan-list" class="nav-button">
          Meal Plans<img src="src/assets/banner_meal_plan.png" alt="Meal Plan Icon" />
        </router-link>

        <router-link v-if="$store.state.token" to="/grocery-list" class="nav-button">
          Grocery List<img src="src/assets/banner_grocery_list.png" alt="Grocery List Icon" />
        </router-link>

        <router-link to="/about" class="nav-button">
          About<img src="src/assets/banner_about.png" alt="About Icon" />
        </router-link>

        <!-- Logout button -->
        <button v-if="$store.state.token" @click="logout" class="nav-button logout-button">
          Logout<img src="src/assets/banner_logout.png" alt="Logout Icon" />
        </button>

        <!-- Login/Register buttons -->
        <router-link v-if="!$store.state.token" to="/login" class="nav-button">Login</router-link>
        <router-link v-if="!$store.state.token" to="/register" class="nav-button">Register</router-link>
      </nav>
    </header>

    <main>
      <!-- Router View -->
      <router-view />
      <!-- Loading Spinner -->
      <LoadingSpinner :isLoading="isLoading" />
    </main>

    <footer class="footer">
      <p>&copy; 2024 Thyme Savour. All Rights Reserved.</p>
    </footer>
  </div>
</template>

<script>
import { reactive } from "vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue"; // Import the spinner

// Global loading state
export const loadingState = reactive({
  isLoading: false,
});

export default {
  components: { LoadingSpinner },
  computed: {
    homeLink() {
      return this.$store.state.token ? "/home" : "/landing"; // Correct route for logged-in users
    },
    isLoading() {
      return loadingState.isLoading;
    },
  },
  methods: {
    logout() {
      this.$store.commit("LOGOUT");
      this.$router.push({ name: "landing" });
    },
  },
};
</script>

<style scoped>
#capstone-app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #D3F1DF; /* Main background color */
  font-family: Arial, Helvetica, sans-serif;
}

header {
  background-color: #85A98F; /* Text background color */
  padding: 1rem;
  display: flex;
  justify-content: center; /* Center the menu */
  align-items: center;
  color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

footer {
  text-align: center;
  background-color: #85A98F; /* Matches header */
  color: white;
  padding: 1rem;
  width: 100%; /* Ensure full width */
  position: fixed;
  bottom: 0; /* Keep footer at the bottom */
  left: 0;
}

.main-nav {
  display: flex;
  gap: 1.5rem;
  width: 100%;
  justify-content: center; /* Center all buttons in the nav */
  flex-wrap: wrap;
}

.nav-button {
  text-decoration: none;
  color: white;
  font-size: 1.2rem;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  background-color: #64A472; /* Adjusted button color */
  transition: background-color 0.3s ease, transform 0.2s ease;
  max-width: 180px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px solid #000; /* Added black border */
}

.nav-button:hover {
  background-color: #4D8C5E; /* Slightly darker for hover */
  transform: translateY(-2px);
}

.nav-button:active {
  background-color: #3B6B46; /* Darker on click */
  transform: translateY(0);
}

.logout-button {
  background-color: #FF9A9A; /* Darker pastel red */
}

.logout-button:hover {
  background-color: #FF7F7F; /* Slightly darker for hover */
}

.logout-button:active {
  background-color: #FF6363; /* Even darker on click */
}

main {
  flex: 1;
  padding: 2rem;
  width: 100%;
  max-width: 1200px; /* Main content max-width */
  margin: 0 auto; /* Centers the main content */
  margin-top: 5rem; /* Pushes main content down to avoid overlap with fixed header */
}

footer p {
  margin: 0;
}

@media (max-width: 768px) {
  .main-nav {
    gap: 1rem; /* Reduce space between buttons */
  }

  .nav-button {
    font-size: 1rem; /* Adjust font size for smaller screens */
    padding: 0.4rem 0.8rem;
  }
}
</style>