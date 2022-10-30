const Mock = require("mockjs");

const Random = Mock.Random;

let Result = {
  code: 200,
  msg: "操作成功",
  data: null,
};

Mock.mock("/code", "get", () => {
  Result.data = {
    token: Random.string(32),
    codeImg: Random.dataImage("100x40", "p7n5w"),
  };
  return Result;
});

Mock.mock("/login", "post", () => {
  return Result;
});

Mock.mock("/sys/userInfo", "get", () => {
  Result.data = {
    id: "1",
    username: "test",
    avatar: require("./assets/touxiang.jpg"),
  };
  return Result;
});

Mock.mock("/quit", "post", () => {
  return Result;
});

Mock.mock("/sys/menu/nav", "get", () => {
  let nav = [
    {
      name: "SysMange",
      title: "系统管理",
      iconClass: "el-icon-s-operation",
      component: "",
      children: [
        {
          name: "SysUser",
          title: "用户管理",
          iconClass: "el-icon-s-custom",
          route: "/sys/user",
          component: "sys/User",
        },
        {
          name: "SysRole",
          title: "角色管理",
          iconClass: "el-icon-rank",
          route: "/sys/role",
          component: "sys/Role",
        },
        {
          name: "SysMenu",
          title: "菜单管理",
          iconClass: "el-icon-menu",
          route: "/sys/menu",
          component: "sys/Menu",
        },
      ],
    },
    {
      name: "SysTools",
      title: "系统工具",
      iconClass: "el-icon-s-tools",
      component: "",
      children: [
        {
          name: "SysDict",
          title: "数字字典",
          iconClass: "el-icon-s-order",
          route: "/dictionary",
          component: "sys/Dictionary",
        },
      ],
    },
  ];
  let authoriys = ["sys:user:list", "sys:user:save", "sys:user:delete"];
  Result.data = {
    nav: nav,
    authoriys: authoriys,
  };
  return Result;
});

// 菜单数据
Mock.mock("/sys/menu/list", "get", () => {
  let menus = [
    {
      id: 1,
      created: "2022-06-22",
      update: "2022-06-22",
      statu: 1,
      parentId: 0,
      name: "系统管理",
      path: "",
      perms: "sys:manage",
      component: "",
      type: 0,
      icon: "el-icon-s-operation",
      orderNum: 1,
      children: [
        {
          id: 2,
          created: "2022-06-22",
          update: "2022-06-22",
          statu: 1,
          parentId: 1,
          name: "用户管理",
          path: "/sys/user",
          perms: "sys:user:list",
          component: "sys/User",
          type: 1,
          icon: "el-icon-s-custom",
          orderNum: 1,
          children: [
            {
              id: 21,
              created: "2022-06-22",
              update: "2022-06-22",
              statu: 1,
              parentId: 2,
              name: "添加用户",
              path: "",
              perms: "sys:user:save",
              component: "",
              type: 2,
              icon: "",
              orderNum: 1,
            },
            {
              id: 22,
              created: "2022-06-22",
              update: "2022-06-22",
              statu: 1,
              parentId: 2,
              name: "更新用户",
              path: "",
              perms: "sys:user:update",
              component: "",
              type: 2,
              icon: "",
              orderNum: 2,
            },
            {
              id: 23,
              created: "2022-06-22",
              update: "2022-06-22",
              statu: 1,
              parentId: 2,
              name: "删除用户",
              path: "",
              perms: "sys:user:delete",
              component: "",
              type: 2,
              icon: "",
              orderNum: 3,
            },
          ],
        },
        {
          id: 3,
          created: "2022-06-22",
          update: "2022-06-22",
          statu: 1,
          parentId: 1,
          name: "角色管理",
          path: "/sys/role",
          perms: "sys:role:list",
          component: "sys/Role",
          type: 1,
          icon: "el-icon-rank",
          orderNum: 2,
          children: [],
        },
      ],
    },
  ];
  Result.data = menus;
  return Result;
});
Mock.mock(RegExp("/sys/menu/info/*"), "get", () => {
  Result.data = {
    id: 3,
    statu: 1,
    parentId: 1,
    name: "角色管理",
    path: " / sys/roles",
    perms: "sys:role:list",
    component: "sys/Role",
    type: 1,
    icon: "el-icon-rank",
    orderNum: 2,
    children: [],
  };
  return Result;
});
Mock.mock(RegExp("/sys/menu/*"), "post", () => {
  return Result;
});

Mock.mock(RegExp("/sys/role/list*"), "get", () => {
  Result.data = {
    records: [
      {
        id: 3,
        created: "2021-日1-04T10:09: 14",
        updated: "2021-e1-30Te8:19:52",
        statu: 1,
        name: "普通用户",
        code: "normal",
        remark: "只有基本查看功能",
        menuIds: [],
      },
      {
        id: 6,
        created: "2021-e1-16T13:29:03",
        updated: "2021-日1-17T15:50:45",
        statu: 1,
        name: "超级管理员",
        code: "admin",
        remark: "系统默认最高权限，不可以编辑和任意修改",
        menuIds: [],
      },
    ],

    total: 2,
    size: 10,
    current: 1,
    orders: [],
    optimizeCountsql: true,
    hitcount: false,
    countId: null,
    maxLimit: null,
    searchcount: true,
    pages: 1,
  };

  return Result;
});

Mock.mock(RegExp("/sys/role/info/*"), "get", () => {
  Result.data = {
    id: 6,
    created: "2021-e1-16T13:29:03",
    updated: "2021-01-17T15:50:45",
    statu: 1,
    name: "超级管理员",
    code: "admin",
    remark: "系统默认最高权限，不可以编辑和任意修改",
    menuIds: [22],
  };
  return Result;
});
Mock.mock(RegExp("/sys/role/*"), "post", () => {
  return Result;
});
Mock.mock(RegExp("/sys/user/list*"), "get", () => {
  Result.data = {
    records: [
      {
        id: 17,
        username: "超级管理员",
        avatar: require("./assets/touxiang.jpg"),
        created: "2021-e1-16T13:29:03",
        updated: "2021-01-17T15:50:45",
        code: "admin",
        roles: ["超级管理员", "普通用户"],
        email: "123@qq.com",
        phone: 111111,
        statu: 1,
      },
    ],
  };
  return Result;
});

Mock.mock(RegExp("/sys/user/info/*"), "get", () => {
  Result.data = {
    id: 17,
    username: "admin",
    avatar: require("./assets/touxiang.jpg"),
    created: "2021-e1-16T13:29:03",
    updated: "2021-01-17T15:50:45",
    code: "admin",
    roles: ["超级管理员", "普通用户"],
    email: "123@qq.com",
    phone: 111111,
    statu: 1,
  };
  return Result;
});
Mock.mock(RegExp("/sys/user/*"), "post", () => {
  return Result;
});
