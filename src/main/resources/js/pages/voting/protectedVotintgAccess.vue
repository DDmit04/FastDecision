<template>
    <div>
        <v-layout justify-center
                  class="mt-5">
            <v-card id="votingAccessForm" color="primary">
                <v-card-title id="votingAccessTitle" class="title">
                    This voting is protected! input voting-key or full link to access it!
                </v-card-title>
                <v-divider color="secondary"></v-divider>
                <v-card-text class="ma-3">
                    <v-text-field id="votingAccessKey"
                                  v-model="votingAccessString"
                                  color="secondary"
                                  placeholder="voting key"/>
                </v-card-text>
                <v-card-actions class="pa-3">
                    <v-btn id="accessVotingBtn"
                           class="white--text"
                           color="accent"
                           @click="accessVoting()">
                        Access
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-layout>
    </div>
</template>

<script>
    import routesNames from "../../router/routesNames"

    /**
     * If voting key is wrong this page ask to input new voting key
     * @displayName Wrong voting key page
     * @example ./../../examples/pages/voting/protectedVotintgAccess.md
     * @author Dmitrochenkov Daniil
     * @version 1.1
     */
    export default {
        name: "protectedVointgAccess",
        props: {
            /** ID of accessing voting */
            votingId: {
                required: true,
                type: [String, Number],
                default: 1
            },
        },
        data() {
            return {
                votingAccessString: ""
            }
        },
        methods: {
            /**
             * @public
             * Try access protected voting with inputed key
             */
            accessVoting() {
                if (this.votingAccessString.includes("http")) {
                    let votingKeyUrlParam = 'votingKey='
                    let votingKeyUrlParamIndex = this.votingAccessString.indexOf(votingKeyUrlParam)
                    this.votingAccessString = this.votingAccessString.substr(
                        votingKeyUrlParamIndex + votingKeyUrlParam.length,
                        this.votingAccessString.length
                    )
                }
                this.$router.push({
                    name: routesNames.CURRENT_VOTING,
                    params: {
                        votingId: this.votingId,
                    },
                    query: {
                        votingKey: this.votingAccessString
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>