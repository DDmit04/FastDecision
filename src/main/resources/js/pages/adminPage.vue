<template>
    <div>
        <div v-if="currentUserIsAdmin" id="adminControlPanel">
            <v-card color="primary">
                <v-card-title>
                    Give admin
                </v-card-title>
                <v-divider color="secondary"></v-divider>
                <v-card-text>
                    <v-text-field id="giveAdminId"
                                  color="secondary"
                                  v-model="userIdToGiveAdmin"
                                  placeholder="user id"
                    />
                    <v-btn id="giveAdminBtn"
                           color="accent"
                           @click="giveAdmin()"
                    >
                        give admin
                    </v-btn>
                </v-card-text>
            </v-card>

            <v-card color="primary" class="mt-3">
                <v-card-title>
                    Remove admin
                </v-card-title>
                <v-divider color="secondary"></v-divider>
                <v-card-text>
                    <v-text-field id="removeAdminId"
                                  color="secondary"
                                  v-model="userIdToRemoveAdmin"
                                  placeholder="user id"
                    />
                    <v-text-field id="removeAdminPassword"
                                  color="secondary"
                                  type="password"
                                  v-model="adminKey"
                                  placeholder="Admin password"
                    />
                    <v-btn id="removeAdminBtn"
                           color="accent"
                           @click="removeAdmin()"
                    >
                        remove admin
                    </v-btn>
                </v-card-text>
            </v-card>
        </div>
        <div v-else id="askAdminPanel">
            <v-card color="primary">
                <v-card-title>
                    Ask admin
                </v-card-title>
                <v-divider color="secondary"></v-divider>
                <v-card-text>
                    <v-text-field id="askAdminPassword"
                                  type="password"
                                  color="secondary"
                                  v-model="adminKey"
                                  placeholder="Admin password"
                    />
                    <v-btn id="askAdminBtn"
                           color="accent"
                           @click="askAdmin()"
                    >
                        ask
                    </v-btn>
                </v-card-text>
            </v-card>

        </div>
    </div>
</template>

<script>
    import admin from "../api/admin";
    import {mapMutations, mapState} from 'vuex'
    import routesNames from "../router/routesNames";

    /**
     * Page for admin works
     * @displayName Admin page
     * @example ./../examples/pages/adminPage.md
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        name: "adminPage",
        data() {
            return {
                adminKey: '',
                userIdToRemoveAdmin: null,
                userIdToGiveAdmin: null
            }
        },
        computed: {
            ...mapState(['currentUser']),
            /**
             * @public
             * Check current user roles contains admin role
             */
            currentUserIsAdmin() {
                return this.currentUser != null && this.currentUser.roles.includes('ADMIN')
            }
        },
        created() {
            if (this.currentUser == null) {
                this.$router.push({name: routesNames.MAIN})
            }
        },
        methods: {
            ...mapMutations(['refreshCurrentUserRolesMutations']),
            /**
             * @public
             * Send request to server for give admin role to current user and update it's data with response
             */
            async askAdmin() {
                const data = await admin.getAdmin(this.adminKey)
                this.refreshCurrentUserRolesMutations(data.roles)
                this.adminKey = ''
            },
            /**
             * @public
             * Send request to server for give admin role to other user
             */
            async giveAdmin() {
                const data = await admin.giveAdmin(this.userId)
            },
            /**
             * @public
             * Send request to server for remove admin role from other user
             */
            async removeAdmin() {
                const data = await admin.removeAdmin(this.userId, this.adminKey)
            }
        }
    }
</script>

<style scoped>

</style>