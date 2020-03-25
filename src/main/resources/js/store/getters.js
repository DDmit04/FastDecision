export let getters = {
    getVotingById(state) {
        return (id) => {
            let result = state.currentSessionVotings.find(voting => voting.votingId == id)
            return result;
        }
    },
}