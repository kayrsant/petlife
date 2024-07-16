<template>
  <div class="cliente-detail">
    <h1>Detalhes do Cliente</h1>
    <div v-if="cliente">
      <p><strong>Nome:</strong> {{ cliente.nome }}</p>
      <p><strong>Email:</strong> {{ cliente.email }}</p>
      <p><strong>Telefone:</strong> {{ cliente.telefone }}</p>
      <p><strong>CEP:</strong> {{ cliente.cep }}</p>
      <p><strong>CPF:</strong> {{ cliente.cpf }}</p>
      <p><strong>Role:</strong> {{ role }}</p>
      <button @click="navigateTo('update')" class="edit-button">Editar</button>
    </div>
    <div v-else>
      <p>Cliente não encontrado.</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const API_URL = "http://localhost:8080/petshop";

export default {
  name: "ClienteDetailPage",
  data() {
    return {
      cliente: null,
      role: null,
    };
  },
  created() {
    this.fetchCliente();
  },
  methods: {
    async fetchCliente() {
      const clienteId = this.$route.params.id;
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(
          `${API_URL}/cliente/${clienteId}`,
          config
        );
        this.cliente = response.data;
        this.fetchUserRole(this.cliente.email);
      } catch (error) {
        console.error("Erro ao buscar cliente:", error);
      }
    },
    async fetchUserRole(email) {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(
          `${API_URL}/user/user-role?email=${email}`,
          config
        );
        this.role = response.data.role;
      } catch (error) {
        console.error("Erro ao buscar role do usuário:", error);
      }
    },
    navigateTo(route) {
      this.$router.push(`/clientes/${route}/${this.cliente.id}`);
    },
  },
};
</script>

<style scoped>
.cliente-detail {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

p {
  margin-bottom: 10px;
}

.edit-button {
  display: block;
  margin: 20px auto;
  padding: 10px 20px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-button:hover {
  background-color: #1976d2;
}
</style>
