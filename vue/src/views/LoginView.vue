<template>
  <div class="auth-page">
    <form v-on:submit.prevent="login">
      <h1>Please Sign In</h1>
      <div role="alert" v-if="invalidCredentials" class="alert">
        Invalid username or password!
      </div>
      <div role="alert" v-if="$route.query.registration">
        Registration successful! Please log in.
      </div>
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <button type="submit" class="submit-btn">Sign In</button>
      <p>
        <router-link v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: { username: "", password: "" },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          console.log("Login successful, token received:", response.token);
          this.$store.commit("SET_AUTH_TOKEN", response.token);
          this.$store.commit("SET_USER", response.user);
          this.$router.push({ name: "home" });
        })
        .catch((error) => {
          console.error("Error during login:", error.response?.data || error.message);
          this.invalidCredentials = true;
        });
    },
  },
};
</script>

<style scoped>
.auth-page {
  max-width: 400px;
  margin: auto;
  text-align: center;
  background-color: #ffffff;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  font-family: Arial, Helvetica, sans-serif; /* Match font family */
}

h1 {
  margin-bottom: 1.5rem;
  color: #85A98F; /* Green color for the title */
  font-size: 2rem;
}

.form-input-group {
  margin-bottom: 1rem;
}

label {
  font-size: 1.1rem;
  color: #333;
}

input {
  width: 100%;
  padding: 0.8rem;
  font-size: 1rem;
  border: 1px solid #85A98F; /* Green border color */
  border-radius: 5px;
  margin-top: 0.5rem;
}

button {
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #64A472; /* Green button */
  color: white;
  width: 100%;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  background-color: #4D8C5E; /* Darker green for hover */
  transform: translateY(-2px);
}

button:active {
  background-color: #3B6B46; /* Even darker green on click */
  transform: translateY(0);
}

.alert {
  color: red;
  margin-bottom: 1rem;
}

p {
  font-size: 1rem;
  color: #333;
}

router-link {
  color: #64A472; /* Green color for the link */
  font-weight: bold;
  text-decoration: none;
}

router-link:hover {
  text-decoration: underline;
}
</style>