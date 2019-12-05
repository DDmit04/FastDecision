import Vue from 'vue'
import VueRouter from 'vue-router'
import createVoting from "../components/createVoting.vue"
import currentVoting from "../components/currentVoting.vue"

Vue.use(VueRouter)

const routes = [

    { path: '/', name: 'createVoting', component: createVoting },
    { path: '/:votingId', name: 'currentVoting', component: currentVoting, props: true },
    { path: '*', component: createVoting },
]

export default new VueRouter({
    mode: 'history',
    routes: routes
})