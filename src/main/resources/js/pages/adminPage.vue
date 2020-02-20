<template>
    <div>
        <div v-if="currentUserIsAdmin">
            <v-text-field color="secondary" v-model="userId" placeholder="user id"/>
            <v-btn color="primary" @click="giveAdmin()">give admin</v-btn>

            <v-text-field color="secondary" v-model="userId" placeholder="user id"/>
            <v-text-field color="secondary" v-model="adminKey" placeholder="Admin password"/>
            <v-btn color="primary" @click="removeAdmin()">remove admin</v-btn>
        </div>
        <div v-else-if="currentUser == null">
            Authorize!
        </div>
        <div v-else>
            <v-text-field color="secondary" v-model="adminKey" placeholder="Admin password"/>
            <v-btn color="primary" @click="askAdmin()">ask</v-btn>
        </div>
    </div>
</template>

<script>
    import admin from "../api/admin";
    import {mapState} from 'vuex'

    export default {
        name: "adminPage",
        data() {
            return {
                adminKey: '',
                userId: null
            }
        },
        computed: {
            ...mapState(['currentUser']),
            currentUserIsAdmin() {
                return this.currentUser != null && this.currentUser.roles.includes('ADMIN')
            }
        },
        methods: {
            async askAdmin() {
                const response = await admin.getAdmin(this.currentUser.id, this.adminKey)
                const data = await response.json()
            },
            async giveAdmin() {
                const response = await admin.giveAdmin(this.userId)
                const data = await response.json()
            },
            async removeAdmin() {
                const response = await admin.removeAdmin(this.userId, this.adminKey)
                const data = await response.json()
            }
        }
    }
</script>

<style scoped>

</style>