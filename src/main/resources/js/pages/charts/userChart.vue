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
            <v-tab v-if="userIsCurrentUser" id="privateVotingsTab">
                Private votings
            </v-tab>
        </v-tabs>
        <v-tabs-items v-model="tabs" :style="{ background: getBackgroundColor}">
            <v-tab-item>
                <chart-loader :load-function="loadUserPublic"
                              :userId="userId"
                />
            </v-tab-item>
            <v-tab-item v-if="userIsCurrentUser">
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
            userIsCurrentUser() {
                return this.currentUser != null && this.currentUser.id == this.userId
            }
        },
        methods: {
            async loadUserPublic(id, page) {
                const data = await votingChart.getUserPublic(id, page)
                return data
            },
            async loadUserPrivate(id, page) {
                if(this.userIsCurrentUser) {
                    const data = await votingChart.getUserPrivate(id, page)
                    return data
                }
            },
        }
    }
</script>

<style scoped>

</style>