<template>
  <div class="recipes-container">
    <h1>My Recipes</h1>
    <div class="grid">
      <div class="recipe-tile" v-for="recipe in recipes" :key="recipe.recipe_id">
        <h2>{{ recipe.recipe_name }}</h2>
        <p><strong>Description: </strong>{{ recipe.description }}</p>
        <p><strong>Preparation Instructions: </strong>{{ recipe.instructions }}</p>
        <p><strong>Ingredients:</strong></p>
        <ul>
          <li v-for="ingredient in recipe.ingredients" :key="ingredient.id">
            {{ ingredient.quantity }} {{ ingredient.unit }} of {{ ingredient.ingredient_name }}
          </li>
        </ul>
        <button @click="deleteRecipe(recipe.recipe_id)" class="delete-btn">Delete Recipe</button>
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
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/my-recipes`, {
          headers: {
            Authorization: `Bearer ${this.$store.state.token}`,
          },
        })
        .then(async (response) => {
          const recipeList = response.data;
          for (const recipe of recipeList) {
            const ingredientsResponse = await axios.get(
              `${import.meta.env.VITE_REMOTE_API}/recipes/${recipe.recipe_id}/ingredients`,
              {
                headers: {
                  Authorization: `Bearer ${this.$store.state.token}`,
                },
              }
            );
            recipe.ingredients = ingredientsResponse.data;
          }
          this.recipes = recipeList;
        })
        .catch((error) => console.error("Error fetching recipes:", error));
    },
    deleteRecipe(recipeId) {
      if (confirm("Are you sure you want to delete this recipe?")) {
        axios
          .delete(`${import.meta.env.VITE_REMOTE_API}/recipes/delete/${recipeId}`, {
            headers: {
              Authorization: `Bearer ${this.$store.state.token}`,
            },
          })
          .then(() => {
            this.recipes = this.recipes.filter(
              (recipe) => recipe.recipe_id !== recipeId
            );
            alert("Recipe deleted successfully.");
          })
          .catch((error) =>
            console.error("Error deleting recipe:", error.response.data)
          );
      }
    },
  },
  mounted() {
    this.fetchRecipes();
  },
};
</script>

<style scoped>
.recipes-container {
  margin: 2rem;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.recipe-tile {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  background-color: #f9f9f9;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.recipe-tile h2 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.recipe-tile ul {
  list-style-type: none;
  padding: 0;
}

.recipe-tile ul li {
  margin-bottom: 0.5rem;
}

.delete-btn {
  background-color: #ff4d4d;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  margin-top: 1rem;
}

.delete-btn:hover {
  background-color: #e63939;
}
</style>