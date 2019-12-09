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
                        <v-layout justify-end>
                            total votes: {{getTotalVotes}}
                        </v-layout>
                    </v-card-title>
                    <v-divider></v-divider>
                    <v-card-text class="ma-3">
                        <div class="py-2" v-for="(option, index) in currentVoting.voteOptions" :key="index">
                            <v-row align="center">
                                <v-col cols="10" class="subtitle-1 pa-0">
                                    <v-layout justify-space-between class="font-weight-medium subtitle-1">
                                        <div>
                                            {{option.voteDiscription}}
                                        </div>
                                        <div>
                                            {{option.pluses}} votes
                                        </div>
                                    </v-layout>
                                    <v-divider></v-divider>
                                </v-col>
                            </v-row>
                            <v-row align="center">
                                <v-col cols="10">
                                    <v-progress-linear height="30" buffer-value="100" :value="calcLine(option.pluses)">
                                    </v-progress-linear>
                                </v-col>
                                <v-col class="subtitle-1">
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
    import {connectWebsocket, addHandler, disconnectWebsocket} from '../utils/websocket'

    export default {
        props: ['votingId'],
        name: "currentVoting",
        data() {
            return {
                currentVoting: null,
                totalVotes: 0,
            }
        },
        created() {
            this.getCurrentVoting()
            connectWebsocket(this.votingId)
        },
        mounted() {
            addHandler((data) => {
                let optionId = this.currentVoting.voteOptions.findIndex(option => option.id == data.id)
                this.currentVoting.voteOptions[optionId].pluses = data.pluses
            })
        },
        destroyed() {
            disconnectWebsocket()
        },
        computed: {
            getTotalVotes() {
                let votes = 0
                this.currentVoting.voteOptions.forEach(opt => {
                    votes += opt.pluses
                })
                return votes
            },
            getPlusesCount() {
                let allPluses = 0
                this.currentVoting.voteOptions.forEach(opt => {
                    allPluses += opt.pluses
                })
                return allPluses
            },
        },
        methods: {
            async getCurrentVoting() {
                const response = await api.getOne(this.votingId)
                if (response.body == '' || !response.ok) {
                    await this.$router.push({name:'page404'})
                } else {
                    const data = await response.json()
                    this.currentVoting = data
                }
            },
            calcLine(optionPluses) {
                let res = optionPluses / this.getTotalVotes * 100
                if (isNaN(res)) {
                    res = 0
                }
                return res
            },
        }
    }
</script>

<style scoped>

</style>