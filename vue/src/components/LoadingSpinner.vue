<template>
    <div v-if="isLoading" class="loading-overlay">
      <img src="@/assets/loading_animation.gif" alt="Loading..." />
    </div>
  </template>
  
  <script>
  export default {
    props: ['isLoading'],
  };
  </script>
  
  <style>
  .loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
  }
  </style>