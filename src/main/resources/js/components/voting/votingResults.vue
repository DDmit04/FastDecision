<template>
    <div>
        <v-container>
            <v-row>
                <v-layout v-if="currentVoting == null" justify-center>
                    <v-progress-circular
                            :size="70"
                            :width="7"
                            color="primary"
                            indeterminate
                    ></v-progress-circular>
                </v-layout>
                <v-col v-else lg="8" sm="12">
                    <v-card color="primary">
                        <v-card-title class="title">
                            {{currentVoting.voteTitle}}
                            <v-layout justify-end>
                                total votes: {{getTotalVotes}}
                            </v-layout>
                        </v-card-title>
                        <v-divider color="secondary"></v-divider>
                        <v-card-text class="ma-3">
                            <div class="py-2" v-for="(option, index) in currentVoting.voteOptions" :key="index">
                                <v-row>
                                    <v-col cols="10" class="pa-0">
                                        <v-layout justify-space-between class="font-weight-black subtitle-1">
                                            <div>
                                                {{option.voteDiscription}}
                                            </div>
                                            <div>
                                                {{option.pluses}} votes
                                            </div>
                                        </v-layout>
                                        <v-divider color="secondary"></v-divider>
                                    </v-col>
                                </v-row>
                                <v-row align="center">
                                    <v-col cols="10">
                                        <v-progress-linear height="30" buffer-value="100"
                                                           color="warning"
                                                           :value="calcLine(option.pluses)">
                                        </v-progress-linear>
                                    </v-col>
                                    <v-col class="subtitle-1">
                                        {{calcLine(option.pluses).toFixed(1)}} %
                                    </v-col>
                                </v-row>
                            </div>
                        </v-card-text>
                    </v-card>
                </v-col>
                <v-col v-if="currentVoting != null">
                    <vc-donut :hasLegend="true" :thickness="100" :sections="sections"/>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
    import api from '../../api/server'
    import {connectWebsocket, addHandler, disconnectWebsocket} from '../../utils/websocket'

    export default {
        props: ['votingId'],
        name: "currentVoting",
        data() {
            return {
                sections: [],
                currentVoting: null,
                totalVotes: 0,
            }
        },
        watch: {
            'currentVoting.totalVotes'(nevVal, oldVal) {
                this.sortVoteOptions()
                this.sections = this.calcDonut
            }
        },
        async created() {
            await connectWebsocket(this.votingId)
            await this.getCurrentVoting()
        },
        mounted() {
            addHandler(async (data) => {
                let optionId = await this.currentVoting.voteOptions.findIndex(option => option.id == data.id)
                this.currentVoting.voteOptions[optionId].pluses = await data.pluses
            })
        },
        destroyed() {
            disconnectWebsocket()
        },
        computed: {
            calcDonut() {
                let sections = []
                let sectionValue = 0
                let sectionsSum = 0
                let sortedVotingCopy = this.currentVoting.voteOptions.slice().sort((first, sec) => sec.id - first.id)
                sortedVotingCopy.forEach(opt => {
                    sectionValue = Math.floor(this.calcLine(opt.pluses))
                    sectionsSum += sectionValue
                    sections.push({label: opt.voteDiscription, value: sectionValue})
                })
                if(sectionsSum < 100) {
                    let maxInd = -1
                    let max = -1
                    sections.forEach((sec, i) => {
                        if(sec.value > max) {
                            maxInd = i
                            max = sec.value
                        }
                    })
                    sections[maxInd].value += (100 - sectionsSum)
                }
                return sections;
            },
            getTotalVotes() {
                let votes = 0
                this.currentVoting.voteOptions.forEach(opt => {
                    votes += opt.pluses
                })
                return votes
            },
        },
        methods: {
            test1(num) {
                this.currentVoting.totalVotes++
                this.currentVoting.voteOptions[num].pluses++
            },
            async getCurrentVoting() {
                const response = await api.getOne(this.votingId)
                if (response.body == '' || !response.ok) {
                    await this.$router.push({name: 'pageNotFound'})
                } else {
                    const data = await response.json()
                    this.currentVoting = await data
                }
            },
            calcLine(optionPluses) {
                let res = optionPluses / this.getTotalVotes * 100
                if (isNaN(res)) {
                    res = 0
                }
                return res
            },
            sortVoteOptions() {
                this.currentVoting.voteOptions.sort((first, sec) => sec.pluses - first.pluses)
            }
        }
    }
</script>

<style scoped>

</style>