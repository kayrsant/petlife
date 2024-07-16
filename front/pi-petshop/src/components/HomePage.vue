<!-- src/components/HomePage.vue -->
<template>
  <div class="home">
    <h1>Bem-vindo à PetLife!</h1>
    <p>Aqui no PetLife, cuidamos do seu pet com amor e dedicação.</p>
    <p>
      Oferecemos uma variedade de serviços para atender às necessidades do seu
      amigo peludo.
    </p>
  </div>
</template>

<script>
export default {
  name: "HomePage",
};
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  text-align: center;
  padding: 20px;
}

h1 {
  color: #6141ac;
}

p {
  margin-bottom: 10px;
}

router-link {
  display: block;
  margin-top: 20px;
  color: #6141ac;
  text-decoration: none;
  font-weight: bold;
}

router-link:hover {
  text-decoration: underline;
}
</style>
