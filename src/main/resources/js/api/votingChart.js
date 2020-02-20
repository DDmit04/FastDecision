import Vue from 'vue'

export default {

    getNewest: (page) => Vue.http.get('/voteApi/charts/newest', {params:  {page: page}} ),
    getPopular: (page) => Vue.http.get('/voteApi/charts/popular', {params:  {page: page}} ),
    getUserPublic : (page, id) => Vue.http.get('/voteApi/charts/userPublic/' + id, {params:  {page: page}} ),
    getUserPrivate : (page, id) => Vue.http.get('/voteApi/charts/userPrivate/' + id, {params:  {page: page}} ),
    search: (page, search) => Vue.http.get('/voteApi/charts/search', {params:  {page: page, search: search}} ),

}