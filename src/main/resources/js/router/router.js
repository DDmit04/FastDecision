import Vue from 'vue'
import VueRouter from 'vue-router'
import createVoting from "../pages/voting/createVoting.vue"
import currentVoting from "../pages/voting/currentVoting.vue"
import votingResults from "../pages/voting/votingResults.vue"
import topChart from "../pages/charts/topChart.vue"
import userChart from "../pages/charts/userChart.vue"
import errorPage from "../pages/errorPage.vue"
import adminPage from "../pages/adminPage.vue"
import searchChart from "../pages/charts/searchChart.vue"
import protectedVotintgAccess from "../pages/voting/protectedVotintgAccess.vue";
import rotesNames from "./routesNames"

Vue.use(VueRouter)

const routes = [

    {
        path: '/',
        name: rotesNames.MAIN,
        component: createVoting
    },
    {
        path: '/dashboard/askAdmin',
        name: rotesNames.ASK_ADMIN_PAGE,
        component: adminPage
    },
    {
        path: '/dashboard/:userId',
        name: rotesNames.USER_VOTINGS_CHART,
        component: userChart,
        props: true
    },
    {
        path: '/:votingId/vote',
        name: rotesNames.CURRENT_VOTING,
        component: currentVoting,
        props: (route) => ({
            votingKey: route.query.votingKey || route.params.votingKey,
            votingId: route.params.votingId
        })
    },
    {
        path: '/:votingId/access',
        name: rotesNames.PROTECTED_VOTING_ACCESS,
        component: protectedVotintgAccess,
        props: true
    },
    {
        path: '/:votingId/results',
        name: rotesNames.VOTING_RESULTS,
        component: votingResults,
        props: (route) => ({
            votingKey: route.query.votingKey || route.params.votingKey,
            votingId: route.params.votingId
        })
    },
    {
        path: '/newest',
        name: rotesNames.NEWEST_VOTINGS,
        component: topChart,
        props: {topChartType: 'newest'}
    },
    {
        path: '/popular',
        name: rotesNames.POPULAR_VOTINGS,
        component: topChart,
        props: {topChartType: 'popular'}
    },
    {
        path: '/search/:stringToSearch',
        name: rotesNames.SEARCH_RESULTS,
        component: searchChart,
        props: (route) => ({
            stringToSearch: route.query.search
        })
    },
    {
        path: '/err',
        name: rotesNames.ERROR_PAGE,
        component: errorPage,
        props: true
    },
    {
        path: '*',
        component: errorPage,
        props: {errorCode: 404}
    },

]

export default new VueRouter({
    mode: 'history',
    routes: routes
})