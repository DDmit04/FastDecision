import Vue from 'vue'
import VueRouter from 'vue-router'
import createVoting from "../components/createVoting.vue"
import currentVoting from "../components/currentVoting.vue"
import votingResults from "../components/votingResults.vue"
import page404 from "../components/page404.vue"

Vue.use(VueRouter)

const routes = [

    { path: '/', name: 'main', component: createVoting },
    { path: '/currentVoting/:votingId', name: 'currentVoting', component: currentVoting, props: true },
    { path: '/votingResults/:votingId', name: 'votingResults', component: votingResults, props: true },
    { path: '/404', name:'page404', component: page404 },
    { path: '*', redirect:'/404' },

]

export default new VueRouter({
    mode: 'history',
    routes: routes
})