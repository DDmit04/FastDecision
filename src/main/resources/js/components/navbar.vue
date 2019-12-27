<template>
    <v-app-bar
            color="info"
            app>
        <v-toolbar-items>
            <v-btn text @click="goToOtherRote(MAIN_ROTE_NAME)">Fast Decision</v-btn>
            <v-btn text @click="switchTheme()">
                <div v-if="getIsDark">
                    <v-icon>{{sunIcon}}</v-icon>
                </div>
                <div v-else>
                    <v-icon>{{moonIcon}}</v-icon>
                </div>
            </v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn @click="goToOtherRote(NEWEST_VOTINGS_ROTE_NAME)" text>Newest</v-btn>
            <v-btn @click="goToOtherRote(POPULAR_VOTINGS_ROTE_NAME)" text>Popular</v-btn>
            <v-btn text>
                <v-avatar v-if="getCurrentUserPic == null" class="mr-2" color="success" size="35">
                    U
                </v-avatar>
                <v-avatar v-else class="mr-2">
                    <img :src="getCurrentUserPic">
                </v-avatar>
                {{getCurrentUsername}}
            </v-btn>
            <v-btn v-if="currentUser == null" @click.stop="modalIsActive = true" text>
                <v-icon>{{signInIcon}}</v-icon>
            </v-btn>
            <v-btn v-else icon href="/logout">
                <v-icon>{{signInIcon}}</v-icon>
            </v-btn>
        </v-toolbar-items>
        <authModal :modalIsActive="modalIsActive"/>
    </v-app-bar>
</template>

<script>
    import {mdiLoginVariant, mdiWhiteBalanceSunny, mdiMoonWaningCrescent} from '@mdi/js'
    import {mapState, mapMutations} from 'vuex'
    import authModal from "./authModal.vue";

    export default {
        name: "navbar",
        data() {
            return {
                signInIcon: mdiLoginVariant,
                sunIcon: mdiWhiteBalanceSunny,
                moonIcon: mdiMoonWaningCrescent,

                modalIsActive: false,

                MAIN_ROTE_NAME: 'main',
                NEWEST_VOTINGS_ROTE_NAME: 'newestVotings',
                POPULAR_VOTINGS_ROTE_NAME: 'popularVotings'
            }
        },
        components: {
            authModal
        },
        computed: {
            ...mapState(['currentUser', 'isDarkTheme']),
            getCurrentUsername() {
                if (this.currentUser == null) {
                    return 'Unknown'
                } else {
                    return this.currentUser.username
                }
            },
            getCurrentUserPic() {
                if (this.currentUser == null) {
                    return null
                } else {
                    return this.currentUser.userPic
                }
            },
            getIsDark() {
                return this.isDarkTheme
            }
        },
        methods: {
            ...mapMutations(['changeThemeMutation']),
            switchTheme() {
                this.changeThemeMutation()
                this.$vuetify.theme.dark = this.getIsDark
            },
            goToOtherRote(roteName) {
                this.$router.push({name: roteName})
            },
        }
    }
</script>

<style scoped>

</style>