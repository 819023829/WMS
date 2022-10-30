import axios from "axios";
import router from "./router";
import Element from "element-ui";

axios.defaults.baseURL = "http://localhost:8081";

const request = axios.create({
  timeout: 5000,
  headers: {
    "Content-Type": "application/json;chartset=utf-8",
  },
});

request.interceptors.request.use((config) => {
  config.headers["Authorization"] = localStorage.getItem("token");
  return config;
});

request.interceptors.response.use(
  (response) => {
    let res = response.data;
    if (res.code === 200) return response;
    else {
      Element.MessageBox.alert(!res.msg ? "系统异常" : res.msg);
      return Promise.reject(response.data.msg);
    }
  },
  (error) => {
    if (error.response.data) {
      error.message = error.response.data.msg;
    }
    if (error.response.status === 401) {
      router.push("/");
    }
    Element.MessageBox.alert(error.message, { duration: 3000 });
    return Promise.reject(error);
  }
);

export default request;
