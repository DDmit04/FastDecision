export let state = {
    currentUser: currentUser,
        isDarkTheme: false,
        currentVoting: null,
    /**
     * inside each currentSessionVotings: {
     *           votingId: voting.id,
     *          votingKey: voting.votingKey
     *  }
     */
        currentSessionVotings: [],
        version: ''
}