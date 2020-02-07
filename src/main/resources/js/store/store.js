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
        currentSessionVotings: []
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
        initialiseStore(state) {
            if (localStorage.getItem('store')) {
                this.replaceState(
                    Object.assign(state, JSON.parse(localStorage.getItem('store')))
                );
            }
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
                if (!localVotingKeyIsValid) {
                    await commit("deleteInvalidVotingSessionMutation", oldVotingSessionData)
                    await commit("addCurrentSessionVotingMutation", newVotingSessionData)
                }
            }
        },
        async addNewVotingAction({commit}, newVoting) {
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
        }
    }
})

store.subscribe((mutation, state) => {
    let store = {
        currentSessionVotings: state.currentSessionVotings,
    }
    localStorage.setItem('store', JSON.stringify(store));
})