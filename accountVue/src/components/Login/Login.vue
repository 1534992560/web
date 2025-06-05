<template>
  <div class="container">
    <div class="login-box">
      <div class="system-title">家 庭 记 账 系 统</div>
      <el-form :model="form" ref="formRef">
        <el-form-item>
          <el-input placeholder="请输入账号" prefix-icon="el-icon-user" v-model="account" class="input-text"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input placeholder="请输入密码" prefix-icon="el-icon-lock" v-model="password" show-password
                    class="input-text"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="login-btn" v-on:click="login">登 录</el-button>
          <el-button type="warning" class="register-btn" v-on:click="open">注 册</el-button>
          <div class="forget-password">
            <el-link type="primary" @click="openResetPassword">忘记密码？</el-link>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <!-- 注册对话框 -->
    <el-dialog
        title="注册家庭成员"
        :visible.sync="dialogVisible"
        width="400px"
        custom-class="register-dialog">
      <el-form :model="registerForm" :rules="rules" ref="registerForm">
        <el-form-item label="账号" prop="account">
          <el-input placeholder="请输入账号" prefix-icon="el-icon-user" v-model="registerForm.account" class="input-text"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input placeholder="请输入密码" prefix-icon="el-icon-lock" v-model="registerForm.password" show-password
                    class="input-text"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input placeholder="请输入邮箱" prefix-icon="el-icon-message" v-model="registerForm.email" class="input-text">
            <el-button slot="append" @click="sendCode" :disabled="codeSent" :class="{'sending': codeSent}">
              {{ codeSent ? `${countdown}s后重试` : '发送验证码' }}
            </el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verificationCode">
          <el-input placeholder="请输入验证码" prefix-icon="el-icon-key" v-model="registerForm.verificationCode" class="input-text"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="register">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 找回密码对话框 -->
    <el-dialog
        title="找回密码"
        :visible.sync="resetPasswordDialogVisible"
        width="400px"
        custom-class="reset-dialog">
      <el-form :model="resetPasswordForm" :rules="resetPasswordRules" ref="resetPasswordForm">
        <el-form-item prop="email" label="邮箱">
          <el-input placeholder="请输入注册时的邮箱" v-model="resetPasswordForm.email" class="input-text">
            <el-button slot="append" @click="sendResetCode" :disabled="resetCodeSent" :class="{'sending': resetCodeSent}">
              {{ resetCodeSent ? `${resetCountdown}s后重试` : '发送验证码' }}
            </el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="verificationCode" label="验证码">
          <el-input placeholder="请输入验证码" v-model="resetPasswordForm.verificationCode" class="input-text"></el-input>
        </el-form-item>
        <el-form-item prop="newPassword" label="新密码">
          <el-input placeholder="请输入新密码" v-model="resetPasswordForm.newPassword" show-password class="input-text"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword" label="确认密码">
          <el-input placeholder="请再次输入新密码" v-model="resetPasswordForm.confirmPassword" show-password class="input-text"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetPasswordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitResetPassword">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {post} from '../../utils/http'
import {Message} from 'element-ui'

