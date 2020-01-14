import Vue from 'vue'
import VueRouter from 'vue-router'
import createVoting from "../components/voting/createVoting.vue"
import currentVoting from "../components/voting/currentVoting.vue"
import votingResults from "../components/voting/votingResults.vue"
import topChart from "../components/charts/topChart.vue"
import userChart from "../components/charts/userChart.vue"
import errorPage from "../components/errorPage.vue"
import rotesNames from "./rotesNames"

Vue.use(VueRouter)

const routes = [

    { path: '/', name: rotesNames.MAIN, component: createVoting },
    { path: '/dashboard/:userId', name: rotesNames.USER_VOTINGS_CHART, component: userChart, props: true },
    { path: '/:votingId/vote', name: rotesNames.CURRENT_VOTING, component: currentVoting, props: true },
    { path: '/:votingId/results', name: rotesNames.VOTING_RESULTS, component: votingResults, props: true },
    { path: '/newest', name: rotesNames.NEWEST_VOTINGS, component: topChart, props: {topChartType : 'newest'} },
    { path: '/popular', name: rotesNames.POPULAR_VOTINGS, component: topChart, props: {topChartType : 'popular'} },
    { path: '/err', name: rotesNames.ERROR_PAGE, component: errorPage, props: true},
    { path: '*', component: errorPage, props: {errorCode: 404} },

]

export default new VueRouter({
    mode: 'history',
    routes: routes
})