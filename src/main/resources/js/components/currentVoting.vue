<template>
    <div>
        <v-container>
            <h3 class="display-2 pl-2 py-2">Current voting:</h3>
            <v-layout v-if="currentVoting == null" justify-center>
                <v-progress-circular
                        :size="70"
                        :width="7"
                        color="green"
                        indeterminate
                ></v-progress-circular>
            </v-layout>
            <v-col v-else cols="7" >
                <v-card class="pa-3 ma-3" tile>
                    <v-card-title class="display-2">
                        {{currentVoting.voteTitle}}
                    </v-card-title>
                    <v-card-text>
                        <div v-for="(option, index) in currentVoting.voteOptions" :key="index">
                            <v-row align="center">
                                <v-col cols="11">
                                    <v-text-field
                                            class="headline"
                                            v-model="option.voteDiscription"
                                            placeholder="Some option"
                                            @click="doVote(currentVoting, option)" readonly>
                                    </v-text-field>
                                </v-col>
                                <v-col class="subtitle-2">
                                    {{option.pluses}} votes
                                </v-col>
                                <!--                                <v-checkbox-->
                                <!--                                        hide-details-->
                                <!--                                        class="shrink mr-2 mt-0"-->
                                <!--                                ></v-checkbox>-->
                            </v-row>
                            <v-row align="center">
                                <v-col cols="11">
                                    <v-progress-linear buffer-value="100" :value="calcLine(option.pluses)"
                                                       height="30">
                                    </v-progress-linear>
                                </v-col>
                                <v-col class="subtitle-2">
                                        {{calcLine(option.pluses)}} %
                                </v-col>
                            </v-row>
                        </div>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-container>
    </div>
</template>

<script>
    import api from '../api/server'
    import SockJS from 'sockjs-client'
    import Stomp from 'webstomp-client'

    export default {
        props: ['votingId'],
        name: "currentVoting",
        data() {
            return {
                currentVoting: null,
                userIdentifier: 0,
                idCounter: 0
            }
        },
        async mounted() {
            await this.getCurrentVoting()
            await this.currentVoting.voteOptions.forEach(opt => {
                this.totalVotes += opt.pluses
            })
            await this.connectWebsocket()
        },
        destroyed() {
            this.disconnectWebsocket()
        },
        methods: {
            doVote(vote, option) {
                this.sendVote(option.id)
            },
            onWebsocketMessage(tick) {
                let data = JSON.parse(tick.body)
                let optionId = this.currentVoting.voteOptions.findIndex(option => option.id == data.id)
                this.currentVoting.voteOptions[optionId].pluses = data.pluses
                this.currentVoting.voteOptions[optionId].votedIps = data.votedIps
            },
            async getCurrentVoting() {
                const response = await api.getOne(this.votingId)
                const data = await response.json()
                this.currentVoting = data
                this.currentVoting.votedUsersIdentifiers = []
            },
            calcLine(optionPluses) {
                let res = optionPluses / this.getPlusesCount() * 100
                if (isNaN(res)) {
                    res = 0
                }
                return res
            },
            getPlusesCount() {
                let allPluses = 0
                this.currentVoting.voteOptions.forEach(opt => {
                    allPluses += opt.pluses
                })
                return allPluses
            },
            sendVote(optionId) {
                if (this.stompClient && this.stompClient.connected) {
                    this.stompClient.send('/app/voting-websocket/' + this.votingId, optionId, {})
                }
            },
            connectWebsocket() {
                this.socket = new SockJS('/voting-websocket')
                this.stompClient = Stomp.over(this.socket)
                this.stompClient.connect({}, (frame) => {
                    this.connected = true
                    this.stompClient.subscribe('/topic/voting/' + this.votingId, this.onWebsocketMessage)
                }, (error) => {
                    console.log(error)
                    this.connected = false
                })
            },
            disconnectWebsocket() {
                if (this.stompClient && this.stompClient.connected) {
                    this.stompClient.disconnect()
                    this.connected = false
                }
            },
            tickleConnection() {
                this.connected ? this.disconnectWebsocket() : this.connectWebsocket()
            },
        }
    }
</script>

<style scoped>

</style>