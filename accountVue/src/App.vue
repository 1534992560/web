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
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  /* margin-top: 60px; */
  width: 100%;
  height: 100%;
}
.el-popconfirm__main {
  font-size: 13px;
  padding-bottom: 10px;
  letter-spacing: 1px;
}
</style>

