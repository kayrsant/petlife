<template>
  <div class="edit-agendamento">
    <h1>Editar Agendamento</h1>
    <form @submit.prevent="showConfirmationPopup">
      <div class="form-group">
        <label for="pet">Pet:</label>
        <input type="text" id="pet" v-model="agendamento.pet.nome" disabled />
      </div>
      <div class="form-group">
        <label for="data">Data e Hora:</label>
        <input type="datetime-local" id="data" v-model="inputDate" required />
      </div>
      <div class="form-group">
        <label for="servicos">Serviços:</label>
        <div class="button-group">
          <button
            v-for="servico in allServices"
            :key="servico.id"
            :class="{ selected: selectedServices.includes(servico.id) }"
            @click="toggleService(servico.id)"
            type="button"
          >
            {{ servico.descricao }} (R$ {{ servico.valor.toFixed(2) }})
          </button>
        </div>
      </div>
      <div class="form-group">
        <label for="valor">Valor:</label>
        <input type="text" id="valor" :value="totalValue" disabled />
      </div>
      <div class="form-group" v-if="isFuncionario || isAdmin">
        <label for="status">Status:</label>
        <select v-model="agendamento.status" id="status">
          <option value="EM_ANDAMENTO">EM ANDAMENTO</option>
          <option value="ABERTO">ABERTO</option>
          <option value="FINALIZADO">FINALIZADO</option>
        </select>
      </div>
      <button type="submit" class="submit-button">Atualizar Agendamento</button>
    </form>

    <!-- Pop-up de Confirmação -->
    <div v-if="showPopup" class="popup-overlay">
      <div class="popup-content">
        <h2>Confirmar Edição</h2>
        <p>Você tem certeza que deseja atualizar este agendamento?</p>
        <button @click="updateAgendamento" class="confirm-button">
          Confirmar
        </button>
        <button @click="cancelUpdate" class="cancel-button">Cancelar</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const API_URL = "http://localhost:8080/petshop";

export default {
  name: "AgendamentosEdit",
  data() {
    return {
      agendamento: {
        pet: {},
        servicos: [],
        data: "",
        valor: 0,
        status: "",
      },
      allServices: [], // All available services
      selectedServices: [], // Array to hold selected service IDs
      showPopup: false, // Variável para controlar o pop-up
      inputDate: "", // Propriedade para gerenciar o input de data e hora
      isFuncionario: false,
      isAdmin: false,
    };
  },
  computed: {
    formattedDate() {
      if (!this.agendamento.data) return "";
      const localDate = new Date(this.agendamento.data);
      localDate.setHours(localDate.getHours() - 3); // Ajuste para GMT-3
      return localDate.toISOString().slice(0, -1).replace("T", " "); // Formatar para "YYYY-MM-DDTHH:MM"
    },
    totalValue() {
      return this.selectedServices
        .map(
          (id) =>
            this.allServices.find((servico) => servico.id === id)?.valor || 0
        )
        .reduce((sum, valor) => sum + valor, 0)
        .toFixed(2);
    },
  },
  watch: {
    inputDate(newDate) {
      this.agendamento.data = new Date(newDate).toISOString(); // Atualiza agendamento.data quando inputDate mudar
    },
  },
  async created() {
    await this.fetchAgendamento();
    await this.fetchAllServices();
    await this.checkUserType();
  },
  methods: {
    async fetchAgendamento() {
      try {
        const agendamentoId = this.$route.params.id;
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(
          `${API_URL}/agendamento/${agendamentoId}`,
          config
        );
        this.agendamento = response.data;
        this.selectedServices = this.agendamento.servicos.map(
          (servico) => servico.id
        );
        this.inputDate = this.formattedDate; // Inicializa inputDate com formattedDate
      } catch (error) {
        console.error("Erro ao buscar agendamento:", error);
      }
    },
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
    async fetchAllServices() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(`${API_URL}/servico`, config);
        this.allServices = response.data;
      } catch (error) {
        console.error("Erro ao buscar serviços:", error);
      }
    },
    toggleService(serviceId) {
      const index = this.selectedServices.indexOf(serviceId);
      if (index === -1) {
        this.selectedServices.push(serviceId);
      } else {
        this.selectedServices.splice(index, 1);
      }
      this.updateTotalValue();
    },
    updateTotalValue() {
      this.totalValue = this.selectedServices
        .map(
          (id) =>
            this.allServices.find((servico) => servico.id === id)?.valor || 0
        )
        .reduce((sum, valor) => sum + valor, 0)
        .toFixed(2);
    },
    showConfirmationPopup() {
      console.log("inputDate:", this.inputDate); // Adicione isto
      console.log("agendamento.data:", this.agendamento.data); // Adicione isto
      this.showPopup = true;
    },
    async updateAgendamento() {
      console.log("inputDate:", this.inputDate); // Adicione isto
      console.log("agendamento.data:", this.agendamento.data); // Adicione isto
      try {
        const agendamentoId = this.$route.params.id;
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const updatedAgendamento = {
          ...this.agendamento,
          data: this.agendamento.data, // Usar agendamento.data diretamente
          servicos: this.selectedServices.map((id) =>
            this.allServices.find((servico) => servico.id === id)
          ),
          valor: parseFloat(this.totalValue), // Converte totalValue para número
        };
        await axios.put(
          `${API_URL}/agendamento/${agendamentoId}`,
          updatedAgendamento,
          config
        );
        this.showPopup = false;
        this.$router.push("/dashboard");
      } catch (error) {
        console.error("Erro ao atualizar agendamento:", error);
      }
    },
    cancelUpdate() {
      this.showPopup = false;
    },
  },
};
</script>

<style scoped>
.edit-agendamento {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input,
select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.button-group button {
  padding: 8px 12px; /* Tamanho reduzido dos botões */
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  text-align: left;
  width: auto; /* Ajustado para caber o conteúdo */
  flex: 1 0 30%; /* Design responsivo: permite que os botões envolvam */
}

.button-group button.selected {
  background-color: #6141ac;
  color: white;
  border: 1px solid #6141ac;
}

.button-group button:hover {
  background-color: #e0e0e0;
}

.submit-button {
  display: block;
  margin: 20px auto;
  padding: 10px 20px;
  background-color: #6141ac;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.submit-button:hover {
  background-color: #4a2e8e;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.popup-content {
  background: #fff;
  padding: 20px;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  text-align: center;
}

.popup-content h2 {
  margin-bottom: 10px;
}

.popup-content p {
  margin-bottom: 20px;
}

.confirm-button,
.cancel-button {
  background-color: #6141ac;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-right: 10px;
}

.confirm-button:hover {
  background-color: #4a2e8e;
}

.cancel-button {
  background-color: #f44336;
}

.cancel-button:hover {
  background-color: #d32f2f;
}
</style>
