import Vue from 'vue'

const newest = Vue.resource('/voteApi/charts/newest')
const popular = Vue.resource('/voteApi/charts/popular')

export default {

    getNewest: () => newest.get({}),
    getPopular: () => popular.get({}),

}