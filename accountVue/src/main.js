import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import Login from './components/Login/Login.vue';
import Home from './components/Home/Home.vue';
import echarts from 'echarts';
import './assets/reset.css';

Vue.use(ElementUI);
Vue.config.productionTip = false
const originalPush = VueRouter.prototype.push
   VueRouter.prototype.push = function push(location) {
   return originalPush.call(this, location).catch(err => err)
}
Vue.use(VueRouter); //挂载属性
Vue.prototype.$echarts = echarts



let router = new VueRouter({
  routes: [
      { path:'/',redirect: '/login' },
      { path: '/system*', name: 'Home', component: Home },
      { path: '/login', name: 'Login', component: Login },
     
  ]
});

new Vue({
   //让vue知道我们的路由规则
  router: router, //可以简写router
  render: h => h(App),
}).$mount('#app')
