import Vue from 'vue'
import '@babel/polyfill'
import 'api/resource'
import store from 'store/store'
import App from 'pages/App.vue'
import VueResource from 'vue-resource'
import vuetify from '../plugins/vuetifyPlugin'
import router from 'router/router'
import Donut from 'vue-css-donut-chart'
import 'vue-css-donut-chart/dist/vcdonut.css'

Vue.use(Donut);

Vue.use(VueResource)
Vue.config.productionTip = false

new Vue ({
    el: '#app',
    vuetify,
    router,
    store,
    render: a => a(App)
})