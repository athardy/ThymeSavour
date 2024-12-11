<template>
  <div class="auth-page">
    <h1>You have been logged out.</h1>
    <router-link to="/login">
      <button class="submit-btn">Login Again</button>
    </router-link>
  </div>
</template>

<script>
export default {
  created() {
    // Clear the user's authentication data
    this.$store.commit("LOGOUT");

    // Redirect to the landing page or home page
    this.$router.push({ name: "landing" });
  },
};
</script>

<style scoped>
.auth-page {
  max-width: 400px;
  margin: auto;
  text-align: center;
  background-color: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h1 {
  margin-bottom: 1.5rem;
}

button {
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  width: 100%;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button:active {
  background-color: #004494;
  transform: translateY(0);
}
</style>