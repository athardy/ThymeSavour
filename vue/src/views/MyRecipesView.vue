<template>
  <div class="my-recipes">
    <h1>My Recipes</h1>

    <!-- Search Filters and Create Recipe Button -->
    <div class="filters-and-create">
      <div class="filter-group">
        <input
          type="text"
          v-model="searchFilters.name"
          @focus="fetchRecipeNames"
          @input="fetchFilteredRecipes"
          placeholder="Filter by name"
          list="name-options"
        />
        <datalist id="name-options">
          <option v-for="name in recipeNames" :key="name" :value="name" />
        </datalist>
        <button v-if="searchFilters.name" @click="clearFilter('name')" class="clear-button">Clear</button>
      </div>

      <div class="filter-group">
        <input
          type="text"
          v-model="searchFilters.ingredient"
          @focus="fetchIngredientNames"
          @input="fetchFilteredRecipes"
          placeholder="Filter by ingredient"
          list="ingredient-options"
        />
        <datalist id="ingredient-options">
          <option v-for="ingredient in ingredientNames" :key="ingredient" :value="ingredient" />
        </datalist>
        <button v-if="searchFilters.ingredient" @click="clearFilter('ingredient')" class="clear-button">Clear</button>
      </div>

      <div class="filter-group">
        <input
          type="text"
          v-model="searchFilters.category"
          @focus="fetchCategoryNames"
          @input="fetchFilteredRecipes"
          placeholder="Filter by category"
          list="category-options"
        />
        <datalist id="category-options">
          <option v-for="category in categoryNames" :key="category" :value="category" />
        </datalist>
        <button v-if="searchFilters.category" @click="clearFilter('category')" class="clear-button">Clear</button>
      </div>

      <!-- Create Recipe Button -->
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
      recipeNames: [],
      ingredientNames: [],
      categoryNames: [],
      searchFilters: {
        name: "",
        ingredient: "",
        category: "",
      },
    };
  },
  methods: {
    fetchRecipes() {
      const userId = this.$store.state.userId; // Ensure that userId is available in your state

      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/my-recipes`, {
          params: { userId }, // Pass userId as a query parameter
        })
        .then((response) => {
          this.recipes = response.data;
        })
        .catch((error) => {
          console.error("Error fetching recipes:", error);
        });
    },

    fetchRecipeNames() {
      const userId = this.$store.state.userId; // Make sure userId is available in your state

      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/names`, {
          params: { userId },
        })
        .then((response) => {
          this.recipeNames = response.data;
        })
        .catch((error) => {
          console.error("Error fetching recipe names:", error);
        });
    },

    fetchIngredientNames() {
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/ingredients`, {
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          this.ingredientNames = response.data;
        })
        .catch((error) => {
          console.error("Error fetching ingredient names:", error);
        });
    },

    fetchCategoryNames() {
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/categories`)
        .then((response) => {
          this.categoryNames = response.data;
        })
        .catch((error) => {
          console.error("Error fetching category names:", error);
        });
    },

    fetchFilteredRecipes() {
      axios
        .get(`${import.meta.env.VITE_REMOTE_API}/recipes/search`, {
          params: {
            name: this.searchFilters.name,
            ingredient: this.searchFilters.ingredient,
            category: this.searchFilters.category,
          },
          headers: { Authorization: `Bearer ${this.$store.state.token}` },
        })
        .then((response) => {
          this.recipes = response.data;
        })
        .catch((error) => {
          console.error("Error fetching filtered recipes:", error);
        });
    },

    clearFilter(field) {
      this.searchFilters[field] = "";
      this.fetchFilteredRecipes();
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
  background-color: #d3f1df; /* Background color */
  font-family: Arial, Helvetica, sans-serif;
}

.filters-and-create {
  display: flex;
  justify-content: space-between;
  gap: 2rem;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
  max-width: 300px;
}

input {
  padding: 0.5rem;
  font-size: 1rem;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.clear-button {
  padding: 0.3rem 1rem;
  background-color: #ff9a9a;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.clear-button:hover {
  background-color: #ff7f7f;
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