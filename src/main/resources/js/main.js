import Vue from 'vue'
import '@babel/polyfill'
import 'api/resource'
import App from 'pages/App.vue'
import VueResource from 'vue-resource'
import vuetify from '../plugins/vuetifyPlugin'
import router from 'router/router'

Vue.use(VueResource)
Vue.config.productionTip = false

new Vue ({
    el: '#app',
    vuetify,
    router,
    render: a => a(App)
})