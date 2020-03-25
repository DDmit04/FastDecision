import routesNames from "./routesNames";
import createVoting from "../pages/voting/createVoting.vue";
import adminPage from "../pages/adminPage.vue";
import userChart from "../pages/charts/userChart.vue";
import currentVoting from "../pages/voting/currentVoting.vue";
import protectedVotintgAccess from "../pages/voting/protectedVotintgAccess.vue";
import votingResults from "../pages/voting/votingResults.vue";
import topChart from "../pages/charts/topChart.vue";
import searchChart from "../pages/charts/searchChart.vue";
import errorPage from "../pages/errorPage.vue";

export const routes = [

    {
        path: '/',
        name: routesNames.MAIN,
        component: createVoting
    },
    {
        path: '/dashboard/askAdmin',
        name: routesNames.ASK_ADMIN_PAGE,
        component: adminPage
    },
    {
        path: '/dashboard/:userId',
        name: routesNames.USER_VOTINGS_CHART,
        component: userChart,
        props: true
    },
    {
        path: '/:votingId/vote',
        name: routesNames.CURRENT_VOTING,
        component: currentVoting,
        props: (route) => ({
            votingKey: route.query.votingKey || route.params.votingKey,
            votingId: route.params.votingId
        })
    },
    {
        path: '/:votingId/access',
        name: routesNames.PROTECTED_VOTING_ACCESS,
        component: protectedVotintgAccess,
        props: true
    },
    {
        path: '/:votingId/results',
        name: routesNames.VOTING_RESULTS,
        component: votingResults,
        props: (route) => ({
            votingKey: route.query.votingKey || route.params.votingKey,
            votingId: route.params.votingId
        })
    },
    {
        path: '/newest',
        name: routesNames.NEWEST_VOTINGS,
        component: topChart,
        props: {topChartType: 'newest'}
    },
    {
        path: '/popular',
        name: routesNames.POPULAR_VOTINGS,
        component: topChart,
        props: {topChartType: 'popular'}
    },
    {
        path: '/search/:stringToSearch',
        name: routesNames.SEARCH_RESULTS,
        component: searchChart,
        props: true
    },
    {
        path: '/err',
        name: routesNames.ERROR_PAGE,
        component: errorPage,
        props: true
    },
    {
        path: '*',
        component: errorPage,
        props: {errorCode: 404}
    },

]