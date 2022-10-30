<template>
  <el-container>
    <el-aside width="200px">
      <el-menu
        :default-active="this.$store.state.menus.editableTabsValue"
        class="el-menu-vertical-demo"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        :router="true"
      >
        <el-menu-item index="/home">
          <template slot="title">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </template>
        </el-menu-item>
        <el-submenu
          v-for="item in menuList"
          :key="item.name"
          :index="item.name"
        >
          <template slot="title">
            <i :class="item.iconClass"></i>
            <span>{{ item.title }}</span>
          </template>
          <el-menu-item
            v-for="subItem in item.children"
            :key="subItem.id"
            :index="subItem.route"
          >
            <template slot="title">
              <i :class="subItem.iconClass"></i>
              <span slot="title">{{ subItem.title }}</span>
            </template>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>XXX后台管理系统</strong>
        <div class="header-avatar">
          <el-avatar size="medium" :src="userInfo.avatar"></el-avatar>

          <el-dropdown>
            <span class="el-dropdown-link">
              {{ userInfo.username }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <router-link to="/user/center">
                  个人中心</router-link
                ></el-dropdown-item
              >
              <el-dropdown-item @click.native="quit">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <Tab></Tab>
        <div style="margin: 0 15px">
          <router-view />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import Tab from "./Tab.vue";
export default {
  name: "SideMenu",
  components: {
    Tab,
  },

  created() {
    this.getUserInfo();
  },
  methods: {
    quit() {
      this.$axios.post("/logout").then((res) => {
        localStorage.clear();
        sessionStorage.clear();
        this.$store.commit("resetState");
        this.$router.push("/");

        location.reload();
      });
    },
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.data;
      });
    },
  },
  data() {
    return {
      userInfo: {
        id: "",
        username: "",
        avatar: "", //头像
      },
    };
  },
  computed: {
    //动态获取导航栏
    menuList: {
      get() {
        return this.$store.state.menus.menuList;
      },
    },
  },
};
</script>
<style scoped>
.el-container {
  height: 100%;
}
.header-avatar {
  float: right;
  width: 130px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.el-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}
.el-dropdown-link {
  cursor: pointer;
}
.el-aside {
  background-color: #d3dce6;
  color: #333;
  line-height: 200px;
}

.el-main {
  color: #333;
  padding: 0;
}
.el-menu {
  border-right: 0px;
}
.el-menu-vertical-demo {
  height: 100%;
}
a {
  text-decoration: none;
}
</style>
