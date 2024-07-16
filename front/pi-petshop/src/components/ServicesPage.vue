<template>
  <div class="services">
    <h1>Nossos Serviços</h1>
    <div class="cards-container">
      <div class="card" v-for="(service, index) in services" :key="index">
        <h2>{{ service.name }}</h2>
        <p>{{ service.description }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ServicesPage",
  data() {
    return {
      services: [
        {
          name: "Consulta Veterinária",
          description:
            "Oferecemos consultas veterinárias completas para cuidar da saúde do seu pet.",
        },
        {
          name: "Banho e Tosa",
          description:
            "Serviços de banho e tosa para deixar seu pet limpo e bem cuidado.",
        },
        {
          name: "Hospedagem",
          description:
            "Hospedagem segura e confortável para o seu pet enquanto você viaja.",
        },
      ],
    };
  },
};
</script>

<style scoped>
.services {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 20px;
}

h1 {
  color: #6141ac;
  margin-bottom: 20px;
}

.cards-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.card {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  max-width: 300px;
  text-align: left;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.card h2 {
  color: #333;
  margin-bottom: 10px;
}

.card p {
  color: #666;
  margin: 0;
}
</style>
