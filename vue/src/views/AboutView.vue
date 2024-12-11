<template>
    <div class="about-page">
      <header>
        <img src="src/assets/main_logo.png" alt="Thyme Savour Logo" class="logo">
      </header>
    
      <main class="about-content">
        <!-- What Is It Section -->
        <section class="about-section">
          <div class="text">
            <h2>What Is It?</h2>
            <p>
              ThymeSavour is your all-in-one solution for meal planning, recipe creation, and
              grocery shopping. Our platform is designed to eliminate the hassle of meal prep
              and grocery shopping, making your life more efficient and more enjoyable.
            </p>
          </div>
        </section>
    
        <!-- How Does It Work Section -->
        <section class="about-section">
          <div class="text">
            <h2>How Does It Work?</h2>
            <p>
              Simply select your desired date range and browse our collection of delicious recipes—
              or create your own! Once you've curated your meal plan, we’ll generate a comprehensive
              shopping list, ensuring you have everything you need while saving you both time and money
              at the store.
            </p>
          </div>
        </section>
    
        <!-- Why Choose Us Section -->
        <section class="about-section">
          <div class="text">
            <h2>Why Choose ThymeSavour?</h2>
            <ul>
              <li>Customize Your Meal Plan: Tailor your meals to fit your schedule and dietary preferences.</li>
              <li>Easy Recipe Creation: Add personal touches to your favorite dishes or explore new flavors.</li>
              <li>Smart Shopping List: No more guessing—get precise quantities of ingredients based on your selected recipes.</li>
            </ul>
          </div>
        </section>
    
        <!-- Developers Section -->
        <section class="about-section">
          <div class="text">
            <h2>Developers</h2>
            <p>
              Aaron Hardy<br />
              Aaron Laning<br />
              Adam Callanan<br />
              Adam Lukachek<br />
              John Fay
            </p>
          </div>
        </section>
      </main>
    </div>
  </template>
  
  <script>
  export default {
    name: "AboutView",
  };
  </script>
  
  <style scoped>
  .about-page {
    font-family: Arial, Helvetica, sans-serif;
    background-color: #D3F1DF; /* Main background color */
    padding: 3rem 1rem;
  }
  
  header {
    text-align: center;
    margin-bottom: 3rem;
  }
  
  .logo {
    width: 150px;  /* Same size as the landing page logo */
    height: auto;
    margin-bottom: 2rem;
  }
  
  h2 {
    font-size: 2rem;
    color: #85A98F;
    margin-bottom: 1rem;
  }
  
  p {
    font-size: 1.2rem;
    color: #333;
  }
  
  ul {
    list-style-type: disc;
    padding-left: 20px;
  }
  
  .about-section {
    margin-bottom: 4rem;
  }
  
  .text {
    padding-right: 2rem;
  }
  
  @media (max-width: 768px) {
    .about-section {
      text-align: center;
    }
  
    .main-button {
      width: 100%;
    }
  }
  </style>