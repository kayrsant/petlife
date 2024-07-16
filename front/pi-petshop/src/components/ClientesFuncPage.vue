<template>
  <div class="clientes">
    <h1>Lista de Clientes</h1>

    <div v-if="clientes.length === 0" class="no-clientes">
      <p>Não há clientes.</p>
    </div>

    <div v-else class="clientes-container">
      <div v-for="cliente in clientes" :key="cliente.id" class="cliente-card">
        <div class="card-content">
          <div class="cliente-info">
            <h2 class="cliente-nome">{{ cliente.nome }}</h2>
            <p class="cliente-details">
              <strong>Email:</strong> {{ cliente.email }}<br />
              <strong>Telefone:</strong> {{ cliente.telefone }}<br />
              <strong>CEP:</strong> {{ cliente.cep }}<br />
            </p>
          </div>

          <div class="card-actions">
            <button
              @click="viewCliente(cliente.id)"
              class="action-button view-button"
            >
              Ver Detalhes
            </button>
            <button
              @click="removeCliente(cliente.id)"
              class="action-button delete-button"
            >
              Excluir
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const API_URL = "http://localhost:8080/petshop";

export default {
  name: "ClienteFuncPage",
  data() {
    return {
      clientes: [],
    };
  },
  created() {
    this.fetchClientes();
  },
  methods: {
    async fetchClientes() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(`${API_URL}/cliente/todos`, config);
        this.clientes = response.data;
      } catch (error) {
        console.error("Erro ao buscar clientes:", error);
      }
    },
    navigateTo(route) {
      this.$router.push({ path: `/clientes/${route}` });
    },
    viewCliente(clienteId) {
      this.$router.push(`/clientes/detalhes/${clienteId}`);
    },
    async removeCliente(clienteId) {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        await axios.delete(`${API_URL}/cliente/${clienteId}`, config);
        this.clientes = this.clientes.filter(
          (cliente) => cliente.id !== clienteId
        );
      } catch (error) {
        console.error("Erro ao remover cliente:", error);
      }
    },
  },
};
</script>

<style scoped>
.clientes {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

.add-button {
  display: block;
  margin: 20px auto;
  padding: 8px 16px;
  background-color: #6141ac;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-button:hover {
  background-color: #4a2e8e;
}

.clientes-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.cliente-card {
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  padding: 10px;
  max-width: 300px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.cliente-info {
  font-size: 14px;
}

.cliente-nome {
  font-size: 16px;
  font-weight: bold;
}

.cliente-details {
  font-size: 12px;
  color: #555;
}

.card-actions {
  display: flex;
  gap: 5px;
  justify-content: space-between;
}

.action-button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 5px 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 12px;
}

.action-button.view-button {
  background-color: #2196f3; /* Azul para Ver Detalhes */
}

.action-button.delete-button {
  background-color: #f44336; /* Vermelho para Excluir */
}

.action-button:hover {
  opacity: 0.9;
}

.delete-button:hover {
  background-color: #c62828;
}

.no-clientes {
  text-align: center;
  color: #888;
}
</style>
