<template>
  <div class="login">
    <Notification ref="notification" />
    <div class="form-container">
      <h1 class="title">PetLife</h1>
      <div class="form-section">
        <h2>Login</h2>
        <form @submit.prevent="login">
          <input
            type="email"
            v-model="loginEmail"
            placeholder="Email"
            required
          />
          <input
            type="password"
            v-model="loginPassword"
            placeholder="Senha"
            required
          />
          <div class="button-group">
            <button type="submit">
              <i class="fas fa-sign-in-alt"></i> Login
            </button>
            <button type="button" @click="cancel">
              <i class="fas fa-times"></i> Cancelar
            </button>
          </div>
        </form>
      </div>
      <div class="form-section">
        <h2>Registro</h2>
        <form @submit.prevent="register">
          <div class="input-group">
            <input type="text" v-model="nome" placeholder="Nome" required />
            <input type="email" v-model="email" placeholder="Email" required />
          </div>
          <div class="input-group">
            <input
              type="password"
              v-model="senha"
              placeholder="Senha"
              required
            />
            <input
              type="text"
              v-model="telefone"
              placeholder="Telefone"
              required
            />
          </div>
          <div class="input-group">
            <input type="text" v-model="CEP" placeholder="CEP" required />
            <input type="text" v-model="CPF" placeholder="CPF" required />
          </div>
          <div class="button-group">
            <button type="submit">
              <i class="fas fa-user-plus"></i> Registrar
            </button>
            <button type="button" @click="resetForm">
              <i class="fas fa-times"></i> Limpar
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import AuthService from "@/services/AuthService.js";
import Notification from "@/components/NotificationBar.vue";

export default {
  components: {
    Notification,
  },
  data() {
    return {
      loginEmail: "",
      loginPassword: "",
      nome: "",
      email: "",
      senha: "",
      telefone: "",
      CEP: "",
      CPF: "",
    };
  },
  methods: {
    async login() {
      try {
        const response = await AuthService.login({
          email: this.loginEmail,
          senha: this.loginPassword,
        });

        const { token } = response;
        localStorage.setItem("token", token);

        this.$refs.notification.show("Login realizado com sucesso!", "info");
        this.$root.isLoggedIn = true;
        this.$router.push("/dashboard");
      } catch (error) {
        this.$refs.notification.show("Erro ao fazer login!", "error");
        console.error(
          "Erro ao fazer login:",
          error.response ? error.response.data : error.message
        );
      }
    },
    async register() {
      try {
        await AuthService.register({
          nome: this.nome,
          email: this.email,
          senha: this.senha,
          telefone: this.telefone,
          cep: this.CEP,
          cpf: this.CPF,
        });

        this.$refs.notification.show(
          "Registro bem-sucedido! Faça o login para continuar.",
          "info"
        );
        this.resetForm();
        this.$router.push("/login");
      } catch (error) {
        this.$refs.notification.show("Erro ao registrar!", "error");
        console.error(
          "Erro ao registrar:",
          error.response ? error.response.data : error.message
        );
      }
    },
    resetForm() {
      this.nome = "";
      this.email = "";
      this.senha = "";
      this.telefone = "";
      this.CEP = "";
      this.CPF = "";
    },
    cancel() {
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css");

body {
  font-family: "Roboto", sans-serif;
  background-color: #f0f0f0;
  margin: 0;
  padding: 0;
}

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.form-container {
  background-color: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.title {
  color: #6141ac;
  text-align: center;
  font-size: 2em;
  margin-bottom: 20px;
}

.form-section {
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

h2 {
  color: #6141ac;
  margin-bottom: 20px;
  text-align: center;
  font-size: 1.5em;
}

input {
  width: calc(100% - 30px);
  padding: 15px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1em;
  transition: border-color 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

input:focus {
  border-color: #6141ac;
  outline: none;
  box-shadow: 0 0 8px rgba(97, 65, 172, 0.5);
}

.button-group {
  display: flex;
  justify-content: space-between;
}

button {
  width: 48%;
  padding: 15px;
  background-color: #6141ac;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1em;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s ease-in-out, transform 0.3s ease-in-out;
}

button i {
  margin-right: 10px;
}

button:hover {
  background-color: #50379c;
  transform: translateY(-3px);
}

button[type="button"] {
  background-color: #d32f2f;
}

button[type="button"]:hover {
  background-color: #b71c1c;
}

.input-group {
  display: flex;
  justify-content: space-between;
}

.input-group input {
  width: calc(50% - 15px);
}
</style>
