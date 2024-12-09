<template>
  <div class="home">
    <h1>Welcome, {{ capitalizedUsername }}!</h1>
    <p>What would you like to do?</p>
  </div>
</template>

<script>
export default {
  computed: {
    user() {
      return this.$store.state.user; // Fetch user info from Vuex store
    },
    capitalizedUsername() {
      return this.user.username.charAt(0).toUpperCase() + this.user.username.slice(1);
    },
  },
};
</script>

<style scoped>
.home {
  text-align: center;
  margin-top: 2rem;
}
</style>