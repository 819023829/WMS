<template>
  <el-tabs
    v-model="editableTabsValue"
    type="card"
    closable
    @tab-remove="removeTab"
    @tab-click="clickTab"
  >
    <el-tab-pane
      v-for="item in editableTabs"
      :key="item.name"
      :label="item.title"
      :name="item.name"
    >
    </el-tab-pane>
  </el-tabs>
</template>
<script>
export default {
  name: "Tab",
  computed: {
    editableTabsValue: {
      get() {
        return this.$store.state.menus.editableTabsValue;
      },
      set(val) {
        this.$store.state.menus.editableTabsValue = val;
      },
    },

    editableTabs: {
      get() {
        return this.$store.state.menus.editableTabs;
      },
      set(val) {
        this.$store.state.menus.editableTabs = val;
      },
    },
  },
  data() {
    return {
      tabIndex: 2,
    };
  },
  methods: {
    //点击tab时切换
    clickTab(target) {
      if (this.$route.path === target.name) return;
      this.$router.push(target.name);
    },
    //删除标签
    removeTab(targetName) {
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;

      if (targetName === "/home") {
        return;
      }
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }

      this.editableTabsValue = activeName;
      this.editableTabs = tabs.filter((tab) => tab.name !== targetName);
      //防止路由重复跳转
      if (this.$route.path != activeName) this.$router.push(activeName);
    },
  },
};
</script>
<style scoped>
</style>
