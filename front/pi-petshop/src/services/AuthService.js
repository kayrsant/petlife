import axios from 'axios';

const API_URL = 'http://localhost:8080/petshop';

const AuthService = {
  async getProfile() {
    const token = this.getToken();
    const config = {
      headers: {
        Authorization: `Bearer ${token}`
      },
      params: {
        fields: this.getEmailFromToken(token)
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
  getEmailFromToken(token) {
    if (!token) {
      console.error('Token não encontrado no localStorage.');
      return null;
    }

    try {
      // Decodifica o token JWT para obter o payload (carga útil)
      const payloadBase64Url = token.split('.')[1];
      const payloadBase64 = payloadBase64Url.replace(/-/g, '+').replace(/_/g, '/');
      const payload = JSON.parse(atob(payloadBase64));

      const email = payload.sub;
      return email;
    } catch (error) {
      console.error('Erro ao decodificar o token:', error);
      return null;
    }
  },
  updateProfile(credentials) {
    console.log('Enviando requisição de atualização:', credentials);
    const token = this.getToken();
    const config = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };
    return axios.put(`${API_URL}/cliente`, credentials, config)
      .then(response => {
        console.log('Resposta de atualização recebida:', response.data);
        return response.data;
      })
      .catch(error => {
        console.error('Erro ao fazer atualização:', error);
        throw error;
      });
  },
  getToken() {
    const token = localStorage.getItem('token');
    return token;
  }
};

export default AuthService;
