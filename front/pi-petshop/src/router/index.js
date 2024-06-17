import { createRouter, createWebHistory } from 'vue-router';

import Home from '../components/HomePage.vue';
import Dashboard from '../components/DashboardPage.vue';
import Login from '../components/LoginPage.vue';
import Profile from '@/components/ProfilePage.vue';
import Pets from '@/components/PetsPage.vue';
import PetCreate from '@/components/PetCreate.vue';
import PetEdit from '@/components/PetEdit.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    beforeEnter: (to, from, next) => {
      const token = localStorage.getItem('token');
      if (!token) {
        next('/login');
      } else {
        next();
      }
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '/pets',
    name: 'Pets',
    component: Pets,
    meta: { requiresAuth: true }
  },
  {
    path: '/pets/create',
    name: 'PetCreate',
    component: PetCreate,
    meta: { requiresAuth: true }
  },
  {
    path: '/pets/editar/:id',
    name: 'PetEdit',
    component: PetEdit,
    meta: { requiresAuth: true },
    props: true
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!localStorage.getItem('token')) {
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
