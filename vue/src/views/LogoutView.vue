<template>
  <h1>Logout</h1>
</template>

<script>
export default {
  created() {
    // Clear the user's authentication data
    this.$store.commit("LOGOUT");

    // Redirect to the landing page instead of the login page
    this.$router.push({ name: "landing" });
  }
};
</script>