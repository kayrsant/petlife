<template>
  <div class="profile">
    <h1>Perfil</h1>
    <div class="profile-card">
      <div class="profile-item">
        <strong>Nome:</strong>
        <span>{{ nome }}</span>
      </div>
      <div class="profile-item">
        <strong>E-mail:</strong>
        <span>{{ email }}</span>
      </div>
      <div class="profile-item">
        <strong>Telefone:</strong>
        <span>{{ telefone }}</span>
      </div>
      <div class="profile-item">
        <strong>CEP:</strong>
        <span>{{ cep }}</span>
      </div>
    </div>
    <button @click="editProfile" class="btn-editar">Editar Perfil</button>
    <NotificationBar ref="notification" />
  </div>
</template>

<script>
import NotificationBar from "@/components/NotificationBar.vue";
import AuthService from "@/services/AuthService";

export default {
  components: {
    NotificationBar,
  },
  data() {
    return {
      nome: "",
      email: "",
      telefone: "",
      cep: "",
    };
  },
  async mounted() {
    await this.loadProfileData();
  },
  methods: {
    async loadProfileData() {
      try {
        const response = await AuthService.getProfile();
        this.nome = response.nome;
        this.email = response.email;
        this.telefone = response.telefone || "";
        this.cep = response.cep || "";
      } catch (error) {
        console.error("Erro ao carregar perfil:", error);
        this.$refs.notification.show("Erro ao carregar perfil!", "error");
      }
    },
    editProfile() {
      this.$router.push("/profile/editar");
    },
  },
};
</script>

<style scoped>
.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  text-align: center;
  padding: 20px;
}

.profile-card {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  max-width: 400px;
  background: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-item {
  display: flex;
  justify-content: space-between;
}

strong {
  font-weight: bold;
}

.btn-editar {
  margin-top: 20px;
  background-color: #68cd86;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-editar:hover {
  background-color: #57b574;
}
</style>
