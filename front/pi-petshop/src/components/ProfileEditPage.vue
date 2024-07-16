<template>
  <div class="profile">
    <h1>Editar Perfil</h1>
    <form @submit.prevent="updateProfile" class="form">
      <div class="form-group">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" v-model="nome" required />
      </div>
      <div class="form-group">
        <label for="email">E-mail:</label>
        <input type="text" id="email" v-model="email" required disabled />
      </div>
      <div class="form-group">
        <label for="telefone">Telefone:</label>
        <input type="text" id="telefone" v-model="telefone" required />
      </div>
      <div class="form-group">
        <label for="cep">CEP:</label>
        <input type="text" id="cep" v-model="cep" required />
      </div>
      <div class="form-group">
        <label for="cpf">CPF:</label>
        <input type="text" id="cpf" v-model="formattedCpf" readonly />
      </div>
      <button type="submit" class="btn-salvar">Salvar</button>
    </form>
    <NotificationBar ref="notification" />
  </div>
</template>

<script>
import NotificationBar from "@/components/NotificationBar.vue";
import AuthService from "@/services/AuthService";

export default {
  components: {
    NotificationBar,
  },
  data() {
    return {
      nome: "",
      email: "",
      telefone: "",
      cep: "",
      cpf: "",
      formattedCpf: "",
    };
  },
  async mounted() {
    await this.loadProfileData();
  },
  methods: {
    async loadProfileData() {
      try {
        const response = await AuthService.getProfile();
        this.nome = response.nome;
        this.email = response.email;
        this.telefone = response.telefone || "";
        this.cep = response.cep || "";
        this.cpf = response.cpf || "";

        this.formattedCpf = this.maskCpf(this.cpf);
      } catch (error) {
        console.error("Erro ao carregar perfil:", error);
        this.$refs.notification.show("Erro ao carregar perfil!", "error");
      }
    },
    maskCpf(cpf) {
      if (!cpf) return "";
      return cpf.replace(/^(\d{3})\d+(\d{3})\d+(\d{2})$/, "$1.***.***-$2");
    },
    async updateProfile() {
      try {
        const data = {
          id: this.id,
          nome: this.nome,
          email: this.email,
          telefone: this.telefone,
          cep: this.cep,
          cpf: this.cpf,
        };

        await AuthService.updateProfile(data);
        this.$refs.notification.show(
          "Perfil atualizado! Redirecionando...",
          "success"
        );
        this.$nextTick(() => {
          setTimeout(() => {
            this.$router.push("/profile");
          }, 3000);
        });
      } catch (error) {
        console.error("Erro ao atualizar perfil:", error);
        this.$refs.notification.show("Erro ao atualizar perfil!", "error");
      }
    },
  },
};
</script>

<style scoped>
.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  text-align: center;
  padding: 20px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  max-width: 400px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

input {
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.btn-salvar {
  margin-top: 20px;
  background-color: #68cd86;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-salvar:hover {
  background-color: #57b574;
}
</style>
