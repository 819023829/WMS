<template>
  <el-row type="flex" class="row-bg" justify="center">
    <el-col :xl="6" :lg="7">
      <h2>欢迎来到XXX管理系统</h2>
      <el-image
        :src="require('@/assets/erweima.jpg')"
        style="height: 180px; width: 180px"
      ></el-image>
    </el-col>
    <el-col :span="1">
      <el-divider direction="vertical"></el-divider>
    </el-col>
    <el-col :xl="6" :lg="7">
      <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="username" style="width: 380px">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" style="width: 380px">
          <el-input v-model="loginForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" style="width: 380px">
          <el-input
            v-model="loginForm.code"
            style="float: left; width: 155px"
          ></el-input>
          <el-image :src="codeImg" class="codeImg" @click="getCodeImg">
          </el-image>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')"
            >登录</el-button
          >
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>
<script>
import qs from "qs";
export default {
  data() {
    return {
      codeImg: "",
      loginForm: {
        username: "admin",
        password: "111111",
        code: "",
        token: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "长度为 5 个字符", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    submitForm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$axios
            .post("/login?" + qs.stringify(this.loginForm))
            .then((res) => {
              const jwt = res.headers["authorization"];
              this.$store.commit("SET_TOKEN", jwt);
              this.$router.push("/home");
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.loginForm.resetFields();
    },
    getCodeImg() {
      this.$axios.get("/code", this.loginForm).then((res) => {
        console.log(res);
        this.loginForm.token = res.data.data.token;
        this.codeImg = res.data.data.codeImg;
        this.loginForm.code = "";
      });
    },
  },
  created() {
    this.getCodeImg();
  },
};
</script>
<style scoped>
.el-row {
  background-color: #fafafa;
  height: 100%;
  display: flex;
  align-items: center;
  text-align: center;
}
.el-divider {
  height: 200px;
}
.codeImg {
  float: left;
  margin-left: 5px;
  border-radius: 4px;
}
</style>
