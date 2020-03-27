import Vue from 'vue'

export default {

    validateKey: async (id, votingKey) => {
        const response = await Vue.http.get('/voteApi/votings/' + id + '/validation/key', {params: {votingKey: votingKey}})
        const data = await response.json()
        return data
    },

}