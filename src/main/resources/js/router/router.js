import Vue from 'vue'
import VueRouter from 'vue-router'
import createVoting from "../components/createVoting.vue"
import currentVoting from "../components/currentVoting.vue"
import votingResults from "../components/votingResults.vue"
import pageNotFound from "../components/pageNotFound.vue"

Vue.use(VueRouter)

const routes = [

    { path: '/', name: 'main', component: createVoting },
    { path: '/:votingId/vote', name: 'currentVoting', component: currentVoting, props: true },
    { path: '/:votingId/results', name: 'votingResults', component: votingResults, props: true },
    { path: '/404', name:'pageNotFound', component: pageNotFound },
    { path: '*', redirect:'/404' },

]

export default new VueRouter({
    mode: 'history',
    routes: routes
})