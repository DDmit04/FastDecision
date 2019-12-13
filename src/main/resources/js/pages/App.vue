<template>
    <v-app :style="{ background: theme}">
        <v-app-bar
                color="info"
                app>
            <v-toolbar-items>
                <v-btn text @click="goToMain()">Fast Decision</v-btn>
                <v-btn text @click="switchTheme()">
                    <div v-if="isDark">
                        <v-icon>{{sunIcon}}</v-icon>
                    </div>
                    <div v-else>
                        <v-icon>{{moonIcon}}</v-icon>
                    </div>
                </v-btn>
            </v-toolbar-items>
            <v-spacer></v-spacer>
            <v-toolbar-items>
                <v-btn text>Newest</v-btn>
                <v-btn text>Popular</v-btn>
            </v-toolbar-items>
        </v-app-bar>
        <v-content>
            <v-container fluid>
                <router-view/>
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
    import {mdiGithubCircle, mdiWhiteBalanceSunny, mdiMoonWaningCrescent} from '@mdi/js'

    export default {
        data() {
            return {
                githubIcon: mdiGithubCircle,
                sunIcon: mdiWhiteBalanceSunny,
                moonIcon: mdiMoonWaningCrescent,
            }
        },
        created() {
            this.$vuetify.theme.dark = false
        },
        computed: {
            isDark() {
                return this.$vuetify.theme.dark
            },
            theme() {
                if (this.$vuetify.theme.dark) {
                    return this.$vuetify.theme.themes.dark.background
                } else {
                    return this.$vuetify.theme.themes.light.background
                }
            }
        },
        methods: {
            switchTheme() {
                this.$vuetify.theme.dark = !this.$vuetify.theme.dark
            },
            goToMain() {
                this.$router.push({name: 'main'})
            }
        }
    }
</script>
<style>
    body {
        max-height: 100%;
        max-width: 100%;
    }
</style>