<template>
  <div class="add-pet">
    <h1>Adicionar Novo Pet</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" v-model="pet.nome" required />
      </div>
      <div class="form-group">
        <label for="tipo">Tipo:</label>
        <select id="tipo" v-model="pet.tipo" required>
          <option value="Cachorro">Cachorro</option>
          <option value="Gato">Gato</option>
          <option value="Pássaro">Pássaro</option>
          <option value="Outro">Outro</option>
        </select>
      </div>
      <div class="form-group">
        <label for="idade">Idade:</label>
        <select id="idade" v-model="pet.idade" required>
          <option v-for="n in 20" :key="n" :value="n">{{ n }}</option>
        </select>
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
        <input type="number" step="0.1" id="peso" v-model="pet.peso" required />
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
      <div
        v-if="pet.alergias !== 'Nenhuma' && pet.alergias !== ''"
        class="form-group"
      >
        <label for="emTratamento">Em Tratamento:</label>
        <input type="checkbox" id="emTratamento" v-model="pet.emTratamento" />
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
  name: "PetAdd",
  components: {
    Notification,
  },
  data() {
    return {
      pet: {
        nome: "",
        tipo: "",
        idade: null,
        raca: "",
        genero: "",
        peso: null,
        alergias: "",
        alergiasOutro: "",
        emTratamento: false,
        cliente: "",
      },
    };
  },
  methods: {
    async submitForm() {
      try {
        const token = localStorage.getItem("token");
        const email = this.getEmailFromToken(token);

        const petDTO = {
          nome: this.pet.nome,
          tipo: this.pet.tipo,
          idade: this.pet.idade,
          raca: this.pet.raca,
          genero: this.pet.genero,
          peso: this.pet.peso,
          alergias: this.pet.alergias,
          emTratamento: this.pet.emTratamento,
          clienteEmail: email,
        };

        const response = await axios.post(`${API_URL}/pet/create`, petDTO);

        console.log("Pet criado com sucesso:", response.data);
        this.$refs.notification.show("Pet adicionado com sucesso!", "success");

        this.pet = {
          nome: "",
          tipo: "",
          idade: null,
          raca: "",
          genero: "",
          peso: null,
          alergias: "",
          alergiasOutro: "",
          emTratamento: false,
          clienteEmail: `${email}`,
        };

        this.$router.push("/pets");
      } catch (error) {
        console.error("Erro ao adicionar pet:", error);
        this.$refs.notification.show(
          "Erro ao adicionar pet. Verifique os dados e tente novamente.",
          "error"
        );
      }
    },
    getEmailFromToken(token) {
      if (!token) {
        console.error("Token não encontrado no localStorage.");
        return null;
      }

      try {
        const payloadBase64Url = token.split(".")[1];
        const payloadBase64 = payloadBase64Url
          .replace(/-/g, "+")
          .replace(/_/g, "/");
        const payload = JSON.parse(atob(payloadBase64));

        const email = payload.sub;
        return email;
      } catch (error) {
        console.error("Erro ao decodificar o token:", error);
        return null;
      }
    },
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");

.add-pet {
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
