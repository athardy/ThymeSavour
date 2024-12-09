<template>
  <div id="capstone-app">
    <aside id="sidebar">
      <ul>
        <li v-if="!$store.state.token">
          <router-link v-bind:to="{ name: 'login' }">Login</router-link>
        </li>
        <li v-if="!$store.state.token">
          <router-link v-bind:to="{ name: 'register' }">Register</router-link>
        </li>
        <li v-if="$store.state.token">
          <router-link v-bind:to="{ name: 'home' }">Home</router-link>
        </li>
        <li v-if="$store.state.token">
          <router-link v-bind:to="{ name: 'my-recipes' }">View My Recipes</router-link>
        </li>
        <li v-if="$store.state.token">
          <router-link v-bind:to="{ name: 'create' }">Create New Recipe</router-link>
        </li>
        <li v-if="$store.state.token">
          <router-link v-bind:to="{ name: 'logout' }">Logout</router-link>
        </li>
      </ul>
    </aside>
    <main>
      <router-view />
    </main>
  </div>
</template>

<style scoped>
#capstone-app {
  display: flex;
  height: 100vh;
}

#sidebar {
  width: 200px;
  background-color: #f4f4f4;
  padding: 1rem;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
}

#sidebar ul {
  list-style: none;
  padding: 0;
}

#sidebar li {
  margin-bottom: 1rem;
}

#sidebar a {
  text-decoration: none;
  color: #333;
  font-weight: bold;
}

#sidebar a:hover {
  color: #007bff;
}

main {
  flex: 1;
  padding: 2rem;
}
</style>