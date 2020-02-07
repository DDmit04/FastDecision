import Vue from 'vue'

const votingKeyValidation = Vue.resource('/voteApi/votings{/id}/validation/key')

export default {

    validateKey: (id, votingKey) => votingKeyValidation.get({id: id, votingKey: votingKey}),

}