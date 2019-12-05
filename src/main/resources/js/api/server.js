import Vue from 'vue'

const server = Vue.resource('/voteApi/votings{/id}')
const server1 = Vue.resource('/voteApi/voteOptions{/id}')

export default {

    addVoting: voting => server.save({}, voting),
    getOne: id => server.get({id: id}),
}