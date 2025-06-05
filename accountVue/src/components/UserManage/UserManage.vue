<template>
  <div class="user-manage">
    <!-- 顶部操作栏 -->
    <div class="action-bar">
      <el-button type="primary" icon="el-icon-plus" @click="dialogFormVisible = true" class="add-button">
        新增成员
      </el-button>
    </div>

    <!-- 用户列表 -->
    <div class="table-wrapper" v-loading="loading">
      <el-table
        class="user-table"
        :data="tableData"
        style="width: 100%"
      >
        <el-table-column prop="index" label="序号" type="index" width="80" align="center">
        </el-table-column>
        <el-table-column prop="name" label="姓名" min-width="120" align="center">
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180" align="center">
        </el-table-column>
        <el-table-column
          prop="createTime"
          :formatter="formatterTime"
          label="创建时间"
          min-width="180"
          align="center"
        >
        </el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button
                type="text"
                size="small"
                class="action-button reset"
                @click="resetPassWord(scope.row)"
              >重置密码</el-button>
              <el-divider direction="vertical"></el-divider>
              <el-button
                type="text"
                size="small"
                class="action-button delete"
                @click="confirmDelete(scope.row)"
              >删除</el-button>
              <el-divider direction="vertical"></el-divider>
              <el-button
                type="text"
                size="small"
                class="action-button transfer"
                @click="confirmTransfer(scope.row)"
              >转让管理员</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page.sync="pageNum"
          :total="total"
          :page-size="pageSize"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
    </div>

    <!-- 新增用户对话框 -->
    <el-dialog
      title="新增家庭成员"
      :visible.sync="dialogFormVisible"
      width="400px"
      destroy-on-close
      custom-class="user-dialog"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="账号" prop="name">
          <el-input v-model="form.name" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="addUser('form')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 重置密码结果对话框 -->
    <el-dialog
      :visible.sync="passwordModalVisible"
      width="400px"
      title="重置密码结果"
      destroy-on-close
      custom-class="password-dialog"
    >
      <div class="password-result">{{ resetPassword }}</div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="passwordModalVisible = false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { get, post, put } from '../../utils/http'
import moment from 'moment'
import { Message, MessageBox } from 'element-ui'

export default {
  name: 'UserManage',
  data() {
    const validateName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入账号'))
      } else {
        callback()
      }
    }

    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码'))
      } else if (value.length < 6) {
        callback(new Error('密码长度不能小于6个字符'))
      } else {
        callback()
      }
    }

    const validateEmail = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入邮箱'));
      } else {
        // 邮箱格式验证，必须以.com结尾
        const emailRegex = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\.com$/;
        if (!emailRegex.test(value)) {
          callback(new Error('邮箱格式应为：Username@domain.com'));
          return;
        }
        // 验证邮箱是否已被注册
        post('/user/checkEmail', { email: value })
          .then(res => {
            if (res.description === 'success' && res.data === 'exists') {
              callback(new Error('该邮箱已被注册'));
            } else {
              callback();
            }
          })
          .catch(() => {
            callback(new Error('邮箱验证失败，请重试'));
          });
      }
    }

    return {
      tableData: [],
      loading: false,
      dialogFormVisible: false,
      passwordModalVisible: false,
      form: {
        name: '',
        password: '',
        email: ''
      },
      pageNum: 1,
      pageSize: 10,
      total: 0,
      resetPassword: '',
      rules: {
        name: [
          { required: true, message: '账号不能为空' },
          { validator: validateName, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱不能为空' },
          { validator: validateEmail, trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    formatterTime(row, column, cellValue) {
      return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.getList()
    },
    getList() {
      this.loading = true
      get('/user/query', {
        houseId: sessionStorage.getItem('houseId'),
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }).then((res) => {
        this.loading = false
        if (res.description === 'success') {
          this.tableData = res.data.list.filter((item) => item.isAdmin !== 1)
          this.total = res.data.total
        }
      }).catch(() => {
        this.loading = false
        Message.error('获取用户列表失败')
      })
    },
    closeDialog() {
      this.dialogFormVisible = false
      this.$refs.form.resetFields()
    },
    addUser(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) return
        
        post('/user/add', {
          houseId: sessionStorage.getItem('houseId'),
          name: this.form.name,
          password: this.form.password,
          email: this.form.email,
          isAdmin: 0
        }).then((res) => {
          if (res.description === 'success') {
            Message.success('添加成功')
            this.closeDialog()
            this.getList()
          }
        })
      })
    },
    confirmDelete(row) {
      MessageBox.confirm(
        `确定将${row.name}移出本家庭吗?`,
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        this.deleteRow(row)
      }).catch(() => {})
    },
    deleteRow(row) {
      put('/user/remove', {
        removeUserId: row.id
      }).then((res) => {
        if (res.description === 'success') {
          Message.success('移除成功')
          this.getList()
        }
      })
    },
    resetPassWord(row) {
      MessageBox.confirm(
        `确定要重置${row.name}的密码吗?`,
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        put('/user/resetPwd', { 
          targetUserId: row.id
        }).then((res) => {
          if (res.description === 'success') {
            this.resetPassword = `${row.name}的密码已重置为：${res.data.password}`
            this.passwordModalVisible = true
          }
        })
      }).catch(() => {})
    },
    confirmTransfer(row) {
      MessageBox.confirm(
        `确定要将管理员权限转让给${row.name}吗?`,
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        this.transferAdmin(row)
      }).catch(() => {})
    },
    transferAdmin(row) {
      put('/user/transferAdmin', {
        fromUserId: sessionStorage.getItem('userId'),
        toUserId: row.id
      }).then((res) => {
        if (res.description === 'success') {
          Message.success('转让成功')
          sessionStorage.setItem('isAdmin', '0')
          this.$parent.updateAdminStatus()
        }
      })
    }
  }
}
</script>

