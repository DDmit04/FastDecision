import Vue from 'vue'

export default {

    getNewest: async (page) => {
        const response = await Vue.http.get('/voteApi/charts/newest', {params:  {page: page}} )
        const data = await response.json()
        return data
    },
    getPopular: async (page) => {
        const response = await Vue.http.get('/voteApi/charts/popular', {params:  {page: page}} )
        const data = await response.json()
        return data
    },
    getUserPublic : async (page, id) => {
        const response = await Vue.http.get('/voteApi/charts/userPublic/' + id, {params:  {page: page}} )
        const data = await response.json()
        return data
    },
    getUserPrivate : async (page, id) => {
        const response = await Vue.http.get('/voteApi/charts/userPrivate/' + id, {params:  {page: page}} )
        const data = await response.json()
        return data
    },
    search: async (page, search) => {
        const response = await Vue.http.get('/voteApi/charts/search', {params:  {page: page, search: search}} )
        const data = await response.json()
        return data
    },

}