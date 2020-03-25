import Vue from 'vue'
import VueResource from 'vue-resource'

Vue.use(VueResource)

export default {

    getAdmin: async (clientAdminPassword) => {
        const response = await Vue.http.post('/user/getAdmin', clientAdminPassword)
        const data = await response.json()
        return data
    },
    giveAdmin: async (id) => {
        const response = await Vue.http.post('/admin/giveAdmin/' + id)
        const data = await response.json()
        return data
    },
    removeAdmin: async (id, clientAdminPassword) => {
        const response = await Vue.http.post('/admin/removeAdmin/' + id, clientAdminPassword)
        const data = await response.json()
        return data
    },

}