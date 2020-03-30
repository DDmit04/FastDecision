<template>
    <div>
        <auth-modal :modalIsActive="modalIsActive"/>
        <v-tabs id="userChartsTabs"
                v-model="tabs"
                background-color="info"
                color="success">
            <v-tab id="publicVotingsTab">
                Public votings
            </v-tab>
            <v-tab v-if="userIsCurrentUserOrAdmin" id="privateVotingsTab">
                Private votings
            </v-tab>
        </v-tabs>
        <v-tabs-items v-model="tabs" :style="{ background: getBackgroundColor}">
            <v-tab-item>
                <chart-loader :load-function="loadUserPublic"
                              :userId="userId"
                />
            </v-tab-item>
            <v-tab-item v-if="userIsCurrentUserOrAdmin">
                <chart-loader :load-function="loadUserPrivate"
                              :userId="userId"
                />
            </v-tab-item>
        </v-tabs-items>
    </div>
</template>

<script>
    import votingChart from "../../api/votingChart";
    import {mapState} from 'vuex'
    import authModal from "../../components/modal/authModal.vue";
    import chartLoader from "../../components/charts/chartLoader.vue"

    /**
     * Page which download user votings
     * @displayName User voting page
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        props: {
            userId: {
                required: true,
                type: [Number, String]
            },
        },
        name: "userChart",
        data() {
            return {
                tabs: null,
                modalIsActive: false,
            }
        },
        components: {
            authModal,
            chartLoader
        },
        async created() {
            if (this.currentUser == null) {
                this.modalIsActive = true
            }
        },
        computed: {
            ...mapState(['currentUser', 'isDarkTheme']),
            getBackgroundColor() {
                if (this.isDarkTheme) {
                    return this.$vuetify.theme.themes.dark.background
                } else {
                    return this.$vuetify.theme.themes.light.background
                }
            },
            /**
             * @public
             * Returns is current user can access user private votings
             * @return {boolean} is current user is user or admin
             */
            userIsCurrentUserOrAdmin() {
                return this.currentUser != null &&
                    (this.currentUser.id == this.userId || this.currentUser.roles.includes("ADMIN"))
            }
        },
        methods: {
            /**
             * @public
             * Returns page of searched votings
             * @param{Number} id user ID which votings fetch
             * @param page{Number} number of page to download
             * @returns {Promise<void>} page of votings downloaded from server
             */
            async loadUserPublic(id, page) {
                const data = await votingChart.getUserPublic(id, page)
                return data
            },
            /**
             * @public
             * Returns page of searched votings
             * @param{Number} id user ID which votings fetch
             * @param page{Number} number of page to download
             * @returns {Promise<void>} page of votings downloaded from server
             */
            async loadUserPrivate(id, page) {
                if(this.userIsCurrentUserOrAdmin) {
                    const data = await votingChart.getUserPrivate(id, page)
                    return data
                }
            },
        }
    }
</script>

<style scoped>

</style>