import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import * as Cookie from 'js-cookie'
import serverValidation from "../api/serverValidation"
import api from "../api/server"
import rotesNames from "../router/routesNames"
import router from "../router/router"

Vue.use(Vuex)

export const store = new Vuex.Store({
    strict: isDevMode,
    state: {
        currentUser: currentUser,
        isDarkTheme: false,
        currentSessionVotings: [],
        version: ''
        // inside each currentSessionVotings:
        //{
        //     votingId: voting.id,
        //     votingKey: voting.votingKey
        //}

    },
    plugins: [
        createPersistedState({
            paths: ['isDarkTheme'],
            getState: (key) => Cookie.getJSON(key),
            setState: (key, state) => Cookie.set(key, state, {expires: 1, secure: false})
        })
    ],
    getters: {
        getVotingById(state) {
            return (id) => {
                let result = state.currentSessionVotings.find(voting => voting.votingId == id)
                return result;
            }
        },
    },
    mutations: {
        initialiseStore(state, version) {
            if (localStorage.getItem('store')) {
                let store = JSON.parse(localStorage.getItem('store'));
                if (store.version == version) {
                    this.replaceState(
                        Object.assign(state, store)
                    );
                } else {
                    state.version = version;
                }
            }
        },
        refreshCurrentUserRolesMutations(state, newRoles) {
            state.currentUser.roles = newRoles
        },
        changeThemeMutation(state) {
            state.isDarkTheme = !state.isDarkTheme
        },
        addCurrentSessionVotingMutation(state, voting) {
            let votingId = voting.votingId || voting.id
            let votingKey = voting.votingKey || 'public'
            state.currentSessionVotings.push({
                votingId: votingId,
                votingKey: votingKey,
            })
        },
        deleteInvalidVotingSessionMutation(state, votingId) {
            let voting = state.currentSessionVotings.find(voting => voting.votingId == votingId)
            let index = state.currentSessionVotings.indexOf(voting)
            state.currentSessionVotings.splice(index, 1)
        }
    },
    actions: {
        async checkCurrentSessionVotingAction({commit, getters}, newVotingSessionData) {
            let oldVotingSessionData = getters.getVotingById(newVotingSessionData.votingId)
            if (oldVotingSessionData == null) {
                await commit("addCurrentSessionVotingMutation", newVotingSessionData)
            } else {
                let localVotingKeyIsValid = await serverValidation.validateKey(oldVotingSessionData.votingId, oldVotingSessionData.votingKey)
                if (!localVotingKeyIsValid.body) {
                    await commit("deleteInvalidVotingSessionMutation", oldVotingSessionData)
                    await commit("addCurrentSessionVotingMutation", newVotingSessionData)
                }
            }
        },
        async addNewVotingAction({commit}, newVoting) {
            try {
                const response = await api.addVoting(newVoting)
                const data = await response.json()
                await commit("addCurrentSessionVotingMutation", data)
                await router.push({
                    name: rotesNames.CURRENT_VOTING,
                    params: {
                        votingId: data.id,
                        votingKey: data.votingKey
                    },
                })
            } catch (err) {
                let allReasons = ''
                let separator = ', '
                if(err.data.errors.length != 0) {
                    await err.data.errors.forEach((error, index) => {
                        if (index == err.data.errors.length - 1) {
                            separator = ''
                        }
                        allReasons += error.defaultMessage + separator
                    })
                } else {
                    allReasons = err.data.message
                }
                await router.push({
                    name: rotesNames.ERROR_PAGE,
                    params: {
                        errorCode: err.status,
                        errorReason: allReasons
                    }
                })
            }
        }
    }
})

store.subscribe((mutation, state) => {
    let store = {
        currentSessionVotings: state.currentSessionVotings,
        version: state.version
    }
    localStorage.setItem('store', JSON.stringify(store));
})