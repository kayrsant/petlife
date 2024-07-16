<template>
  <div class="edit-pet">
    <h1>Editar Pet</h1>
    <form @submit.prevent="showModal">
      <div class="form-group">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" v-model="pet.nome" required />
      </div>
      <div class="form-group">
        <label for="tipo">Tipo:</label>
        <input type="text" id="tipo" v-model="pet.tipo" required />
      </div>
      <div class="form-group">
        <label for="idade">Idade:</label>
        <input type="number" id="idade" v-model.number="pet.idade" required />
      </div>
      <div class="form-group">
        <label for="raca">Raça:</label>
        <input type="text" id="raca" v-model="pet.raca" required />
      </div>
      <div class="form-group">
        <label for="genero">Gênero:</label>
        <select id="genero" v-model="pet.genero" required>
          <option value="Macho">Macho</option>
          <option value="Fêmea">Fêmea</option>
        </select>
      </div>
      <div class="form-group">
        <label for="peso">Peso (kg):</label>
        <input type="number" step="0.1" id="peso" v-model.number="pet.peso" />
      </div>
      <div class="form-group">
        <label for="alergias">Alergias:</label>
        <select id="alergias" v-model="pet.alergias">
          <option value="Nenhuma">Nenhuma</option>
          <option value="Pulga">Pulga</option>
          <option value="Poeira">Poeira</option>
          <option value="Outro">Outro</option>
        </select>
        <input
          v-if="pet.alergias === 'Outro'"
          type="text"
          v-model="pet.alergiasOutro"
          placeholder="Especificar alergias"
        />
      </div>
      <div class="form-group">
        <label for="emTratamento">Em Tratamento:</label>
        <select id="emTratamento" v-model="pet.emTratamento">
          <option value="Sim">Sim</option>
          <option value="Não">Não</option>
        </select>
      </div>
      <button type="submit" class="save-button">Salvar</button>
    </form>

    <!-- Modal de Confirmação -->
    <div v-if="showConfirmationModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="hideModal">&times;</span>
        <h2>Confirmar Alterações</h2>
        <p>Deseja realmente alterar as informações do pet?</p>
        <p><strong>Nome:</strong> {{ pet.nome }}</p>
        <p><strong>Tipo:</strong> {{ pet.tipo }}</p>
        <p><strong>Idade:</strong> {{ pet.idade }}</p>
        <p><strong>Raça:</strong> {{ pet.raca }}</p>
        <p><strong>Gênero:</strong> {{ pet.genero }}</p>
        <p><strong>Peso:</strong> {{ pet.peso }}</p>
        <p><strong>Alergias:</strong> {{ pet.alergias }}</p>
        <p v-if="pet.alergias === 'Outro'">
          <strong>Alergias Específicas:</strong> {{ pet.alergiasOutro }}
        </p>
        <p><strong>Em Tratamento:</strong> {{ pet.emTratamento }}</p>
        <button @click="updatePet" class="confirm-button">Confirmar</button>
        <button @click="hideModal" class="cancel-button">Cancelar</button>
      </div>
    </div>
    <NotificationBar ref="notification" />
  </div>
</template>

<script>
import axios from "axios";
import NotificationBar from "@/components/NotificationBar.vue";

export default {
  components: {
    NotificationBar,
  },
  data() {
    return {
      pet: {
        id: this.$route.params.id,
        nome: "",
        tipo: "",
        idade: null,
        raca: "",
        genero: "",
        peso: null,
        alergias: "Nenhuma",
        alergiasOutro: "",
        emTratamento: "Não",
        cliente: { id: null }, // Adicione o cliente aqui
      },
      showConfirmationModal: false,
      cliente: { id: null }, // Adicione o cliente aqui
    };
  },
  async created() {
    await this.fetchPet();
  },
  methods: {
    async fetchPet() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(
          `http://localhost:8080/petshop/pet/${this.pet.id}`,
          config
        );
        this.pet = response.data;
        // Assumindo que você precisa do cliente também
        this.cliente.id = this.pet.cliente.id;
      } catch (error) {
        console.error("Erro ao buscar pet:", error);
        this.$refs.notification.show({
          type: "error",
          message: "Erro ao buscar detalhes do pet.",
        });
      }
    },
    showModal() {
      this.showConfirmationModal = true;
    },
    hideModal() {
      this.showConfirmationModal = false;
    },
    async updatePet() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };

        this.pet.cliente = { id: this.cliente.id };

        if (
          !this.pet.nome ||
          !this.pet.tipo ||
          !this.pet.idade ||
          !this.pet.raca ||
          !this.pet.genero ||
          !this.pet.emTratamento
        ) {
          throw new Error("Preencha todos os campos obrigatórios.");
        }

        await axios.put(
          `http://localhost:8080/petshop/pet/${this.pet.id}`,
          this.pet,
          config
        );

        this.$refs.notification.show("Pet atualizado com sucesso", "success");

        this.$nextTick(() => {
          setTimeout(() => {
            this.$router.push("/dashboard");
          }, 3000);
        });

        this.hideModal();
      } catch (error) {
        console.error("Erro ao atualizar pet:", error);
        this.$refs.notification.show({
          type: "error",
          message: "Erro ao atualizar o pet.",
        });
      }
    },
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");

.edit-pet {
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

input[type="checkbox"] {
  width: auto;
}

.save-button {
  background-color: #1aac46;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em;
}

.save-button:hover {
  background-color: #57b574;
}

.modal {
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.close {
  position: absolute;
  top: 10px;
  right: 20px;
  font-size: 1.5em;
  cursor: pointer;
}

.confirm-button,
.cancel-button {
  background-color: #1aac46;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em;
  margin: 10px 5px;
}

.confirm-button:hover,
.cancel-button:hover {
  background-color: #57b574;
}

.cancel-button {
  background-color: #f44336;
}

.cancel-button:hover {
  background-color: #d32f2f;
}

.btn-salvar {
  background-color: #68cd86;
}
</style>
