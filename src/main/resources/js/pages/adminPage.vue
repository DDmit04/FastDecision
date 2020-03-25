<template>
    <div>
        <div v-if="currentUserIsAdmin" id="adminControlPanel">
            <v-text-field id="giveAdminId" color="secondary" v-model="userId" placeholder="user id"/>
            <v-btn id="giveAdminBtn" color="primary" @click="giveAdmin()">give admin</v-btn>

            <v-text-field id="removeAdminId" class="mt-4" color="secondary" :type="'password'" v-model="userId" placeholder="user id"/>
            <v-text-field id="removeAdminPassword" color="secondary" :type="'password'" v-model="adminKey" placeholder="Admin password"/>
            <v-btn id="removeAdminBtn" color="primary" @click="removeAdmin()">remove admin</v-btn>
        </div>
        <div v-else id="askAdminPanel">
            <v-text-field id="askAdminPassword" :type="'password'" color="secondary" v-model="adminKey" placeholder="Admin password"/>
            <v-btn id="askAdminBtn" color="primary" @click="askAdmin()">ask</v-btn>
        </div>
    </div>
</template>

<script>
    import admin from "../api/admin";
    import {mapMutations, mapState} from 'vuex'
    import routesNames from "../router/routesNames";

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
        created() {
            if(this.currentUser == null) {
                this.$router.push({name: routesNames.MAIN})
            }
        },
        methods: {
            ...mapMutations(['refreshCurrentUserRolesMutations']),
            async askAdmin() {
                const data = await admin.getAdmin(this.adminKey)
                this.refreshCurrentUserRolesMutations(data.roles)
            },
            async giveAdmin() {
                const data = await admin.giveAdmin(this.userId)
            },
            async removeAdmin() {
                const data = await admin.removeAdmin(this.userId, this.adminKey)
            }
        }
    }
</script>

<style scoped>

</style>