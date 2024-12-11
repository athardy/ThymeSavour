import axios from 'axios';

const http = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API || 'http://localhost:9000',
});

export default {
  async login(user) {
    try {
      const response = await http.post('/auth/login', user);
      return response.data;
    } catch (error) {
      console.error('Error during login:', error);
      throw error;
    }
  },

  async register(user) {
    try {
      const response = await http.post('/auth/register', user);
      return response.data;
    } catch (error) {
      console.error('Error during registration:', error);
      throw error;
    }
  },

  async checkUsernameAvailability(username) {
    try {
      const response = await http.get('/auth/check-username', {
        params: { username },
      });
      return response.data;
    } catch (error) {
      console.error('Error checking username availability:', error);
      throw error;
    }
  },

  async validateToken(token) {
    try {
      const response = await http.get('/auth/validate-token', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error validating token:', error);
      throw error;
    }
  },
};