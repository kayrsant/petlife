<template>
  <div class="pets">
    <header>
      <h1>Pets Cadastrados</h1>
    </header>
    <div class="pet-cards">
      <div v-for="pet in sortedPets" :key="pet.id" class="pet-card">
        <h2>{{ pet.nome }}</h2>
        <p>Tipo: {{ pet.tipo }}</p>
        <p>Idade: {{ pet.idade }} anos</p>
        <p>Raça: {{ pet.raca }}</p>
        <p>Peso: {{ pet.peso }} kg</p>
        <p>Gênero: {{ pet.genero }}</p>
        <p v-if="pet.alergias !== 'Nenhuma'">Alergias: {{ pet.alergias }}</p>
        <p v-if="pet.emTratamento">Em Tratamento: {{ pet.emTratamento }}</p>
        <p>Dono: {{ pet.cliente.nome }}</p>
        <div class="actions">
          <button class="edit-pet-button" @click="editPet(pet.id)">
            Editar
          </button>
          <button class="remove-pet-button" @click="removePet(pet.id)">
            Remover
          </button>
        </div>
      </div>
    </div>
    <!-- Adicione a referência ao NotificationBar aqui -->
    <NotificationBar ref="notificationBar" />
  </div>
</template>

<script>
import axios from "axios";
import NotificationBar from "@/components/NotificationBar.vue";

export default {
  name: "PetsCadastradosPage",
  components: {
    NotificationBar,
  },
  data() {
    return {
      pets: [],
    };
  },
  computed: {
    sortedPets() {
      return this.pets.slice().sort((a, b) => a.id - b.id);
    },
  },
  created() {
    this.fetchPets();
  },
  methods: {
    async fetchPets() {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(
          "http://localhost:8080/petshop/pet",
          config
        );
        this.pets = response.data;
      } catch (error) {
        console.error("Erro ao buscar pets:", error);
      }
    },
    addPet() {
      this.$router.push({
        path: "pets/create",
      });
    },
    editPet(petId) {
      this.$router.push(`/func/pets/editar/${petId}`);
    },
    async removePet(petId) {
      try {
        const token = localStorage.getItem("token");
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        await axios.delete(
          `http://localhost:8080/petshop/pet/${petId}`,
          config
        );
        this.pets = this.pets.filter((pet) => pet.id !== petId);
        this.$refs.notificationBar.showSuccess("Pet removido com sucesso.");
      } catch (error) {
        this.$refs.notificationBar.showError(
          "O Pet está vinculado a um agendamento.",
          "error"
        );
      }
    },
  },
};
</script>

<style scoped>
/* Estilos iguais ao do exemplo anterior */
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");
.pets {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  font-family: "Roboto", sans-serif;
}

header {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.new-pet-button-container {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-bottom: 20px;
}

.pet-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  margin-top: 20px;
}

.pet-card {
  background-color: #fff;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 220px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.pet-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}

.pet-card h2 {
  font-size: 1.5em;
  color: #333;
  margin-bottom: 10px;
}

.pet-card p {
  font-size: 1em;
  color: #666;
  margin-bottom: 5px;
}

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.edit-pet-button,
.remove-pet-button {
  background-color: #ffc107;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em;
}

.edit-pet-button:hover {
  background-color: #57b574;
}

.remove-pet-button {
  background-color: rgb(170, 1, 1);
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em;
}

.remove-pet-button:hover {
  background-color: #ff3737;
}

.button {
  --main-focus: #2d8cf0;
  --font-color: #323232;
  --bg-color-sub: #dedede;
  --bg-color: #eee;
  --main-color: #323232;
  position: relative;
  width: 150px;
  height: 40px;
  cursor: pointer;
  display: flex;
  align-items: center;
  border: 2px solid var(--main-color);
  box-shadow: 4px 4px var(--main-color);
  background-color: var(--bg-color);
  border-radius: 10px;
  overflow: hidden;
}

.button,
.button__icon,
.button__text {
  transition: all 0.3s;
}

.button .button__text {
  transform: translateX(22px);
  color: var(--font-color);
  font-weight: 600;
}

.button .button__icon {
  position: absolute;
  transform: translateX(109px);
  height: 100%;
  width: 39px;
  background-color: var(--bg-color-sub);
  display: flex;
  align-items: center;
  justify-content: center;
}

.button .svg {
  width: 20px;
  fill: var(--main-color);
}

.button:hover {
  background: var(--bg-color);
}

.button:hover .button__text {
  color: transparent;
}

.button:hover .button__icon {
  width: 148px;
  transform: translateX(0);
}

.button:active {
  transform: translate(3px, 3px);
  box-shadow: 0px 0px var(--main-color);
}

.card {
  box-sizing: border-box;
  width: 190px;
  height: 254px;
  background: rgba(217, 217, 217, 0.58);
  border: 1px solid white;
  box-shadow: 12px 17px 51px rgba(0, 0, 0, 0.22);
  backdrop-filter: blur(6px);
  border-radius: 17px;
  text-align: center;
  cursor: pointer;
  transition: all 0.5s;
  display: flex;
  align-items: center;
  justify-content: center;
  user-select: none;
  font-weight: bolder;
  color: black;
}

.card:hover {
  border: 1px solid black;
  transform: scale(1.05);
}

.card:active {
  transform: scale(0.95) rotateZ(1.7deg);
}
</style>
