<template>
    <div>
        <v-container>
            <v-layout v-if="currentVoting == null" justify-center>
                <v-progress-circular
                        :size="70"
                        :width="7"
                        color="success"
                        indeterminate
                ></v-progress-circular>
            </v-layout>
            <v-col v-else lg="8" sm="12">
                <v-card color="primary">
                    <v-card-title class="title">
                        {{currentVoting.voteTitle}}
                    </v-card-title>
                    <v-divider color="secondary"></v-divider>
                    <v-card-text class="mx-3 mt-3">
                        <div class="py-2" v-for="(option, index) in currentVoting.voteOptions" :key="index">
                            <v-row align="center">
                                <v-col color="secondary" class="font-weight-medium">
                                    {{option.voteDiscription}}
                                </v-col>
                                <v-col>
                                    <v-checkbox
                                            color="success"
                                            hide-details
                                            :value="index == selectedOptionIndex"
                                            @click="selectedOptionIndex = index"
                                            class="white--text mr-2 mt-0"
                                    ></v-checkbox>
                                </v-col>
                            </v-row>
                            <v-divider color="secondary"></v-divider>
                        </div>
                    </v-card-text>
                    <v-card-actions class="pa-3">
                        <v-btn class="white--text" color="accent" :x-large="true" :disabled="selectedOptionIndex == -1" @click="doVote()">
                            vote!
                        </v-btn>
                        <v-btn class="white--text" color="secondary" :x-large="true" @click="goToResults()">
                            results
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-container>
    </div>
</template>

<script>
    import api from '../api/server'
    import {connectWebsocket, sendVote, disconnectWebsocket} from '../utils/websocket'

    export default {
        props: ['votingId'],
        name: "currentVoting",
        data() {
            return {
                themeColor: 'black',
                currentVoting: null,
                selectedOptionIndex: -1,
            }
        },
        async created() {
            await this.getCurrentVoting()
            await connectWebsocket(this.votingId)
        },
        destroyed() {
            disconnectWebsocket()
        },
        methods: {
            async doVote() {
                await sendVote(this.currentVoting.voteOptions[this.selectedOptionIndex].id, this.votingId)
                await this.$router.push({name: 'votingResults', params: {votingId: this.votingId}})
            },
            async getCurrentVoting() {
                const response = await api.getOne(this.votingId)
                if (response.body == '' || !response.ok) {
                    await this.$router.push({name: 'pageNotFound'})
                } else {
                    const data = await response.json()
                    this.currentVoting = data
                }
            },
            goToResults() {
                this.$router.push({name: 'votingResults', params: {votingId: this.votingId}})
            }
        }
    }
</script>

<style scoped>
</style>