<style>
.user-manage {
  padding: 20px;
  height: 100%;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.action-bar {
  margin-bottom: 20px;
}

.add-button {
  padding: 12px 20px;
  font-weight: 500;
  border-radius: 4px;
  transition: all 0.3s;
}

.add-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-wrapper {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.user-table {
  margin-bottom: 20px;
}

.el-table {
  border-radius: 8px;
}

.el-table th {
  background-color: #f5f7fa !important;
  color: #606266;
  font-weight: 500;
  height: 50px;
}

.el-table td {
  padding: 12px 0;
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-button {
  padding: 4px 8px;
  font-size: 13px;
  transition: all 0.3s;
}

.action-button.reset {
  color: #409EFF;
}

.action-button.delete {
  color: #F56C6C;
}

.action-button.transfer {
  color: #67C23A;
}

.action-button:hover {
  opacity: 0.8;
  transform: translateY(-1px);
}

.pagination-container {
  padding: 20px 0;
  text-align: right;
}

/* 对话框样式 */
.user-dialog,
.password-dialog {
  border-radius: 8px;
  overflow: hidden;
}

.el-dialog__header {
  background: #f8f9fb;
  padding: 20px;
  margin: 0;
}

.el-dialog__title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.el-dialog__body {
  padding: 30px 20px;
}

.el-form-item {
  margin-bottom: 22px;
}

.el-form-item__label {
  font-weight: normal;
  color: #606266;
}

.el-input__inner {
  height: 40px;
  line-height: 40px;
  border-radius: 4px;
}

.dialog-footer {
  padding: 20px;
  border-top: 1px solid #ebeef5;
  text-align: right;
}

.dialog-footer .el-button {
  padding: 9px 20px;
}

.password-result {
  text-align: center;
  font-size: 16px;
  color: #606266;
  padding: 20px 0;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .user-manage {
    padding: 15px;
  }

  .action-buttons {
    flex-direction: column;
    gap: 8px;
  }

  .el-divider--vertical {
    display: none;
  }

  .action-button {
    width: 100%;
    text-align: center;
  }

  .el-dialog {
    width: 90% !important;
    margin: 0 auto;
  }

  .el-form-item__label {
    float: none;
    text-align: left;
    margin-bottom: 8px;
  }

  .el-form-item__content {
    margin-left: 0 !important;
  }
}
</style>
