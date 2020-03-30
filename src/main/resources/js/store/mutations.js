/**
 * @displayName Vuex mutations
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
export let mutations = {
    /**
     * @public
     * Transfer data from local storage to store
     * @param{String|Number} version current app version
     */
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
    /**
     * @public
     * Update current user roles
     * @param{[]} newRoles new user roles
     */
    refreshCurrentUserRolesMutations(state, newRoles) {
        state.currentUser.roles = newRoles
    },
    /**
     * @public
     * Invert current theme
     */
    changeThemeMutation(state) {
        state.isDarkTheme = !state.isDarkTheme
    },
    /**
     * @public
     * Create new voting session with voting ID and voting key
     * @param{Object} voting voting to create new voting session
     */
    addCurrentSessionVotingMutation(state, voting) {
        state.currentVoting = voting
        let votingId = voting.votingId || voting.id
        let votingKey = voting.votingKey || 'public'
        state.currentSessionVotings.push({
            votingId: votingId,
            votingKey: votingKey,
        })
    },
    /**
     * Delete voting from store
     * @param{Number} votingId deleted voting ID
     */
    deleteInvalidVotingSessionMutation(state, votingId) {
        let voting = state.currentSessionVotings.find(voting => voting.votingId == votingId)
        let index = state.currentSessionVotings.indexOf(voting)
        state.currentSessionVotings.splice(index, 1)
    }
}