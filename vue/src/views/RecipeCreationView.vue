<template>
    <div>
        <h1>Create a New Recipe</h1>
        <form @submit.prevent="submitRecipe">
            <div>
                <label for="recipe-name">Recipe Name:</label>
                <input id="recipe-name" v-model="recipe.recipe_name" placeholder="Enter recipe name..." required />
            </div>
            <div>
                <label for="description">Description:</label>
                <input id="description" v-model="recipe.description" placeholder="Enter description..." required />
            </div>
            <div>
                <label for="instructions">Preparation Instructions:</label>
                <textarea name="" id="instructions" v-model="recipe.instructions" placeholder="Enter preparation instructions here" cols="68" rows="10" required></textarea>
            </div>
            <button type="submit">Save Recipe</button>
        </form>
    </div>
</template>

<script>
import axios from 'axios';

export default{
    data() {
        return {
            recipe: {
                recipe_name: "",
                description: "",
                instructions: ""
            }
        }
    },

methods: {
    async submitRecipe() {
        try {
        const response = await axios.post("http://localhost:9000/recipes/create", this.recipe, {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`, // Replace with actual token retrieval logic
          },
        });
            alert(response.data);//success message
            this.recipe = {recipe_name: "", description: "", instructions: ""}; //reset form
            this.$router.push({name: "my-recipes"});
        } catch (error) {
            console.error("DIDN'T WORK DUH", error.response?.data || error.message);
            alert("failed to create your recipe.");
        }
    },
}}
</script>

<style scoped>
form {
    max-width: 500px;
    margin: auto;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
}
label {
    display: block;
    margin-bottom: 5 px;
    font-weight: bold;
}
input, textarea {
    width: 100%;
    padding: 8px;
    margin-bottom: 10px;
}
button {
    background-color: #4caf50;
    color: white;
    border: none;
    padding: 10px 20px; 
    cursor: pointer;
}
button:hover{
    background-color: red;
}
</style>