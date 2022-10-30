<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
          v-model="searchForm.name"
          placeholder="名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="getRoleList"></el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="add">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="确定全部删除吗？" @confirm="del(null)">
          <el-button slot="reference" type="danger" :disabled="delButton"
            >批量删除</el-button
          >
        </el-popconfirm>
      </el-form-item>
    </el-form>

    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      border
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>

      <el-table-column prop="name" label="名称" width="120"> </el-table-column>
      <el-table-column prop="code" label="唯一编码" width="120px">
      </el-table-column>
      <el-table-column prop="remark" label="描述"> </el-table-column>
      <el-table-column prop="statu" label="状态" width="100px">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.statu === 1" type="success">
            正常
          </el-tag>
          <el-tag size="small" v-if="scope.row.statu === 0" type="danger"
            >禁用
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="操作" width="200px">
        <template slot-scope="scope">
          <el-button type="text" @click="assignmentPerms(scope.row.id)"
            >分配权限</el-button
          >
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" @click="editHandle(scope.row.id)"
            >编辑</el-button
          >
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm
              title="这是一段内容确定删除吗？"
              @confirm="del(scope.row.id)"
            >
              <el-button slot="reference" type="text">删除</el-button>
            </el-popconfirm>
          </template></template
        ></el-table-column
      >
    </el-table>
    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="current"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="size"
      :total="total"
    >
    </el-pagination>

    <!-- 新增对话框 -->
    <el-dialog title="菜单信息" :visible.sync="dialogVisible" width="600px">
      <el-form
        :model="editForm"
        :rules="editFormRules"
        ref="editForm"
        label-width="100px"
        class="demo-editForm"
      >
        <el-form-item label="角色名称" prop="name" label-width="100px">
          <el-input v-model="editForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一编码" prop="code" label-width="100px">
          <el-input v-model="editForm.code" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="remark" label-width="100px">
          <el-input v-model="editForm.remark" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="statu" label-width="100px">
          <el-radio-group v-model="editForm.statu">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item style="margin-left: 100px">
          <el-button type="primary" @click="submitForm('editForm')"
            >提交</el-button
          >
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog title="分配权限" :visible.sync="permDialogVisible" width="600px">
      <el-form :model="permForm">
        <el-tree
          :data="permTreeData"
          show-checkbox
          node-key="id"
          ref="permTree"
          :check-strictly="true"
          :default-expand-all="true"
          :props="defaultProps"
        >
        </el-tree>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="permDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitPermForm('permForm')"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      searchForm: {},
      dialogVisible: false,
      permDialogVisible: false,
      delButton: true,
      total: 0,
      size: 10,
      current: 2,
      editForm: {},
      editFormRules: {
        name: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
        code: [{ required: true, message: " 请输入唯一编码", trigger: "blur" }],

        statu: [{ required: true, message: " 请选择状态", trigger: "blur" }],
      },
      tableData: [],
      multipleSelection: [],
      permForm: {},
      defaultProps: {
        children: "children",
        label: "name",
      },
      permTreeData: [],
    };
  },
  created() {
    this.getRoleList();
    this.$axios.get("/sys/menu/list").then((res) => {
      this.permTreeData = res.data.data;
    });
  },
  methods: {
    add() {
      this.dialogVisible = true;
      this.editForm = {};
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.delButton = val.length == 0; //是否激活批量删除按钮
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.size = val;
      this.getRoleList();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.current = val;
      this.getRoleList();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    getRoleList() {
      this.searchForm.current = this.current;
      this.searchForm.size = this.size;
      this.$axios
        .get("/sys/role/list", {
          params: {
            name: this.searchForm.name,
            current: this.searchForm.current,
            size: this.searchForm.size,
          },
        })
        .then((res) => {
          this.tableData = res.data.data.records;
          this.size = res.data.data.size;
          this.current = res.data.data.current;
          this.total = res.data.data.total;
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios
            .post(
              "/sys/role/" + (this.editForm.id ? "update" : "save"),
              this.editForm
            )
            .then((res) => {
              this.$message({
                showClose: true,
                message: "恭喜你，操作成功",
                type: "success",
              });
              this.getRoleList();
              this.dialogVisible = false;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    editHandle(id) {
      this.$axios.get("/sys/role/info/" + id).then((res) => {
        console.log(res);
        this.editForm = res.data.data;
        this.dialogVisible = true;
      });
    },
    del(id) {
      var ids = [];
      if (id) {
        ids.push(id);
      } else {
        this.multipleSelection.forEach((item) => {
          ids.push(item.id);
        });
      }

      this.$axios.post("/sys/role/delete/" + ids).then((res) => {
        this.$message({
          showClose: true,
          message: "恭喜你，删除成功",
          type: "success",
          onClose: () => {
            this.getRoleList();
          },
        });
      });
    },
    assignmentPerms(id) {
      this.permDialogVisible = true;
      this.$axios.get("/sys/role/info/" + id).then((res) => {
        this.$refs.permTree.setCheckedKeys(res.data.data.menuIds);
        this.permForm = res.data.data;
      });
    },
    submitPermForm(formName) {
      var menuIds = this.$refs.permTree.getCheckedKeys();
      this.$axios
        .post("/sys/role/perm/" + this.permForm.id, menuIds)
        .then((res) => {
          this.$message({
            showClose: true,
            message: "恭喜你，操作成功",
            type: "success",
            onClose: () => {
              this.getRoleList();
            },
          });
          this.permDialogVisible = false;
        });
    },
  },
};
</script>
<style scoped>
.el-pagination {
  float: right;
  margin-top: 20px;
}
</style>
