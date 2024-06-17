<!-- src/components/Notification.vue -->
<template>
  <transition name="slide-fade">
    <div v-if="visible" class="notification" :class="type">
      {{ message }}
    </div>
  </transition>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      message: "",
      type: "",
    };
  },
  methods: {
    show(message, type) {
      this.message = message;
      this.type = type;
      this.visible = true;
      setTimeout(() => {
        this.visible = false;
      }, 5000);
    },
  },
};
</script>

<style scoped>
.notification {
  position: fixed;
  bottom: 20px;
  right: 20px;
  padding: 15px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
}

.notification.info {
  background-color: #45b3fa;
  color: white;
}

.notification.warn {
  background: #ffc107;
  border-left-color: #ffa07a;
}

.notification.error {
  background: #e74c3c;
  border-left-color: #c0392b;
}

.notification.success {
  background: #2ecc71;
  border-left-color: #1abc9c;
}

.slide-fade-enter-active {
  transition: transform 0.5s ease-in-out;
}

.slide-fade-leave-active {
  transition: transform 0.5s ease-in-out;
}

.slide-fade-enter {
  transform: translateX(100%);
  opacity: 0;
}

.slide-fade-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
</style>
