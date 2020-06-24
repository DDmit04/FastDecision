export let state = {
    currentUser: currentUser,
    isDarkTheme: false,
    currentStoreVoting: null,
    /**
     * inside each currentSessionVotings: {
     *           votingId: voting.id,
     *          votingKey: voting.votingKey
     *  }
     */
    currentSessionVotings: [],
    version: ''
}