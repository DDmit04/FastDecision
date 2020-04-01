<template>
    <v-app-bar color="info" app>
        <authModal :modalIsActive="modalIsActive" @close="modalIsActive = false"/>
        <v-toolbar-items>
            <v-btn id="mainPageBtn"
                   text
                   @click="goToOtherRote(routesNames.MAIN)">
                Fast Decision
            </v-btn>
            <v-btn id="switchThemeBtn"
                   text
                   @click="switchTheme()">
                <div v-if="getIsDark">
                    <v-icon>{{sunIcon}}</v-icon>
                </div>
                <div v-else>
                    <v-icon>{{moonIcon}}</v-icon>
                </div>
            </v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <v-text-field id="votingSearch"
                      class="mt-4 mr-4"
                      color="secondary"
                      v-model="search"
                      clearable
                      placeholder="Search by title..."
        />
        <v-btn id="searchBtn"
               class="mr-2"
               outlined
               :disabled="search.length == 0"
               @click="goToSearch()">
            <v-icon>{{searchIcon}}</v-icon>
        </v-btn>
        <v-toolbar-items>
            <v-btn id="newestVotingsBtn"
                   text
                   @click="goToOtherRote(routesNames.NEWEST_VOTINGS)">
                Newest
            </v-btn>
            <v-btn id="popularVotingsBtn"
                   text
                   @click="goToOtherRote(routesNames.POPULAR_VOTINGS)">
                Popular
            </v-btn>
            <v-btn id="userVotingsBtn"
                   text
                   @click="goToUserVotings()">
                <div id="userAvatar">
                    <v-avatar v-if="getCurrentUserPic == null"
                              class="mr-2"
                              color="success"
                              size="35">
                        U
                    </v-avatar>
                    <v-avatar v-else
                              class="mr-2"
                              size="35">
                        <img :src="getCurrentUserPic">
                    </v-avatar>
                </div>
                {{getCurrentUsername | normalizeString(10)}}
            </v-btn>
            <v-btn v-if="currentUser != null"
                   icon
                   href="/logout">
                <v-icon>{{signInIcon}}</v-icon>
            </v-btn>
        </v-toolbar-items>
    </v-app-bar>
</template>

<script>
    import {mdiLoginVariant, mdiMagnify, mdiMoonWaningCrescent, mdiWhiteBalanceSunny} from '@mdi/js'
    import {mapMutations, mapState} from 'vuex'
    import authModal from "./modal/authModal.vue"
    import routesNames from "../router/routesNames";

    /**
     * Component displaying app navbar
     * @displayName Navbar component
     * @example ./../examples/components/navbar.md
     * @author Dmitrochenkov Daniil
     * @version 1.1
     */
    export default {
        name: "navbar",
        data() {
            return {
                signInIcon: mdiLoginVariant,
                sunIcon: mdiWhiteBalanceSunny,
                moonIcon: mdiMoonWaningCrescent,
                searchIcon: mdiMagnify,

                modalIsActive: false,
                routesNames: routesNames,
                search: ''
            }
        },
        components: {
            authModal
        },
        computed: {
            ...mapState(['currentUser', 'isDarkTheme']),
            /**
             * @public
             * Returns current username
             * @return {string|*} checked username
             */
            getCurrentUsername() {
                if (this.currentUser == null) {
                    return 'Unknown'
                } else {
                    return this.currentUser.username
                }
            },
            /**
             * @public
             * Returns current userpic link
             * @return {string|*} checked userpic link
             */
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
            /**
             * @public
             * Switch current theme
             */
            switchTheme() {
                this.changeThemeMutation()
                this.$vuetify.theme.dark = this.getIsDark
            },
            /**
             * @public
             * Redirect to other rote
             * @param roteName{String} new rote name
             */
            goToOtherRote(roteName) {
                this.$router.push({name: roteName}).catch(err => {})
            },
            /**
             * @public
             * If user exists redirect to current user profile
             */
            goToUserVotings() {
                if (this.currentUser != null) {
                    this.$router.push({
                        name: this.routesNames.USER_VOTINGS_CHART,
                        params: {userId: this.currentUser.id}
                    })
                } else {
                    this.modalIsActive = true
                }
            },
            /**
             * @public
             * Redirect to voting search results
             */
            goToSearch() {
                this.$router.push({name: routesNames.SEARCH_RESULTS, params: {stringToSearch: this.search}})
            }
        }
    }
</script>

<style scoped>

</style>