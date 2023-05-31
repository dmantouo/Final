import Vue from 'vue'
import App from './App.vue'
import router from './routers'
import axios from './http.js'
import apiUrl from './utils/apiUrl'
import store from './store/index'
import * as common from './assets/js/commonJs'
import qs from 'qs'
import ElementUI from 'element-ui'
import locale from 'element-ui/lib/locale/lang/en'

Vue.use(ElementUI, { locale })
Vue.prototype.qs = qs;
Vue.prototype.$echarts = echarts
Vue.prototype.$axios = axios
Vue.prototype.apiUrl=apiUrl
Vue.prototype.common=common
Vue.config.productionTip = false


new Vue({
    el:"#app",
    router,
    store,
    render:h=>h(App)
}).$mount('#app');