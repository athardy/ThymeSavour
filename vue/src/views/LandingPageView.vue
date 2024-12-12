<template>
  <div class="landing-page">
    <header>
      <img src="src/assets/main_logo.png" alt="Thyme Savour Logo" class="logo">
    </header>

    <main>
      <!-- Section 1: Introduction to Thyme Savour -->
      <section class="intro-section">
        <div class="text">
          <h2>Welcome to Thyme Savour!</h2>
          <p>
            Organize your meals, create recipes, and manage your grocery list
            effortlessly. Our platform helps you save time, eat healthier, and
            get inspired by a world of delicious meals.
          </p>
        </div>
        <div class="image">
          <img src="src/assets/landing_welcome.png" alt="Intro Image" />
        </div>
      </section>

      <!-- Section 2: Features and Benefits -->
      <section class="features-section">
        <div class="image">
          <img src="src/assets/landing_features.png" alt="Features Image" />
        </div>
        <div class="text">
          <h2>Features You’ll Love</h2>
          <p>
            Whether you're tracking your nutrition, planning meals for the
            week, or creating new recipes, Thyme Savour has everything you need
            in one place.
          </p>
        </div>
      </section>

      <!-- Section 3: Get Started Today -->
      <section class="get-started-section">
        <div class="text">
          <h2>Get Started Today</h2>
          <p>
            Ready to make meal planning simple and fun? Sign up now and start
            building your personalized meal plans and grocery lists.
          </p>
        </div>
        <div class="image">
          <img src="src/assets/landing_signup.png" alt="Get Started Image" />
        </div>
      </section>
    </main>
  </div>
</template>

<script>
export default {
  name: "LandingPageView",
};
</script>

<style scoped>
/* General Styling */
.landing-page {
  font-family: Arial, Helvetica, sans-serif;
  background-color: #D3F1DF; /* Main background color */
  min-height: 100vh; /* Ensures full viewport height */
  display: flex;
  flex-direction: column;
  padding: 0;
}

header {
  text-align: center;
  margin-bottom: 3rem; /* Adjust space between header and body */
}

.logo {
  width: 150px;
  height: auto;
  margin-bottom: 1.5rem;
  background-color: transparent;
}

h2 {
  font-size: 2rem;
  color: #85A98F;
  margin-bottom: 0.8rem;
}

p {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 0.8rem;
}

img {
  width: 90%; /* Reduced image size */
  max-width: 300px; /* Maximum image size */
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* Section Layouts */
main {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  flex-grow: 1;
}

section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 3rem;
  padding: 20px 0;
}

section.reverse-layout {
  flex-direction: row-reverse;
}

section .text {
  flex: 1;
  padding: 0 2rem;
}

section .image {
  flex: 1;
  padding: 0 2rem;
}

/* Mobile Responsiveness */
@media (max-width: 768px) {
  section {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  section.reverse-layout {
    flex-direction: column;
  }

  section .text,
  section .image {
    padding: 0;
  }

  section .image {
    margin-top: 1.5rem;
  }

  .logo {
    width: 120px;
  }

  h2 {
    font-size: 1.8rem;
  }

  p {
    font-size: 1.1rem;
  }
}
</style>