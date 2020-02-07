<template>
    <div>
        <auth-modal :modalIsActive="modalIsActive"/>
        <v-tabs background-color="info" color="success" v-model="tabs">
            <v-tab>Public votings</v-tab>
            <v-tab v-if="currentUser.id == userId">Private votings</v-tab>
        </v-tabs>
        <v-tabs-items v-model="tabs" :style="{ background: getBackgroundColor}">
            <v-tab-item>
                <chart-loader :load-function="loadUserPublic"
                              :first-arg="userId"
                />
            </v-tab-item>
            <v-tab-item v-if="currentUser.id == userId">
                <chart-loader :load-function="loadUserPrivate"
                              :first-arg="userId"
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
            }
        },
        methods: {
            async loadUserPublic(id) {
                const response = await votingChart.getUserPublic(id)
                const data = await response.json()
                return data
            },
            async loadUserPrivate(id) {
                const response = await votingChart.getUserPrivate(id)
                const data = await response.json()
                return data
            },
        }
    }
</script>

<style scoped>

</style>