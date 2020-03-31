import Vue from 'vue'
import vuetifyPlugin from "../../plugins/vuetifyPlugin";
import 'vuetify/dist/vuetify.min.css'
import Donut from 'vue-css-donut-chart'
import filters from "../filters/filters"
import 'regenerator-runtime'

for(let name in filters) {
    Vue.filter(name, filters[name]);
}

Vue.use(Donut)
Vue.use(vuetifyPlugin, {
    iconfont: 'mdi'
})