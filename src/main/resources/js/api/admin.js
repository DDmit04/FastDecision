import Vue from 'vue'

const askAdmin = Vue.resource('/user/getAdmin')
const giveAdmin = Vue.resource('/admin/giveAdmin{/id}')
const removeAdmin = Vue.resource('/admin/removeAdmin{/id}')

export default {

    getAdmin: (id, clientAdminPassword) => askAdmin.save({}, clientAdminPassword),
    giveAdmin: (id) => giveAdmin.save({id: id}),
    removeAdmin: (id, clientAdminPassword) => removeAdmin.save({id: id}, clientAdminPassword)

}