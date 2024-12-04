<template>
    <div>
      <h1>My Recipes</h1>
      <ul>
        <li v-for="recipe in recipes" :key="recipe.recipe_id">
          <h2>{{ recipe.recipe_name }}</h2>
          <p><strong>Description: </strong>{{ recipe.description }}</p>
          <p><strong>Preparation Instructions: </strong>{{ recipe.instructions }}</p>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        recipes: [],
      };
    },
    mounted() {
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/my-recipes`, {
          headers: {
            Authorization: `Bearer ${this.$store.state.token}`,
          },
        })
        .then(response => {
          this.recipes = response.data;
        })
        .catch(error => console.error(error));
    },
  };
  </script>