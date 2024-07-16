<template>
  <div class="agendamentos">
    <h1>Lista de Agendamentos</h1>
    <button @click="navigateTo('create')" class="add-button">
      Adicionar Agendamento
    </button>

    <div v-if="filteredAgendamentos.length === 0" class="no-agendamentos">
      <p>Não há agendamentos.</p>
    </div>

    <div v-else class="agendamentos-container">
      <div
        v-for="agendamento in filteredAgendamentos"
        :key="agendamento.id"
        class="agendamento-card"
      >
        <div class="card-content">
          <div class="pet-info">
            <h2 class="pet-name">{{ agendamento.pet.nome }}</h2>
            <p class="pet-details">
              {{ agendamento.pet.tipo }} | {{ agendamento.pet.raca }}
              {{ agendamento.pet.sexo }} | {{ agendamento.pet.idade }} anos
            </p>
          </div>

          <div class="data-info">
            <p class="data-text">{{ formatDate(agendamento.data) }}</p>
            <p class="hora-text">{{ formatTime(agendamento.data) }}</p>
          </div>

          <div class="funcionario-info">
            <p class="funcionario-text">
              <strong>Funcionário:</strong> {{ agendamento.funcionario.nome }}
            </p>
          </div>

          <div class="servicos-info">
            <h3>Serviços:</h3>
            <ul>
              <li v-for="servico in agendamento.servicos" :key="servico.id">
                {{ servico.descricao }} (R$ {{ servico.valor.toFixed(2) }})
              </li>
            </ul>
          </div>

          <p :class="statusClass(agendamento.status)" class="status-info">
            {{ agendamento.status }}
          </p>

          <p class="valor-info">R$ {{ agendamento.valor.toFixed(2) }}</p>

          <div class="card-actions">
            <button
              @click="editAgendamento(agendamento.id)"
              class="action-button edit-button"
            >
              Editar
            </button>
            <button
              @click="removeAgendamento(agendamento.id)"
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
  name: "AgendamentosPage",
  data() {
    return {
      agendamentos: [],
    };
  },
  computed: {
    filteredAgendamentos() {
      return this.agendamentos;
    },
  },
  created() {
    this.fetchAgendamentos();
  },
  methods: {
    async fetchAgendamentos() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(
          `${API_URL}/agendamento/cliente`,
          config
        );
        this.agendamentos = response.data.sort((a, b) => {
          const statusOrder = {
            EM_ANDAMENTO: 1,
            ABERTO: 2,
            FINALIZADO: 3,
          };
          return (
            statusOrder[a.status] - statusOrder[b.status] ||
            new Date(a.data) - new Date(b.data)
          );
        });
      } catch (error) {
        console.error("Erro ao buscar agendamentos:", error);
      }
    },
    navigateTo(route) {
      this.$router.push({ path: `/agendamentos/${route}` });
    },
    editAgendamento(agendamentoId) {
      this.$router.push(`/agendamentos/editar/${agendamentoId}`);
    },
    async removeAgendamento(agendamentoId) {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        await axios.delete(`${API_URL}/agendamento/${agendamentoId}`, config);
        this.agendamentos = this.agendamentos.filter(
          (agendamento) => agendamento.id !== agendamentoId
        );
      } catch (error) {
        console.error("Erro ao remover agendamento:", error);
      }
    },
    formatDate(date) {
      const dt = new Date(date);
      return dt.toLocaleDateString();
    },
    formatTime(date) {
      const dt = new Date(date);
      const hours = dt.getHours().toString().padStart(2, "0");
      const minutes = dt.getMinutes().toString().padStart(2, "0");
      return `${hours}:${minutes}`;
    },
    statusClass(status) {
      switch (status) {
        case "ABERTO":
          return "status-aberto";
        case "FINALIZADO":
          return "status-finalizado";
        case "EM_ANDAMENTO":
          return "status-em-andamento";
        default:
          return "";
      }
    },
  },
};
</script>

<style scoped>
.agendamentos {
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

.agendamentos-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.agendamento-card {
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  padding: 10px;
  max-width: 300px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.pet-info {
  font-size: 14px;
}

.pet-name {
  font-size: 16px;
  font-weight: bold;
}

.pet-details {
  font-size: 12px;
  color: #555;
}

.data-info {
  font-size: 12px;
  color: #777;
}

.data-text {
  margin: 0;
}

.hora-text {
  margin: 0;
}

.funcionario-info {
  font-size: 12px;
  color: #444;
}

.funcionario-text {
  margin: 0;
}

.servicos-info {
  font-size: 12px;
  margin-top: 5px;
}

.servicos-info h3 {
  margin-bottom: 5px;
  font-weight: bold;
}

.servicos-info ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.servicos-info li {
  background-color: #f0f0f0;
  padding: 3px 6px;
  border-radius: 4px;
  margin-bottom: 2px;
  font-size: 12px;
}

.status-info {
  font-size: 12px;
  font-weight: bold;
  text-align: center;
  padding: 4px 6px;
  border-radius: 4px;
  color: #fff;
}

.status-aberto {
  background-color: #4caf50; /* Verde */
}

.status-finalizado {
  background-color: #f44336; /* Vermelho */
}

.status-em-andamento {
  background-color: #ffeb3b; /* Amarelo */
  color: #000;
}

.valor-info {
  font-size: 14px;
  font-weight: bold;
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

.action-button.edit-button {
  background-color: #2196f3; /* Azul para Editar */
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

.no-agendamentos {
  text-align: center;
  color: #888;
}
</style>
