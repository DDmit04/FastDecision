import {createLocalVue, mount, shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vue from 'vue'
import VuexMock from 'vuex'
import Vuetify from 'vuetify'
import VueRouter from 'vue-router'
import {opts} from "../../plugins/vuetifyOptions"
import filters from "../filters/filters";
import routesNames from "../router/routesNames";

const localVueMock = createLocalVue()
localVueMock.use(VuexMock)
localVueMock.use(VueRouter)
Vue.use(Vuetify)

for(let name in filters) {
    localVueMock.filter(name, filters[name]);
}

const setupedVuetifyMock = new Vuetify(opts)
const setupedRouterMock = new VueRouter({
    routes: [
        {
            path: '/',
            name: routesNames.MAIN,
        },
        {
            path: '/admin',
            name: routesNames.ASK_ADMIN_PAGE,
        },
        {
            path: '/dashboard/:userId',
            name: routesNames.USER_VOTINGS_CHART,
        },
        {
            path: '/:votingId/vote',
            name: routesNames.CURRENT_VOTING,
        },
        {
            path: '/:votingId/access',
            name: routesNames.PROTECTED_VOTING_ACCESS,
        },
        {
            path: '/:votingId/results',
            name: routesNames.VOTING_RESULTS,
        },
        {
            path: '/newest',
            name: routesNames.NEWEST_VOTINGS,
        },
        {
            path: '/popular',
            name: routesNames.POPULAR_VOTINGS,
        },
        {
            path: '/search/:stringToSearch',
            name: routesNames.SEARCH_RESULTS,
        },
        {
            path: '/err',
            name: routesNames.ERROR_PAGE,
        }
    ]
})

export {
    shallowMount, mount,
    flushPromises,
    localVueMock,
    VuexMock,
    Vuetify,
    setupedRouterMock,
    setupedVuetifyMock
}