import serverValidation from "../api/serverValidation";
import server from "../api/server";
import router from "../router/router";
import rotesNames from "../router/routesNames";

/**
 * @displayName Vuex actions
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
export let actions = {

    /**
     * @public
     * Check newVotingSessionData in state
     * if empty - validate in server oldVotingSessionData
     * if invalid - call replace oldVotingSessionData by newVotingSessionData
     * if Check newVotingSessionData in state isn't empty - call add newVotingSessionData to store sessions
     * @param{Object} newVotingSessionData voting data to check
     */
    async checkCurrentSessionVotingAction({commit, getters}, newVotingSessionData) {
        let oldVotingSessionData = getters.getVotingById(newVotingSessionData.votingId)
        if (oldVotingSessionData == null) {
            await commit("addCurrentSessionVotingMutation", newVotingSessionData)
        } else {
            let keyValidationResult = await serverValidation.validateKey(oldVotingSessionData.votingId, oldVotingSessionData.votingKey)
            let localVotingKeyIsValid = keyValidationResult.keyIsValid
            if (!localVotingKeyIsValid) {
                await commit("deleteInvalidVotingSessionMutation", oldVotingSessionData)
                await commit("addCurrentSessionVotingMutation", newVotingSessionData)
            }
        }
    },
    /**
     * @public
     * Call add new voting to server and store
     * @param{Object} newVoting voting to add
     */
    async addNewVotingAction({commit}, newVoting) {
        try {
            const data = await server.addVoting(newVoting)
            await commit("addCurrentSessionVotingMutation", data)
        } catch (err) {
            let allReasons = ''
            if(err.data.errors.length != 0) {
                allReasons = await err.data.errors.join(', ')
            } else {
                allReasons = await err.data.message
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