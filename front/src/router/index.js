import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Index from "../views/Index.vue";
import UserCenter from "../views/UserCenter.vue";
import store from "../store";
import axios from "../axios";

Vue.use(VueRouter);

const routes = [
  {
    path: "/index",
    name: "Index",
    component: Index,
    children: [
      {
        path: "/home",
        name: "Home",
        component: Home,
      },

      {
        path: "/user/center",
        name: "UserCenter",
        meta: { title: "个人中心" },
        component: UserCenter,
      },
    ],
  },
  {
    path: "/",
    name: "Login",
    component: Login,
  },
];

const router = new VueRouter({
  routes,
});

//动态绑定路由
router.beforeEach((to, from, next) => {
  let hasRoute = store.state.menus.hasRoute;
  let token = localStorage.getItem("token");
  if (to.path == "/") {
    next();
  } else if (!token) {
    next({ path: "/" });
  } else if (token && !hasRoute) {
    axios
      .get("/sys/menu/nav", {
        headers: {
          Authorization: localStorage.getItem("token"),
        },
      })
      .then((res) => {
        //拿到menuList
        store.commit("setMenuList", res.data.data.nav);
        //拿到用户权限
        store.commit("setPermList", res.data.data.authoriys);
        //动态绑定路由
        let newRoutes = router.options.routes;
        res.data.data.nav.forEach((menu) => {
          if (menu.children) {
            menu.children.forEach((e) => {
              //转化成路由
              let route = menuToRoute(e);

              //把路由添加到路由管理中
              if (route) {
                newRoutes[0].children.push(route);
              }
            });
          }
        });
        router.matcher = new VueRouter().matcher;
        for (let x of newRoutes) {
          router.addRoute(x);
        }
      });
    hasRoute = true;
    store.commit("changeRouteStatus", hasRoute);
  }
  next();
});

//导航转化成路由
const menuToRoute = (menu) => {
  if (!menu.component) {
    return null;
  }
  let route = {
    name: menu.name,
    path: menu.route,
    meta: {
      title: menu.title,
      icon: menu.iconClass,
    },
  };
  route.component = () => import(`@/views/${menu.component}.vue`);
  return route;
};

export default router;