export default {
  name: 'Login',
  data() {
    // 添加邮箱验证器
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
            if (res.description === 'exists') {
              callback(new Error('该邮箱已被注册'));
            } else {
              callback();
            }
          })
          .catch(() => {
            callback(new Error('邮箱验证失败，请重试'));
          });
      }
    };

    // 添加确认密码的验证规则
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.resetPasswordForm.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };

    return {
      account: '',
      password: '',
      registerForm: {
        account: '',
        password: '',
        email: '',
        verificationCode: ''
      },
      dialogVisible: false,
      codeSent: false,
      countdown: 60,
      resetPasswordDialogVisible: false,
      resetPasswordForm: {
        email: '',
        verificationCode: '',
        newPassword: '',
        confirmPassword: ''
      },
      resetCodeSent: false,
      resetCountdown: 60,
      rules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6个字符', trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        verificationCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { len: 6, message: '验证码长度必须为6位', trigger: 'blur' }
        ]
      },
      resetPasswordRules: {
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error('请输入邮箱'));
              } else {
                const emailRegex = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\.com$/;
                if (!emailRegex.test(value)) {
                  callback(new Error('邮箱格式应为：Username@domain.com'));
                } else {
                  callback();
                }
              }
            }, 
            trigger: 'blur' 
          }
        ],
        verificationCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { len: 6, message: '验证码长度必须为6位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.generateCaptcha()
  },
  methods: {
    generateRandomCaptcha() {
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      let result = '';
      for (let i = 0; i < 4; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      return result;
    },
    generateCaptcha() {
      this.captchaText = this.generateRandomCaptcha();
    },
    login: function () {
      if (this.account === '') {
        Message.error({message: '账号不能为空!'})
        return
      }
      if (this.password === '') {
        Message.error({message: '密码不能为空!'})
        return
      }
      post('/user/login', {name: this.account, password: this.password}).then(
          (res) => {
            if (res.description === 'success') {
              Message.success({message: '登录成功'})
              this.$router.push({path: '/system/accountManage'})
              sessionStorage.setItem('userId', res.data[0].id)
              sessionStorage.setItem('houseId', res.data[0].houseId)
              sessionStorage.setItem('name', res.data[0].name)
              sessionStorage.setItem('isAdmin', res.data[0].isAdmin)
            }
          }
      )
    },
    open() {
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.resetForm();
      });
    },
    sendCode() {
      this.$refs.registerForm.validateField('email', (errorMessage) => {
        if (errorMessage) {
          return;
        }
        
        if (!this.registerForm.email) {
          this.$message.error('请先输入邮箱');
          return;
        }
        
        post('/user/sendVerificationCode', {
          email: this.registerForm.email
        }).then(res => {
          if (res.description === 'success') {
            this.$message.success('验证码已发送，请查收邮件');
            this.codeSent = true;
            this.startCountdown();
          } else {
            this.$message.error(res.description || '发送验证码失败');
          }
        }).catch(err => {
          this.$message.error('发送验证码失败，请稍后重试');
          console.error(err);
        });
      });
    },
    startCountdown() {
      const timer = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--;
        } else {
          this.codeSent = false;
          this.countdown = 60;
          clearInterval(timer);
        }
      }, 1000);
    },
    register() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          post('/user/register', {
            name: this.registerForm.account,
            password: this.registerForm.password,
            email: this.registerForm.email,
            verificationCode: this.registerForm.verificationCode,
            budget: 0  // 设置默认预算为0
          }).then(res => {
            if (res.description === 'success') {
              this.$message.success('注册成功');
              this.dialogVisible = false;
              this.resetForm();
            } else {
              this.$message.error(res.description || '注册失败');
            }
          }).catch(err => {
            this.$message.error('注册失败，请稍后重试');
            console.error(err);
          });
        }
      });
    },
    resetForm() {
      this.registerForm = {
        account: '',
        password: '',
        email: '',
        verificationCode: ''
      };
      this.codeSent = false;
      this.countdown = 60;
      if (this.$refs.registerForm) {
        this.$refs.registerForm.resetFields();
      }
    },
    openResetPassword() {
      this.resetPasswordDialogVisible = true;
      this.resetPasswordForm = {
        email: '',
        verificationCode: '',
        newPassword: '',
        confirmPassword: ''
      };
    },
    sendResetCode() {
      this.$refs.resetPasswordForm.validateField('email', (errorMessage) => {
        if (errorMessage) {
          return;
        }
        
        post('/user/sendResetCode', {
          email: this.resetPasswordForm.email
        }).then(res => {
          if (res.description === 'success') {
            this.$message.success('验证码已发送，请查收邮件');
            this.resetCodeSent = true;
            this.startResetCountdown();
          } else {
            this.$message.error(res.description || '发送验证码失败');
          }
        }).catch(err => {
          this.$message.error('发送验证码失败，请稍后重试');
          console.error(err);
        });
      });
    },
    startResetCountdown() {
      const timer = setInterval(() => {
        if (this.resetCountdown > 0) {
          this.resetCountdown--;
        } else {
          this.resetCodeSent = false;
          this.resetCountdown = 60;
          clearInterval(timer);
        }
      }, 1000);
    },
    submitResetPassword() {
      this.$refs.resetPasswordForm.validate(valid => {
        if (valid) {
          post('/user/resetPasswordByEmail', {
            email: this.resetPasswordForm.email,
            verificationCode: this.resetPasswordForm.verificationCode,
            newPassword: this.resetPasswordForm.newPassword
          }).then(res => {
            if (res.description === 'success') {
              this.$message.success(`密码重置成功，您的用户名是：${res.data}，请使用新密码登录`);
              this.resetPasswordDialogVisible = false;
              this.resetPasswordForm = {
                email: '',
                verificationCode: '',
                newPassword: '',
                confirmPassword: ''
              };
            } else {
              this.$message.error(res.description || '密码重置失败');
            }
          }).catch(err => {
            this.$message.error('密码重置失败，请稍后重试');
            console.error(err);
          });
        }
      });
    }
  },
}
</script>

<style>
.container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('../../assets/家庭.jpg') no-repeat center center;
  background-size: cover;
  position: relative;
}

.container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(3px);
}

.login-box {
  position: relative;
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.system-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 30px;
  letter-spacing: 4px;
}

.input-text {
  margin-bottom: 20px;
}

.login-btn, .register-btn {
  width: 45%;
  padding: 12px 0;
  margin: 0 2.5%;
  font-size: 16px;
  letter-spacing: 4px;
}

.forget-password {
  text-align: right;
  margin-top: 15px;
}

/* 对话框样式 */
.register-dialog,
.reset-dialog {
  border-radius: 8px;
}

.el-dialog__header {
  background: #f8f9fb;
  padding: 20px;
  margin: 0;
  border-radius: 8px 8px 0 0;
}

.el-dialog__title {
  font-size: 18px;
  font-weight: 500;
}

.el-dialog__body {
  padding: 30px 20px;
}

.el-form-item {
  margin-bottom: 22px;
}

.dialog-footer {
  padding: 10px 20px 20px;
  text-align: right;
}

.sending {
  background-color: #909399;
  border-color: #909399;
  color: #fff;
  cursor: not-allowed;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .login-box {
    width: 90%;
    max-width: 400px;
    padding: 30px 20px;
  }

  .login-btn,
  .register-btn {
    width: 100%;
    margin: 10px 0;
  }

  .el-dialog {
    width: 90% !important;
    margin: 0 auto;
  }
}
</style>
