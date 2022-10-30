import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import axios from "./axios.js";
import "./assets/css/global.css";
import { message } from "@/assets/js/resetMessage";
import glb from "./globalFun";

Vue.use(ElementUI);

Vue.prototype.$message = message;
Vue.prototype.$axios = axios;
Vue.config.productionTip = false;

// require("./mock.js");

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
