<template>
    <div>
        <v-container>
            <v-layout v-if="currentVoting == null" justify-center>
                <v-progress-circular
                        :size="70"
                        :width="7"
                        color="green"
                        indeterminate
                ></v-progress-circular>
            </v-layout>
            <v-col v-else lg="8" sm="12">
                <v-card>
                    <v-card-title class="title">
                        {{currentVoting.voteTitle}}
                    </v-card-title>
                    <v-divider></v-divider>
                    <v-card-text class="mx-3 mt-3">
                        <div class="py-2" v-for="(option, index) in currentVoting.voteOptions" :key="index">
                            <v-row align="center">
                                <v-col class="font-weight-medium">
                                    {{option.voteDiscription}}
                                </v-col>
                                <v-col>
                                    <v-checkbox
                                            hide-details
                                            color="orange"
                                            :value="index == selectedOptionIndex"
                                            @click="selectedOptionIndex = index"
                                            class="mr-2 mt-0"
                                    ></v-checkbox>
                                </v-col>
                            </v-row>
                            <v-divider></v-divider>
                        </div>
                    </v-card-text>
                    <v-card-actions class="pa-3">
                        <v-btn color="primary" :x-large="true" :disabled="selectedOptionIndex == -1" @click="doVote()">vote!</v-btn>
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
                currentVoting: null,
                selectedOptionIndex: -1,
            }
        },
        async created() {
            this.getCurrentVoting()
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
                    await this.$router.push({name: 'page404'})
                } else {
                    const data = await response.json()
                    console.log(data)
                    this.currentVoting = data
                }
            },
        }
    }
</script>

<style scoped>

</style>