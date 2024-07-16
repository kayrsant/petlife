<template>
  <div class="cliente-edit">
    <NotificationBar ref="notificationBar" />
    <h1>Editar Cliente</h1>
    <form @submit.prevent="updateUserRole">
      <label for="nome">Nome:</label>
      <input v-model="cliente.nome" id="nome" type="text" required />

      <label for="email">Email:</label>
      <input v-model="cliente.email" id="email" type="email" required />

      <label for="telefone">Telefone:</label>
      <input v-model="cliente.telefone" id="telefone" type="text" required />

      <label for="cep">CEP:</label>
      <input v-model="cliente.cep" id="cep" type="text" required />

      <label for="cpf">CPF:</label>
      <input v-model="cliente.cpf" id="cpf" type="text" required />

      <button type="submit">Atualizar</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import NotificationBar from "@/components/NotificationBar.vue";

const API_URL = "http://localhost:8080/petshop";

export default {
  name: "ClienteEditPage",
  components: {
    NotificationBar,
  },
  data() {
    return {
      cliente: {},
      roles: ["CLIENTE", "FUNCIONARIO", "ADMIN"],
      selectedRole: "CLIENTE",
    };
  },
  async created() {
    const clienteId = this.$route.params.id;
    await this.fetchCliente(clienteId);
  },
  methods: {
    async fetchCliente(clienteId) {
      try {
        const response = await axios.get(`${API_URL}/cliente/${clienteId}`);
        this.cliente = response.data;
      } catch (error) {
        console.error("Erro ao buscar cliente:", error);
        this.$refs.notificationBar.show("Erro ao buscar cliente", "error");
      }
    },
    async updateUserRole() {
      try {
        console.log(`id: ${this.cliente.id},
          nome: ${this.cliente.nome},
          email: ${this.cliente.email},
          telefone: ${this.cliente.telefone},
          cep: ${this.cliente.cep},
          cpf: ${this.cliente.cpf},
          role: null,`);
        await axios.put(`${API_URL}/cliente/${this.cliente.id}`, {
          id: this.cliente.id,
          nome: this.cliente.nome,
          email: this.cliente.email,
          telefone: this.cliente.telefone,
          cep: this.cliente.cep,
          cpf: this.cliente.cpf,
          role: null,
        });

        this.$refs.notificationBar.show(
          "Cliente atualizado com sucesso",
          "success"
        );
        setTimeout(() => {
          this.$router.push("/dashboard");
        }, 3000);
      } catch (error) {
        this.$refs.notificationBar.show(
          "Erro para atualizar cliente.",
          "error"
        );
      }
    },
  },
};
</script>

<style scoped>
.cliente-edit {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  background: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h1 {
  margin-bottom: 1.5rem;
  color: #333;
}

form {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 0.5rem;
  font-weight: bold;
  color: #555;
}

input,
select {
  padding: 0.8rem;
  margin-bottom: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

button {
  padding: 0.8rem;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: #fff;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0056b3;
}

button:focus {
  outline: none;
}
</style>
