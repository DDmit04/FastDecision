import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import * as Cookie from 'js-cookie'

Vue.use(Vuex)

export default new Vuex.Store({
    strict: isDevMode,
    state: {
        currentUser: currentUser,
        isDarkTheme: false,
    },
    plugins: [
        createPersistedState({
            paths: ['isDarkTheme'],
            getState: (key) => Cookie.getJSON(key),
            setState: (key, state) => Cookie.set(key, state, { expires: 1, secure: false })
        })
    ],
    getters: {},
    mutations: {
        changeThemeMutation(state) {
            state.isDarkTheme = !state.isDarkTheme
        }
    },
    actions: {}
})