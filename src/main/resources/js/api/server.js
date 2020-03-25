import Vue from 'vue'
import VueResource from 'vue-resource'

Vue.use(VueResource)

export default {

    addVoting: async (voting) => {
        const response = await Vue.http.post('/voteApi/votings/', voting)
        const data = await response.json()
        return data
    },
    getOne: async (id, votingKey) => {
        const response = await Vue.http.get('/voteApi/votings/' + id, {params:  {votingKey: votingKey}})
        const data = await response.json()
        return data
    },
    deleteOne: async (id) => {
        const response = await Vue.http.delete('/voteApi/votings/' + id)
        return response
    }

}