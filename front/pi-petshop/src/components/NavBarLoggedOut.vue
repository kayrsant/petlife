<template>
  <nav class="navbar">
    <router-link to="/" class="nav-button">
      <i class="fas fa-home"></i> Início
    </router-link>
    <router-link to="/login" class="nav-button">
      <i class="fas fa-sign-in-alt"></i> Login
    </router-link>
    <router-link to="/login" class="nav-button">
      <i class="fas fa-user-plus"></i> Registrar
    </router-link>
  </nav>
</template>

<script>
export default {
  name: "NavBarLoggedOut",
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
