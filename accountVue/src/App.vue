<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

//import Login from './components/Login/Login.vue';
export default {
  name: "App",
  // components: {
  //    Login,
  // },
};
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  width: 100%;
  height: 100vh;
  margin: 0;
  padding: 0;
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

/* Element UI 组件全局样式优化 */
.el-button {
  border-radius: 4px;
  font-weight: 500;
}

.el-input__inner {
  border-radius: 4px;
}

.el-dialog {
  border-radius: 8px;
}

.el-dialog__header {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.el-dialog__body {
  padding: 30px 20px;
}

.el-dialog__footer {
  padding: 20px;
  border-top: 1px solid #ebeef5;
}

.el-message {
  min-width: 300px;
  padding: 15px 20px;
  border-radius: 8px;
}

.el-popconfirm__main {
  font-size: 13px;
  padding-bottom: 10px;
  letter-spacing: 1px;
}
</style>

