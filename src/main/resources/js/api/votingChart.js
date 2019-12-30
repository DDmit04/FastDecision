import Vue from 'vue'

const newestResource = Vue.resource('/voteApi/charts/newest')
const popularResource = Vue.resource('/voteApi/charts/popular')
const userPublicResource = Vue.resource('/voteApi/charts/userPublic{/id}')
const userPrivateResource = Vue.resource('/voteApi/charts/userPrivate{/id}')


export default {

    getNewest: () => newestResource.get({}),
    getPopular: () => popularResource.get({}),
    getUserPublic : (id) => userPublicResource.get({id: id}),
    getUserPrivate : (id) => userPrivateResource.get({id: id}),

}