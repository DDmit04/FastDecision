import Vue from 'vue'

const server = Vue.resource('/voteApi/votings{/id}')

export default {

    addVoting: voting => server.save({}, voting),
    getOne: id => server.get({id}),
    deleteOne : id => server.remove({id})
}