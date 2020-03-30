<template>
    <v-dialog v-model="active"
              max-width="500"
              min-width="200">
        <v-card>
            <v-card-title class="headline">
                Authorization
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text>
                <v-row align="center">
                    <v-col class="text-center">
                        <v-btn href='/login/google' class="my-2" color="red white--text" block >
                            <v-icon class="mr-2">{{googleIcon}}</v-icon>
                            Google
                        </v-btn>
                        <v-btn href='/login/github' class="my-2" color="black white--text" block>
                            <v-icon class="mr-2">{{githubIcon}}</v-icon>
                            Github
                        </v-btn>
                    </v-col>
                </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mdiGithubCircle, mdiGoogle} from '@mdi/js'

    /**
     * Component to display user authentication modal dialog
     * @displayName Authentication modal component
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        props: {
            /** Is dialog shown */
            modalIsActive: {
                required: true,
                type: Boolean,
                default: false
            },
        },
        name: "authModal",
        data() {
            return {
                googleIcon: mdiGoogle,
                githubIcon: mdiGithubCircle,
            }
        },
        computed: {
            active: {
                get() {
                    return this.modalIsActive
                },
                set(value) {
                    if (!value) {
                        /** Close dialog event */
                        this.$emit('close')
                    }
                }
            }
        }
    }
</script>

<style scoped>

</style>