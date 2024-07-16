import axios from 'axios';

const API_URL = 'http://localhost:8080/petshop';

const AuthService = {
  async getProfile() {
    const token = this.getToken();
    const config = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };
    try {
      const response = await axios.get(`${API_URL}/cliente`, config);
      return response.data;
    } catch (error) {
      console.error('Erro ao obter perfil:', error);
      throw error;
    }
  },
  async updateProfile(data) {
    console.log('Enviando requisição de atualização:', data);
    const token = this.getToken();
    const config = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };
    try {
      const response = await axios.put(`${API_URL}/cliente`, data, config);
      console.log('Resposta de atualização recebida:', response.data);
      return response.data;
    } catch (error) {
      console.error('Erro ao fazer atualização:', error);
      throw error;
    }
  },
  login(credentials) {
    console.log('Enviando requisição de login:', credentials);
    return axios.post(`${API_URL}/auth/login`, credentials)
      .then(response => {
        console.log('Resposta de login recebida:', response.data);
        const token = response.data.token;
        localStorage.setItem('token', token);
        return response.data;
      })
      .catch(error => {
        console.error('Erro ao fazer login:', error);
        throw error;
      });
  },
  register(userInfo) {
    console.log('Enviando requisição de registro:', userInfo);
    return axios.post(`${API_URL}/auth/register`, userInfo)
      .then(response => {
        console.log('Resposta de registro recebida:', response.data);
        return response.data;
      })
      .catch(error => {
        console.error('Erro ao registrar:', error);
        throw error;
      });
  },
  setAuthInterceptor() {
    axios.interceptors.request.use(
      config => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      error => {
        return Promise.reject(error);
      }
    );
  },
  getToken() {
    const token = localStorage.getItem('token');
    return token;
  }
};

export default AuthService;
