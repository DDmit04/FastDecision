<template>
    <v-app :style="{ background: getBackgroundColor}">
        <navbar/>
        <v-content>
            <v-container fluid>
                <router-view :key="$route.path + $route.params.votingKey"/>
            </v-container>
        </v-content>
        <v-footer class="justify-center pl-0"
                  color="info"
                  inset
                  app>
            <v-btn href="https://github.com/DDmit04/FastDecision" class="white--text" text>
                <v-icon>{{githubIcon}}</v-icon>
            </v-btn>
        </v-footer>
    </v-app>
</template>

<script>
    import {mdiGithubCircle} from '@mdi/js'
    import {mapState} from 'vuex'
    import navbar from "../components/navbar.vue";

    /**
     * App entry point
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        data() {
            return {
                githubIcon: mdiGithubCircle,
            }
        },
        components: {
            navbar
        },
        created() {
            this.$vuetify.theme.dark = this.isDarkTheme
        },
        computed: {
            ...mapState(['isDarkTheme']),
            /**
             * @public
             * returns vuetify background color from vuetify plugin settings
             * @returns {*} vuetify background color
             */
            getBackgroundColor() {
                if (this.isDarkTheme) {
                    return this.$vuetify.theme.themes.dark.background
                } else {
                    return this.$vuetify.theme.themes.light.background
                }
            }
        },
    }
</script>
<style>
    body {
        max-height: 110%;
        max-width: 100%;
    }
</style>