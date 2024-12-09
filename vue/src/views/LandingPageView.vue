<template>
    <div class="landing-page">
      <h1>Welcome to Meal Planner!</h1>
      <p>Organize your meals, create recipes, and manage your grocery list effortlessly.</p>
      <router-link to="/login">
        <button>Login</button>
      </router-link>
      <router-link to="/register">
        <button>Register</button>
      </router-link>
    </div>
  </template>
  
  <script>
  export default {
    name: "LandingPageView",
  };
  </script>
  
  <style scoped>
  .landing-page {
    text-align: center;
    margin-top: 2rem;
  }
  button {
    padding: 0.5rem 1.5rem;
    font-size: 1.2rem;
    margin: 0.5rem;
    cursor: pointer;
  }
  </style>