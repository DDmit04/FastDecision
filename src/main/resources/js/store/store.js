import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import * as Cookie from 'js-cookie'
import {state} from "./state"
import {getters} from "./getters"
import {mutations} from "./mutations"
import {actions} from "./actions"

Vue.use(Vuex)

export const store = new Vuex.Store({
    strict: isDevMode,
    state,
    plugins: [
        createPersistedState({
            paths: ['isDarkTheme'],
            getState: (key) => Cookie.getJSON(key),
            setState: (key, state) => Cookie.set(key, state, {expires: 1, secure: false})
        })
    ],
    getters,
    mutations,
    actions
})

store.subscribe((mutation, state) => {
    let store = {
        currentSessionVotings: state.currentSessionVotings,
        version: state.version
    }
    localStorage.setItem('store', JSON.stringify(store));
})