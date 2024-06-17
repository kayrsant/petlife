<template>
  <nav class="navbar">
    <router-link to="/" class="nav-button">
      <i class="fas fa-home"></i> Início
    </router-link>
    <router-link to="/dashboard" class="nav-button">
      <i class="fas fa-tachometer-alt"></i> Dashboard
    </router-link>
    <router-link to="/pets" class="nav-button">
      <i class="fas fa-paw"></i> Pets
    </router-link>
    <router-link to="/profile" class="nav-button">
      <i class="fas fa-user-circle"></i> Perfil
    </router-link>
    <button @click="logout" class="nav-button">
      <i class="fas fa-sign-out-alt"></i> Sair
    </button>
  </nav>
</template>

<script>
export default {
  computed: {
    isLoggedIn() {
      const token = localStorage.getItem("token");
      return !!token;
    },
  },
  name: "NavBarLoggedIn",
  methods: {
    logout() {
      localStorage.removeItem("token");
      this.$emit("logout");
      this.$router.push("/");
    },
  },
  created() {
    if (this.isLoggedIn) {
      this.$emit("logado");
    }
  },
};
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css");

.navbar {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #6141ac;
  padding: 10px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-button {
  color: white;
  text-decoration: none;
  padding: 10px 20px;
  margin: 0 10px;
  background-color: #50379c;
  border-radius: 8px;
  display: flex;
  align-items: center;
  transition: background-color 0.3s, transform 0.3s;
}

.nav-button i {
  margin-right: 8px;
}

.nav-button:hover {
  background-color: #6141ac;
  transform: translateY(-2px);
}

.nav-button:active {
  transform: translateY(1px);
}
</style>
