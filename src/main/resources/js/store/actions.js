import serverValidation from "../api/serverValidation";
import api from "../api/server";
import router from "../router/router";
import rotesNames from "../router/routesNames";

export let actions = {
    async checkCurrentSessionVotingAction({commit, getters}, newVotingSessionData) {
        let oldVotingSessionData = getters.getVotingById(newVotingSessionData.votingId)
        if (oldVotingSessionData == null) {
            await commit("addCurrentSessionVotingMutation", newVotingSessionData)
        } else {
            let keyValidationResult = await serverValidation.validateKey(oldVotingSessionData.votingId, oldVotingSessionData.votingKey)
            let localVotingKeyIsValid = keyValidationResult.keyIsValid
            if (!localVotingKeyIsValid.body) {
                await commit("deleteInvalidVotingSessionMutation", oldVotingSessionData)
                await commit("addCurrentSessionVotingMutation", newVotingSessionData)
            }
        }
    },
    async addNewVotingAction({commit}, newVoting) {
        try {
            const data = await api.addVoting(newVoting)
            await commit("addCurrentSessionVotingMutation", data)
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