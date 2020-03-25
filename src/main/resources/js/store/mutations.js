export let mutations = {
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
}