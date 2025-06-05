<template>


  <div class="container">

    <div style="width: 25%; display: flex;background-color: white; ">

      <div style="flex: 1;width: 50%;padding: 40px; display: flex;flex-direction: column;justify-content: center;">
        <div style="text-align: center; font-size: 30px; margin-bottom: 40px;color: #000000">家 庭 记 账 系 统</div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="username" style="margin-bottom: 20px">
            <el-input placeholder="请输入账号" prefix-icon="el-icon-user" v-model="account" class="input-text"></el-input>
          </el-form-item>
          <el-form-item prop="password " style="margin-bottom: 20px">
            <el-input placeholder="请输入密码" prefix-icon="el-icon-lock" v-model="password" show-password
                      class="input-text"></el-input>
          </el-form-item>

          <el-form-item>
            <div style="display: flex;margin-top: 20px">
              <div style="flex: 1;margin-right: 10px">
                <el-button style="width: 100%; background-color: #045ce0;  color: white" v-on:click="login">登 录
                </el-button>
              </div>
              <div style="flex: 1">
                <el-button style="width: 100%; background-color: #d4690c;  color: white" v-on:click="open">注 册
                </el-button>
              </div>
            </div>
            <div style="text-align: right; margin-top: 10px">
              <el-link type="primary" @click="openResetPassword">忘记密码？</el-link>
            </div>
          </el-form-item>
        </el-form>
      </div>

    </div>

    <el-dialog
        title="注册家庭成员"
        :visible.sync="dialogVisible"
        width="30%">
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
            <el-button slot="append" @click="sendCode" :disabled="codeSent">
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
        width="30%">
      <el-form :model="resetPasswordForm" :rules="resetPasswordRules" ref="resetPasswordForm">
        <el-form-item prop="email" label="邮箱">
          <el-input placeholder="请输入注册时的邮箱" v-model="resetPasswordForm.email" class="input-text">
            <el-button slot="append" @click="sendResetCode" :disabled="resetCodeSent">
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
        // 先验证邮箱格式
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
          callback(new Error('请输入正确的邮箱格式'));
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
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
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
  overflow: hidden;
  /*background-color: #42b983;*/
  background-image: url("../../assets/家庭.jpg");
  background-size: 100% 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}


#login {
  height: 100%;
  background: url('https://cn.bing.com/th?id=OHR.MoonDogs_ZH-CN5201314184_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp') 50% 50% / cover;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-title {
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 30px;
}

.login-content {
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form {
  padding: 30px 60px 60px 60px;
  width: 400px;
}

.input-text {
  margin-bottom: 25px;
}

.submit-button {
  width: 96%;
}
</style>
