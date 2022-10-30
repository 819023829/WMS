<template>
  <div id="app">
    <router-view />
  </div>
</template>
<script>
export default {
  name: "App",
  watch: {
    $route(to, from) {
      if (to.path != "/") {
        let obj = {
          route: to.path,
          title: to.meta.title,
        };
        if (obj.title) {
          this.$store.commit("addTab", obj);
        }
      }
    },
  },
};
</script>

<style>
</style>
