import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import AuthService from './services/AuthService.js';

AuthService.setAuthInterceptor();

createApp(App).use(router).mount('#app');
