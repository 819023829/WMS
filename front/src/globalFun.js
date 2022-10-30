import Vue from "vue";
Vue.mixin({
  methods: {
    hasAuth(perm) {
      //权限控制的方法
      var authority = this.$store.state.menus.permList;
      return authority.indexOf(perm) > -1;
    },
  },
});
