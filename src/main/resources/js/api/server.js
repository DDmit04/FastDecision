import Vue from 'vue'

const server = Vue.resource('/voteApi/votings{/id}')

export default {

    addVoting: voting => server.save({}, voting),
    getOne: (id, votingKey) => server.get({id: id, votingKey: votingKey}),
    deleteOne: id => server.remove({id}),

}