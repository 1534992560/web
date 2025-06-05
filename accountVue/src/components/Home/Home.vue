<template>
  <div id="home">
    <div class="menu">
      <div class="title">
        <img src="./logo.png" alt="logo"/>
        <span>家庭记账系统</span>
      </div>
      <el-menu
          :default-active="defaultMenu"
          class="el-menu-list"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
      >
        <el-menu-item index="1" @click="changeRoute('/system/accountManage')">
          <i class="el-icon-notebook-1"></i>
          <span>账单管理</span>
        </el-menu-item>
        <el-menu-item index="2" @click="changeRoute('/system/statistics')">
          <i class="el-icon-pie-chart"></i>
          <span>账单统计</span>
        </el-menu-item>
        <el-menu-item index="4" @click="changeRoute('/system/consumeManage')">
          <i class="el-icon-tickets"></i>
          <span>消费类型</span>
        </el-menu-item>
        <el-menu-item
            v-if="isAdmin"
            index="3"
            @click="changeRoute('/system/userManage')"
        >
          <i class="el-icon-user"></i>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="wrap">
      <div class="header">
        <div class="welcome-text" :title="'欢迎您，' + name">
          欢迎您，{{ name }}
        </div>
        <div class="header-actions">
          <el-dropdown @command="handleCommand" trigger="click">
            <span class="el-dropdown-link">
              <i class="el-icon-user"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="editPassword">修改密码</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-tooltip content="退出登录" placement="bottom">
            <i class="el-icon-switch-button logout-icon" @click="loginOut('/login')"></i>
          </el-tooltip>
        </div>
      </div>
      <div class="main-content">
        <keep-alive>
          <component :is="currentComponent"></component>
        </keep-alive>
      </div>
    </div>
    <el-dialog
        title="修改密码"
        :visible.sync="dialogFormVisible"
        width="400px"
        destroy-on-close
    >
      <el-form :model="form" :rules="passwordRules" ref="passwordForm">
        <el-form-item label="原密码" prop="oldPassWord">
          <el-input v-model="form.oldPassWord" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input v-model="form.password" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassWord">
          <el-input v-model="form.confirmPassWord" type="password" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closePasswordDialog">取 消</el-button>
        <el-button type="primary" @click="submitPasswordChange">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import UserManage from '../UserManage/UserManage.vue'
import Statistics from '../Statistics/Statistics.vue'
import AccountsManage from '../AccountsManage/AccountsManage.vue'
import ConsumeManage from '../ConsumeManage/ConsumeManage.vue'
import { put } from '../../utils/http'
import { Message } from 'element-ui'

export default {
  name: 'Home',
  components: {
    UserManage,
    Statistics,
    AccountsManage,
    ConsumeManage
  },
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }

    return {
      path: window.location.hash,
      dialogFormVisible: false,
      form: {
        oldPassWord: '',
        password: '',
        confirmPassWord: ''
      },
      name: sessionStorage.getItem('name'),
      isAdmin: false,
      defaultMenu: '1',
      passwordRules: {
        oldPassWord: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6个字符', trigger: 'blur' }
        ],
        confirmPassWord: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    currentComponent() {
      if (this.path.includes('statistics')) return 'Statistics'
      if (this.path.includes('userManage')) return 'UserManage'
      if (this.path.includes('consumeManage')) return 'ConsumeManage'
      return 'AccountsManage'
    }
  },
  mounted() {
    const router = {
      '#/system/accountManage': '1',
      '#/system/statistics': '2',
      '#/system/userManage': '3',
      '#/system/consumeManage': '4'
    }
    this.defaultMenu = router[window.location.hash]
    this.updateAdminStatus()

    this.$router.afterEach(() => {
      this.path = window.location.hash
    })
  },
  methods: {
    changeRoute(path) {
      this.path = path
      this.$router.push(path)
    },
    loginOut(path) {
      sessionStorage.clear()
      this.$router.push(path)
    },
    handleCommand(command) {
      if (command === 'editPassword') {
        this.dialogFormVisible = true
      }
    },
    closePasswordDialog() {
      this.dialogFormVisible = false
      this.$refs.passwordForm.resetFields()
    },
    submitPasswordChange() {
      this.$refs.passwordForm.validate(valid => {
        if (!valid) return
        
        put('/user/updateMyPsw', {
          name: this.name,
          oldPassword: this.form.oldPassWord,
          newPassword: this.form.password
        }).then(res => {
          if (res.description === 'success') {
            Message.success('密码修改成功')
            this.closePasswordDialog()
          }
        })
      })
    },
    updateAdminStatus() {
      this.isAdmin = sessionStorage.getItem('isAdmin') === '1'
      if (!this.isAdmin && this.path.includes('userManage')) {
        this.changeRoute('/system/accountManage')
      }
    }
  }
}
</script>

<style scoped>
#home {
  height: 100vh;
  display: flex;
  background-color: #f0f2f5;
}

.menu {
  width: 220px;
  background: #545c64;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.title {
  display: flex;
  align-items: center;
  padding: 16px;
  color: #fff;
  font-size: 18px;
  font-weight: 600;
}

.title img {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}

.el-menu {
  border-right: none;
}

.wrap {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.welcome-text {
  font-size: 14px;
  color: #666;
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.el-dropdown-link {
  cursor: pointer;
  font-size: 20px;
  color: #666;
}

.logout-icon {
  font-size: 20px;
  color: #666;
  cursor: pointer;
  transition: color 0.3s;
}

.logout-icon:hover {
  color: #409EFF;
}

.main-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

/* 自定义滚动条样式 */
.main-content::-webkit-scrollbar {
  width: 6px;
}

.main-content::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.main-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}
</style>
