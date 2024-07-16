<template>
  <div class="add-agendamento">
    <h1>Adicionar Novo Agendamento</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="pet">Pet:</label>
        <select id="pet" v-model="agendamento.petId" required>
          <option v-for="pet in pets" :key="pet.id" :value="pet.id">
            {{ pet.nome }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="data">Data e Hora:</label>
        <input
          type="datetime-local"
          id="data"
          v-model="agendamento.data"
          required
        />
      </div>
      <div class="form-group">
        <label for="funcionario">Funcionário:</label>
        <select id="funcionario" v-model="agendamento.funcionarioId" required>
          <option
            v-for="funcionario in funcionarios"
            :key="funcionario.id"
            :value="funcionario.id"
          >
            {{ funcionario.nome }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="servicos">Serviços:</label>
        <div class="button-group">
          <button
            v-for="servico in servicos"
            :key="servico.id"
            :class="{ selected: agendamento.servicos.includes(servico.id) }"
            @click="toggleService(servico.id)"
            type="button"
          >
            {{ servico.descricao }} (R$ {{ servico.valor.toFixed(2) }})
          </button>
        </div>
      </div>
      <div class="form-group">
        <label for="valor">Valor Total:</label>
        <input type="text" id="valor" :value="totalValue" disabled />
      </div>
      <button type="submit" class="btn-salvar">Salvar</button>
    </form>
    <Notification ref="notification" />
  </div>
</template>

<script>
import axios from "axios";
import Notification from "@/components/NotificationBar.vue";

const API_URL = "http://localhost:8080/petshop";

export default {
  name: "AgendamentoAdd",
  components: {
    Notification,
  },
  data() {
    return {
      agendamento: {
        petId: "",
        data: "",
        funcionarioId: "",
        servicos: [],
        valor: 0,
        status: "ABERTO",
      },
      clienteId: null,
      clienteNome: "",
      pets: [],
      funcionarios: [],
      servicos: [],
    };
  },
  computed: {
    totalValue() {
      return this.agendamento.servicos
        .map((id) => this.servicos.find((servico) => servico.id === id).valor)
        .reduce((sum, valor) => sum + valor, 0)
        .toFixed(2);
    },
  },
  created() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };

        // Requisita os dados do cliente atual
        const clienteResponse = await axios.get(`${API_URL}/cliente`, config);
        this.clienteId = clienteResponse.data.id;
        this.clienteNome = clienteResponse.data.nome;

        // Requisição dos dados dos pets, funcionários e serviços
        const [petsResponse, funcionariosResponse, servicosResponse] =
          await Promise.all([
            axios.get(`${API_URL}/pet/cliente`, config),
            axios.get(`${API_URL}/funcionario/todos`, config),
            axios.get(`${API_URL}/servico`, config),
          ]);
        this.pets = petsResponse.data;
        this.funcionarios = funcionariosResponse.data;
        this.servicos = servicosResponse.data;

        console.log("Dados do cliente obtidos:", clienteResponse.data);
      } catch (error) {
        console.error("Erro ao buscar dados:", error);
      }
    },
    toggleService(serviceId) {
      const index = this.agendamento.servicos.indexOf(serviceId);
      if (index === -1) {
        this.agendamento.servicos.push(serviceId);
      } else {
        this.agendamento.servicos.splice(index, 1);
      }
      this.agendamento.valor = this.totalValue;
    },
    async submitForm() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };

        // Prepare o objeto de dados para o agendamento
        const agendamentoData = {
          cliente: {
            id: this.clienteId,
            nome: this.clienteNome,
          },
          pet: {
            id: this.agendamento.petId,
          },
          funcionario: {
            id: this.agendamento.funcionarioId,
          },
          data: this.agendamento.data,
          valor: parseFloat(this.agendamento.valor), // Convertendo valor para número
          status: this.agendamento.status,
          servicos: this.agendamento.servicos.map((servicoId) => {
            const servico = this.servicos.find((s) => s.id === servicoId);
            return {
              id: servico.id,
              descricao: servico.descricao,
              valor: servico.valor,
            };
          }),
        };

        console.log("Enviando dados do agendamento:", agendamentoData);

        const response = await axios.post(
          `${API_URL}/agendamento/create`,
          agendamentoData,
          config
        );

        console.log("Resposta do servidor:", response);
        console.log("Agendamento criado com sucesso:", response.data);
        this.$refs.notification.show(
          "Agendamento adicionado com sucesso!",
          "success"
        );

        // Limpar o formulário após sucesso
        this.agendamento = {
          petId: "",
          data: "",
          funcionarioId: "",
          servicos: [],
          valor: 0,
          status: "ABERTO",
        };

        this.$router.push("/agendamentos");
      } catch (error) {
        console.error("Erro ao adicionar agendamento:", error);
        this.$refs.notification.show(
          "Erro ao adicionar agendamento. Verifique os dados e tente novamente.",
          "error"
        );
      }
    },
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");

.add-agendamento {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  font-family: "Roboto", sans-serif;
}

h1 {
  font-size: 2.5em;
  color: #4a4a4a;
  margin-bottom: 20px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
  max-width: 400px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  font-size: 1.2em;
  margin-bottom: 5px;
}

input,
select {
  padding: 10px;
  font-size: 1em;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.button-group button {
  padding: 10px 15px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

.button-group button.selected {
  background-color: #6141ac;
  color: white;
  border: 1px solid #6141ac;
}

.button-group button:hover {
  background-color: #e0e0e0;
}

button {
  background-color: #68cd86;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em;
}

button:hover {
  background-color: #57b574;
}

.btn-salvar {
  background-color: #1aac46;
}
</style>
