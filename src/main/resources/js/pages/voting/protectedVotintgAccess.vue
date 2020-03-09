<template>
    <div>
        <v-layout justify-center class="mt-5">
            <v-card color="primary">
                <v-card-title class="title">
                    This voting is protected! input voting-key to access it!
                </v-card-title>
                <v-divider color="secondary"></v-divider>
                <v-card-text class="ma-3">
                    <v-text-field v-model="votingKey" color="secondary" placeholder="voting key"/>
                </v-card-text>
                <v-card-actions class="pa-3">
                    <v-btn @click="accessVoting()" class="white--text" color="accent">
                        Access
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-layout>
    </div>
</template>

<script>
    import routesNames from "../../router/routesNames"

    export default {
        name: "protectedVointgAccess",
        props: {
            votingId: {
                required: true,
                type: [String, Number]
            },
        },
        data() {
            return {
                votingKey: ""
            }
        },
        methods: {
            accessVoting() {
                this.$router.push({
                    name: routesNames.CURRENT_VOTING,
                    params: {
                        votingId: this.votingId,
                    },
                    query: {
                        votingKey: this.votingKey
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>