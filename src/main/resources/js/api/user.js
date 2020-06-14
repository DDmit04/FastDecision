import Vue from 'vue'
import VueResource from 'vue-resource'

Vue.use(VueResource)

export default {

    getUserData: async (id) => {
        const response = await Vue.http.get('/user/get/' + id)
        const data = await response.json()
        return data
    },

}