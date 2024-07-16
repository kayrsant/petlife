<template>
  <div class="dashboard">
    <h1>Bem-vindo!</h1>
    <div class="card-container">
      <!-- Cards de Cliente -->
      <div class="client-cards">
        <div class="card" @click="navigateTo('pets')">
          <i class="fas fa-paw"></i>
          <p>Meus Pets</p>
        </div>
        <div class="card" @click="navigateTo('agendamentos')">
          <i class="fas fa-calendar-check"></i>
          <p>Agendamentos</p>
        </div>
        <div class="card" @click="navigateTo('profile')">
          <i class="fas fa-user"></i>
          <p>Perfil</p>
        </div>
      </div>

      <!-- Cards de Funcionário -->
      <div v-if="isFuncionario || isAdmin" class="employee-cards">
        <div class="card" @click="navigateTo('func/pets')">
          <i class="fas fa-paw"></i>
          <p>Pets Cadastrados</p>
        </div>

        <div
          v-if="isFuncionario || isAdmin"
          class="card"
          @click="navigateTo('func/agendamentos')"
        >
          <i class="fas fa-calendar-check"></i>
          <p>Todos os Agendamentos</p>
        </div>

        <div class="card" @click="navigateTo('func/clientes')">
          <i class="fas fa-users"></i>
          <p>Clientes</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const API_URL = "http://localhost:8080/petshop";

export default {
  data() {
    return {
      isFuncionario: false,
      isAdmin: false,
    };
  },
  created() {
    this.checkUserType();
  },
  methods: {
    async checkUserType() {
      try {
        const token = localStorage.getItem("token");
        if (!token) throw new Error("Token não encontrado");

        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };

        // Faz uma requisição para obter os dados do funcionário logado
        const { data: funcionario } = await axios.get(
          `${API_URL}/funcionario`,
          config
        );

        // Decodifique o token para obter o payload
        const decodedPayload = JSON.parse(atob(token.split(".")[1]));
        const { roles = [], sub } = decodedPayload;

        // Verifica se o email do funcionário corresponde ao email do token
        const isFuncionario = funcionario.email === sub;
        const isAdmin =
          roles.includes("ROLE_ADMIN") || roles.includes("ROLE_FUNCIONARIO");

        this.isFuncionario = isFuncionario;
        this.isAdmin = isAdmin;

        console.log("Verificação de funcionário:", this.isFuncionario);
        console.log("Verificação de admin:", this.isAdmin);
      } catch (error) {
        console.error("Erro ao verificar tipo de usuário:", error);
      }
    },
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
  flex-direction: column;
  gap: 20px;
  margin-top: 30px;
}

.client-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.employee-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
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
