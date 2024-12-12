import { createStore as _createStore } from 'vuex';
import axios from 'axios';

export function createStore() {
  const currentToken = localStorage.getItem('token') || '';
  const currentUser = JSON.parse(localStorage.getItem('user')) || {};

  let store = _createStore({
    state: {
      token: currentToken,
      user: currentUser,
    },
    mutations: {
      SET_AUTH_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        console.log("Auth token set:", token); // Debugging log
      },
      SET_USER(state, user) {
        console.log("Setting user:", user); // Debugging log
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      },
      LOGOUT(state) {
        console.log("Logging out user"); // Debugging log
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        state.token = '';
        state.user = {};
        delete axios.defaults.headers.common['Authorization'];
      },
    },
    getters: {
      userId(state) {
        return state.user?.id || null; // Safeguard against undefined `id`
      },
      isAuthenticated(state) {
        return !!state.token; // Return true if a token exists
      },
    },
  });
  return store;
}