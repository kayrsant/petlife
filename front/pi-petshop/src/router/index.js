import { createRouter, createWebHistory } from 'vue-router';

import Home from '../components/HomePage.vue';
import Dashboard from '../components/DashboardPage.vue';
import Login from '../components/LoginPage.vue';
import Profile from '@/components/ProfilePage.vue';
import Pets from '@/components/PetsPage.vue';
import PetCreate from '@/components/PetCreate.vue';
import PetEdit from '@/components/PetEdit.vue';
import Agendamentos from '@/components/AgendamentosPage.vue';
import AgendamentoEdit from '@/components/AgendamentosEdit.vue';
import AgendamentoCreate from '@/components/AgendamentoCreate.vue';
import ProfileEditPage from '@/components/ProfileEditPage.vue';
import PetsCadastrados from '@/components/PetsCadastradosPage.vue';
import AgendamentosFunc from '@/components/AgendamentoFuncPage.vue';
import AgendamentoCreateFunc from '@/components/AgendamentoCreateFuncPage.vue';
import ClientesFunc from '@/components/ClientesFuncPage.vue';
import ClienteDetailPage from '@/components/ClienteDetailPage.vue';
import ClienteCreatePage from '@/components/ClienteCreatePage.vue';
import ClienteUpdatePage from '@/components/ClienteUpdatePage.vue';
import PetEditFunc from '@/components/PetsEditFuncPage.vue'

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
    path: '/profile/editar',
    name: 'ProfileEdit',
    component: ProfileEditPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/pets',
    name: 'Pets',
    component: Pets,
    meta: { requiresAuth: true }
  },
  {
    path: '/func/pets',
    name: 'PetsCadastrados',
    component: PetsCadastrados,
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
  },
  {
    path: '/func/pets/editar/:id',
    name: 'PetEdit',
    component: PetEditFunc,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/agendamentos',
    name: 'Agendamentos',
    component: Agendamentos,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/func/agendamentos',
    name: 'AgendamentosFunc',
    component: AgendamentosFunc,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/func/clientes',
    name: 'ClientesFunc',
    component: ClientesFunc,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/clientes/detalhes/:id',
    name: 'ClientesDetailFunc',
    component: ClienteDetailPage,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/func/clientes/create',
    name: 'ClientesFunc',
    component: ClienteCreatePage,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/func/clientes',
    name: 'ClientesFunc',
    component: ClientesFunc,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/clientes/update/:id',
    name: 'ClientesUpdateFunc',
    component: ClienteUpdatePage,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: "/agendamentos/editar/:id",
    component: AgendamentoEdit,
    meta: { requiresAuth: true },
    props: true,
  },
  {
    path: "/agendamentos/create",
    component: AgendamentoCreate,
    meta: { requiresAuth: true },
    props: true,
  },
  {
    path: '/func/agendamentos/create',
    name: 'AgendamentoCreateFunc',
    component: AgendamentoCreateFunc,
    meta: { requiresAuth: true },
    props: true
  },
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
