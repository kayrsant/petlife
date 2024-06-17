<template>
  <div id="app">
    <component :is="navbarComponent" @logout="handleLogout" />
    <router-view />
  </div>
</template>

<script>
import NavBarLoggedIn from "./components/NavBarLoggedIn.vue";
import NavBarLoggedOut from "./components/NavBarLoggedOut.vue";

export default {
  name: "App",
  components: {
    NavBarLoggedIn,
    NavBarLoggedOut,
  },
  data() {
    return {
      isLoggedIn: false,
      navbarComponent: "NavBarLoggedOut",
    };
  },
  created() {
    this.isLoggedIn = !!localStorage.getItem("token");
    this.updateNavbarComponent();
  },
  watch: {
    isLoggedIn() {
      this.updateNavbarComponent();
    },
  },
  methods: {
    handleLogout() {
      localStorage.removeItem("token");
      this.isLoggedIn = false;
      this.$router.push("/login");
    },
    updateNavbarComponent() {
      this.navbarComponent = this.isLoggedIn
        ? "NavBarLoggedIn"
        : "NavBarLoggedOut";
    },
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");

body {
  font-family: "Roboto", sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f0f0f0;
}
</style>
