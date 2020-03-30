/**
 * @displayName Vuex getters
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
export let getters = {
    /**
     * @public
     * Returns current voting session by voting ID
     * @return {Object} voting session
     */
    getVotingById(state) {
        return (id) => {
            let result = state.currentSessionVotings.find(voting => voting.votingId == id)
            return result;
        }
    },
}