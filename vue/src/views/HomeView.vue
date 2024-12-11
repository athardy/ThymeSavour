<template>
  <div class="home">
    <div class="header-section">
      <h1>Welcome, {{ capitalizedUsername }}!</h1>
      <p class="slogan">What are we cooking up today?</p>
      <p class="prompt">Select an option from the menu above to get started.</p>
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    user() {
      return this.$store.state.user; // Fetch user info from Vuex store
    },
    capitalizedUsername() {
      return this.user.username
        ? this.user.username.charAt(0).toUpperCase() + this.user.username.slice(1)
        : "Guest";
    },
  },
};
</script>

<style scoped>
.home {
  text-align: center;
  margin-top: 4rem;
}

.header-section {
  margin-bottom: 2rem;
}

.header-section h1 {
  font-size: 2.5rem;
  font-weight: 600;
  color: #333;
}

.header-section .slogan {
  font-size: 1.5rem;
  font-weight: 500;
  color: #4D8C5E; /* Matching the green color */
  margin-top: 1rem;
}

.header-section .prompt {
  font-size: 1.2rem;
  color: #555;
  margin-top: 1rem;
  font-style: italic;
}
</style>