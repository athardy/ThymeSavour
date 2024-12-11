<template>
  <div class="auth-page">
    <form @submit.prevent="register">
      <h1>Create Account</h1>

      <!-- Username Validation Error -->
      <div role="alert" v-if="usernameError" class="alert">
        {{ usernameError }}
      </div>

      <!-- Form Input Fields -->
      <div class="form-input-group">
        <label for="username">Username</label>
        <input
          type="text"
          id="username"
          v-model="user.username"
          @blur="checkUsername"
          required
          autofocus
        />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <div class="form-input-group">
        <label for="confirmPassword">Confirm Password</label>
        <input
          type="password"
          id="confirmPassword"
          v-model="user.confirmPassword"
          required
        />
      </div>

      <!-- Password Match Validation Error -->
      <div role="alert" v-if="passwordError" class="alert">
        {{ passwordError }}
      </div>

      <!-- Submit Button -->
      <button type="submit" :disabled="usernameError || passwordError">
        Create Account
      </button>

      <!-- Link to Login -->
      <p>
        <router-link :to="{ name: 'login' }">Already have an account? Log in.</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        role: "user", // Default role
      },
      usernameError: null,
      passwordError: null,
    };
  },
  methods: {
    methods: {
  checkUsername() {
    // Call API to check username availability
    authService
      .checkUsernameAvailability(this.user.username)
      .then((response) => {
        // Check if the response and response.data exist
        if (response && response.data) {
          // Now safely access the 'exists' property
          this.usernameError = response.data.exists
            ? "Username is already taken."
            : null;
        } else {
          // Handle case where response.data is undefined or not in expected format
          this.usernameError = "Error checking username availability.";
        }
      })
      .catch((error) => {
        console.error("Error checking username availability:", error);
        this.usernameError = "Error checking username availability.";
      });
  },
},
    register() {
      // Check if passwords match before proceeding
      if (this.user.password !== this.user.confirmPassword) {
        this.passwordError = "Passwords do not match.";
        return;
      } else {
        this.passwordError = null;
      }

      // Attempt registration
      authService
        .register(this.user)
        .then(() => {
          alert("Registration successful! Please log in.");
          this.$router.push({ name: "login", query: { registration: "success" } });
        })
        .catch((error) => {
          console.error("Error during registration:", error);
          alert("An error occurred during registration. Please try again.");
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