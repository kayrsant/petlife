<template>
  <div class="cliente-create">
    <h1>Criar Novo Cliente</h1>
    <form @submit.prevent="createCliente">
      <div class="form-group">
        <label for="nome">Nome</label>
        <input v-model="cliente.nome" type="text" id="nome" required />
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input v-model="cliente.email" type="email" id="email" required />
      </div>
      <div class="form-group">
        <label for="telefone">Telefone</label>
        <input v-model="cliente.telefone" type="text" id="telefone" required />
      </div>
      <div class="form-group">
        <label for="cep">CEP</label>
        <input v-model="cliente.cep" type="text" id="cep" required />
      </div>
      <div class="form-group">
        <label for="cpf">CPF</label>
        <input v-model="cliente.cpf" type="text" id="cpf" required />
      </div>
      <button type="submit" class="submit-button">Criar Cliente</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";

const API_URL = "http://localhost:8080/petshop";

export default {
  name: "ClienteCreatePage",
  data() {
    return {
      cliente: {
        nome: "",
        email: "",
        telefone: "",
        cep: "",
        cpf: "",
      },
    };
  },
  methods: {
    async createCliente() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        await axios.post(`${API_URL}/cliente/create`, this.cliente, config);
        this.$router.push("/clientes");
      } catch (error) {
        console.error("Erro ao criar cliente:", error);
      }
    },
  },
};
</script>

<style scoped>
.cliente-create {
  padding: 20px;
  max-width: 600px;
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
  font-weight: bold;
}

input {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.submit-button {
  display: block;
  margin: 20px auto;
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #388e3c;
}
</style>
