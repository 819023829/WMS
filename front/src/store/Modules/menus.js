import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default {
  state: {
    menuList: [],
    permList: [],
    hasRoute: false,
    editableTabsValue: "/home",
    editableTabs: [
      {
        name: "/home",
        title: "首页",
      },
    ],
  },

  mutations: {
    addTab(state, tab) {
      let index = state.editableTabs.findIndex((e) => e.name === tab.route);

      if (index === -1) {
        state.editableTabs.push({
          title: tab.title,
          name: tab.route,
        });
      }
      state.editableTabsValue = tab.route;
    },
    setMenuList: (state, menus) => {
      state.menuList = menus;
    },
    setPermList: (state, perms) => {
      state.permList = perms;
    },
    changeRouteStatus: (state, hasRoute) => {
      state.hasRoute = hasRoute;
    },
    resetState: (state) => {
      state.menuList = [];
      state.permList = [];
      state.hasRoute = false;
      state.editableTabsValue = "/home";
      state.editableTabs = [
        {
          name: "/home",
          title: "首页",
        },
      ];
    },
  },
  actions: {},
};
