<template>
    <div>
        <v-container>
            <v-layout v-if="currentVoting == null" justify-center>
                <v-progress-circular
                        id="circularProgress"
                        :size="70"
                        :width="7"
                        color="success"
                        indeterminate
                />
            </v-layout>
            <v-col v-else lg="8" sm="12">
                <v-card id="currentVoting" color="primary">
                    <v-card-title id="currentVotingTitle"
                                  class="title"
                    >
                        {{currentVoting.votingTitle}}
                    </v-card-title>
                    <v-divider color="secondary"></v-divider>
                    <v-card-text class="mx-3 mt-3">
                        <div v-for="(option, index) in currentVoting.votingOptions"
                             :key="index"
                             :id="'currentVotingOption' + index"
                             class="py-2"
                        >
                            <v-row align="center">
                                <v-col color="secondary"
                                       :id="'currentVotingOptionDiscription' + index"
                                       class="font-weight-medium"
                                >
                                    {{option.voteDiscription}}
                                </v-col>
                                <v-col>
                                    <v-checkbox
                                            id="selectedOption"
                                            color="success"
                                            class="white--text mr-2 mt-0"
                                            hide-details
                                            :value="index == selectedOptionIndex"
                                            @click="selectedOptionIndex = index"
                                    ></v-checkbox>
                                </v-col>
                            </v-row>
                            <v-divider color="secondary"></v-divider>
                        </div>
                    </v-card-text>
                    <v-card-actions class="pa-3">
                        <v-btn id="doVoteBtn"
                               class="white--text"
                               color="accent"
                               :x-large="true"
                               :disabled="selectedOptionIndex == -1"
                               @click="doVote()"
                        >
                            vote!
                        </v-btn>
                        <v-btn id="goToResultsBtn"
                               class="white--text"
                               color="secondary"
                               :x-large="true"
                               @click="goToResults()">
                            results
                        </v-btn>
                    </v-card-actions>
                </v-card>
                <data-revealer class="my-2" :dataToReveal="votingLink"/>
            </v-col>
        </v-container>
    </div>
</template>

<script>
    import {sendVote} from '../../utils/websocket'
    import routesNames from "../../router/routesNames"
    import votingConnectMixin from "../../mixins/votingConnectMixin"
    import dataRevealer from "../../components/dataRevealer.vue"

    export default {
        props: {
            votingId: {
                required: true,
                type: [String, Number]
            },
            votingKey: {
                required: false,
                type: String,
                default: 'public'
            },
            votingProp: {
                required: false,
                type: Object,
            }
        },
        name: "currentVoting",
        mixins: [votingConnectMixin],
        components: {
            dataRevealer
        },
        data() {
            return {
                currentVoting: this.votingProp,
                selectedOptionIndex: -1,
                modifiedVotingKey: this.votingKey,
            }
        },
        async created() {
            await this.mixinConnectToVoting(this.votingId, this.votingKey)
            this.currentVoting = this.mixinVoting
            this.modifiedVotingKey = this.mixinVotingKey
        },
        computed: {
            votingLink() {
                let origin = window.location.origin
                let path = this.$router.currentRoute.path
                return origin + path + '?votingKey=' + this.modifiedVotingKey
            }
        },
        methods: {
            async doVote() {
                await sendVote(
                    this.currentVoting.votingOptions[this.selectedOptionIndex].id,
                    this.votingId,
                    this.modifiedVotingKey
                )
                await this.$router.push({
                    name: routesNames.VOTING_RESULTS,
                    params: {
                        votingId: this.votingId,
                        votingKey: this.modifiedVotingKey
                    },
                })
            },
            goToResults() {
                this.$router.push({
                    name: routesNames.VOTING_RESULTS,
                    params: {
                        votingId: this.votingId,
                        votingKey: this.modifiedVotingKey
                    },
                }).catch(err => {})
            }
        }
    }
</script>

<style scoped>
</style>