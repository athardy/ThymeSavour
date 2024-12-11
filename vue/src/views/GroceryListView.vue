<template>
    <div class="grocery-list">
      <h1>Your Grocery List</h1>
      <ul>
        <li v-for="item in groceryList" :key="item.ingredient_id">
          {{ item.total_quantity }} {{ item.unit }} of {{ item.ingredient_name }}
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        groceryList: [],
      };
    },
    methods: {
      fetchGroceryList() {
        axios
          .get(`${import.meta.env.VITE_REMOTE_API}/grocery-list`, {
            headers: { Authorization: `Bearer ${this.$store.state.token}` },
          })
          .then((response) => {
            this.groceryList = response.data;
          })
          .catch((error) => console.error("Error fetching grocery list:", error));
      },
    },
    mounted() {
      this.fetchGroceryList();
    },
  };
  </script>
  
  <style scoped>
  .grocery-list {
    max-width: 600px;
    margin: auto;
  }
  </style>