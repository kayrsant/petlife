<!-- src/components/Dashboard.vue -->
<template>
  <div class="dashboard">
    <h1>Bem-vindo!</h1>
    <div class="card-container">
      <div class="card" @click="navigateTo('pets')">
        <i class="fas fa-paw"></i>
        <p>Meus Pets</p>
      </div>
      <div class="card" @click="navigateTo('profile')">
        <i class="fas fa-user"></i>
        <p>Editar Perfil</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    navigateTo(route) {
      this.$router.push(`/${route}`);
    },
  },
};
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 20px;
}

.card-container {
  display: flex;
  gap: 20px;
  margin-top: 30px;
}

.card {
  background-color: #6141ac;
  color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.card:hover {
  transform: translateY(-5px);
}

.card i {
  font-size: 2em;
  margin-bottom: 10px;
}
</style>
