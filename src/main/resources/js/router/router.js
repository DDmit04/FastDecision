import Vue from 'vue'
import VueRouter from 'vue-router'
import createVoting from "../components/voting/createVoting.vue"
import currentVoting from "../components/voting/currentVoting.vue"
import votingResults from "../components/voting/votingResults.vue"
import pageNotFound from "../components/pageNotFound.vue"
import topChart from "../components/charts/topChart.vue"
import userChart from "../components/charts/userChart.vue";

Vue.use(VueRouter)

const routes = [

    { path: '/', name: 'main', component: createVoting },
    { path: '/dashboard/:userId', name: 'userChart', component: userChart, props: true },
    { path: '/:votingId/vote', name: 'currentVoting', component: currentVoting, props: true },
    { path: '/:votingId/results', name: 'votingResults', component: votingResults, props: true },
    { path: '/newest', name: 'newestVotings', component: topChart, props: {topChartType : 'newest'} },
    { path: '/popular', name: 'popularVotings', component: topChart, props: {topChartType : 'popular'} },
    { path: '/404', name:'pageNotFound', component: pageNotFound },
    { path: '*', redirect:'/404' },

]

export default new VueRouter({
    mode: 'history',
    routes: routes
})