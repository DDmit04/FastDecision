<template>
    <v-app-bar
            color="info"
            app>
        <authModal :modalIsActive="modalIsActive" @close="modalIsActive = false"/>
        <v-toolbar-items>
            <v-btn text @click="goToOtherRote(rotesNames.MAIN)">Fast Decision</v-btn>
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
            <v-btn @click="goToOtherRote(rotesNames.NEWEST_VOTINGS)" text>Newest</v-btn>
            <v-btn @click="goToOtherRote(rotesNames.POPULAR_VOTINGS)" text>Popular</v-btn>
            <v-btn text @click="goToUserVotings()">
                <v-avatar v-if="getCurrentUserPic == null" class="mr-2" color="success" size="35">
                    U
                </v-avatar>
                <v-avatar v-else class="mr-2" size="35">
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
    </v-app-bar>
</template>

<script>
    import {mdiLoginVariant, mdiWhiteBalanceSunny, mdiMoonWaningCrescent} from '@mdi/js'
    import {mapState, mapMutations} from 'vuex'
    import authModal from "./modal/authModal.vue"
    import rotesNames from "../router/rotesNames";

    export default {
        name: "navbar",
        data() {
            return {
                signInIcon: mdiLoginVariant,
                sunIcon: mdiWhiteBalanceSunny,
                moonIcon: mdiMoonWaningCrescent,

                modalIsActive: false,
                rotesNames: rotesNames,
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
            goToUserVotings() {
                if(this.currentUser != null) {
                    this.$router.push({name: this.rotesNames.USER_VOTINGS_CHART, params: {username: this.currentUser.username}})
                } else {
                    this.modalIsActive = true
                }
            }
        }
    }
</script>

<style scoped>

</style>