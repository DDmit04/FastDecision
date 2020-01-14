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
import rotesNames from "./router/rotesNames"

Vue.use(Donut);

Vue.use(VueResource)
Vue.config.productionTip = false

Vue.config.errorHandler = (err, vm, info) => {
    if(err.status != '' && err.status != null) {
        vm.$router.push({name: rotesNames.ERROR_PAGE, params: {errorCode: err.status}})
    }
}

new Vue ({
    el: '#app',
    vuetify,
    router,
    store,
    render: a => a(App)
})