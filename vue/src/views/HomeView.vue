<template>
  <div class="home">
    <h1>Welcome, {{ user.username }}!</h1>
    <p>You are authenticated!</p>
    <button @click="goToMyRecipes">View My Recipes</button>
    <button @click="goToCreateRecipe">Create New Recipe</button>
  </div>
</template>

<script>
export default {
  computed: {
    user() {
      return this.$store.state.user; // Fetch user info from Vuex store
    },
  },
  methods: {
    goToMyRecipes() {
      this.$router.push({ name: "my-recipes" }); // Navigate to My Recipes view
    },
    goToCreateRecipe() {
      this.$router.push({name: "create"}); //Navigate to Recipe Creation view
    }
  },
};
</script>

<style scoped>
.home {
  text-align: center;
  margin-top: 2rem;
}
button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  margin-top: 1rem;
  cursor: pointer;
}
</style>