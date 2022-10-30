<template>
  <div class="UserCenter">
    <h1>欢迎您，{{ userInfo.username }}</h1>
    <el-form
      :model="passForm"
      status-icon
      :rules="rules"
      ref="ruleForm"
      label-width="100px"
      class="demo-ruleForm input"
    >
      <el-form-item label="旧密码" prop="oldPass">
        <el-input
          type="password"
          v-model="passForm.oldPass"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPass">
        <el-input
          type="password"
          v-model="passForm.newPass"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input
          type="password"
          v-model="passForm.checkPass"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')"
          >提交</el-button
        >
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  created() {
    this.getUserInfo();
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("sys/user/updatePass", this.passForm).then((res) => {
            this.$message({
              showClose: true,
              message: "恭喜你，修改成功",
              type: "success",
              onClose: () => {},
            });
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.data;
      });
    },
  },

  data() {
    var validateOldPass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.passForm.oldPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validateNewPass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.passForm.newPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.passForm.newPass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      passForm: {
        oldPass: "",
        newPass: "",
        checkPass: "",
      },
      rules: {
        oldPass: [
          { required: true, validator: validateOldPass, trigger: "blur" },
        ],
        newPass: [
          { required: true, validator: validateNewPass, trigger: "blur" },
        ],
        checkPass: [
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
      },
      userInfo: {
        id: "",
        username: "",
        avatar: "", //头像
      },
    };
  },
};
</script>
<style scoped>
h1 {
  margin: 0;
}
.UserCenter {
  text-align: center;
  margin-top: 100px;
}
.input {
  width: 380px;
  display: inline-block;
  margin-top: 40px;
}
</style>
