<template>
  <div class="my-recipes">
    <h1>My Recipes</h1>

    <!-- Create Recipe Button -->
    <div class="create-recipe">
      <router-link to="/create-recipe" class="create-button">
        Create Recipe
      </router-link>
    </div>

    <!-- Recipes List -->
    <div class="recipes-list">
      <div class="recipe-card" v-for="recipe in recipes" :key="recipe.recipe_id">
        <h3>{{ recipe.recipe_name }}</h3>
        <p>{{ recipe.description }}</p>
        <div class="actions">
          <router-link :to="{ name: 'recipe-details', params: { id: recipe.recipe_id } }">
            <button class="view-button">View</button>
          </router-link>
          <button @click="deleteRecipe(recipe.recipe_id)" class="delete-button">Delete</button>
        </div>
      </div>
    </div>
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
  methods: {
    fetchRecipes() {
      const userId = this.$store.getters.userId;

      if (!userId) {
        console.error("User ID is not defined. Cannot fetch recipes.");
        return;
      }

      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/my-recipes`, {
          params: { userId },
        })
        .then((response) => {
          this.recipes = response.data;
        })
        .catch((error) => {
          console.error("Error fetching recipes:", error);
        });
    },

    deleteRecipe(recipeId) {
      axios
        .delete(`${import.meta.env.VITE_REMOTE_API}/recipes/${recipeId}`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then(() => {
          this.fetchRecipes();
        })
        .catch((error) => {
          console.error("Error deleting recipe:", error);
        });
    },
  },
  mounted() {
    this.fetchRecipes();
  },
};
</script>

<style scoped>
.my-recipes {
  padding: 2rem;
  background-color: #d3f1df;
  font-family: Arial, Helvetica, sans-serif;
}

.create-recipe {
  margin-bottom: 2rem;
  text-align: center;
}

.create-button {
  padding: 0.5rem 1rem;
  background-color: #64a472;
  color: white;
  text-decoration: none;
  border-radius: 5px;
}

.create-button:hover {
  background-color: #4d8c5e;
}

.recipes-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.recipe-card {
  padding: 1.5rem;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.recipe-card h3 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.recipe-card p {
  font-size: 1rem;
  color: #666;
}

.actions {
  display: flex;
  gap: 1rem;
}

.view-button {
  background-color: #64a472;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
}

.view-button:hover {
  background-color: #4d8c5e;
}

.delete-button {
  background-color: #ff9a9a;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #ff7f7f;
}
</style>