<template>
    <div>
        <v-container>
            <v-row>
                <v-layout v-if="currentVoting == null" justify-center>
                    <v-progress-circular
                            id="loadCircle"
                            :size="70"
                            :width="7"
                            color="primary"
                            indeterminate
                    ></v-progress-circular>
                </v-layout>
                <v-col v-else lg="8" sm="12">
                    <v-card id="votingResults"
                            color="primary">
                        <v-card-title class="title">
                            <v-layout id="votingResultsTitle" justify-start>
                                {{currentVoting.votingTitle}}
                            </v-layout>
                            <v-layout id="votingResultsTotalVotes" justify-end>
                                total votes: {{getTotalVotes}}
                            </v-layout>
                        </v-card-title>
                        <v-divider color="secondary"></v-divider>
                        <v-card-text class="ma-3">
                            <div v-for="(option, index) in currentVoting.votingOptions"
                                 :key="index"
                                 :id="'votingResultsOptions' + index"
                                 class="py-2"
                            >
                                <v-row>
                                    <v-col cols="10" class="pa-0">
                                        <v-layout justify-space-between
                                                  class="font-weight-black subtitle-1">
                                            <div :id="'votingResultsDiscription' + index">
                                                {{option.voteDiscription}}
                                            </div>
                                            <div :id="'votingResultsPluses' + index">
                                                {{option.pluses}} votes
                                            </div>
                                        </v-layout>
                                        <v-divider color="secondary"></v-divider>
                                    </v-col>
                                </v-row>
                                <v-row align="center">
                                    <v-col cols="10">
                                        <v-progress-linear :id="'optionPlusesLine' + index"
                                                           height="30" buffer-value="100"
                                                           color="warning"
                                                           :value="calcLine(option.pluses)">
                                        </v-progress-linear>
                                    </v-col>
                                    <v-col :id="'optionPlusesCount' + index" class="subtitle-1">
                                        {{calcLine(option.pluses).toFixed(1)}} %
                                    </v-col>
                                </v-row>
                            </div>
                        </v-card-text>
                    </v-card>
                </v-col>
                <v-col id="votingDonut" v-if="currentVoting != null">
                    <vc-donut :hasLegend="true" :thickness="100" :sections="sections"/>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
    import {addHandler} from '../../utils/websocket'
    import votingMixin from "../../mixins/votingConnectMixin"

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
        name: "votingResults",
        mixins: [votingMixin],
        data() {
            return {
                sections: [],
                currentVoting: this.votingProp,
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
            await this.mixinConnectToVoting(this.votingId, this.votingKey)
            this.currentVoting = this.mixinVoting
        },
        mounted() {
            addHandler(async (data) => {
                let optionId = await this.currentVoting.votingOptions.findIndex(option => option.id == data.id)
                this.currentVoting.votingOptions[optionId].pluses = data.pluses
            })
        },
        computed: {
            calcDonut() {
                let sections = []
                let sectionValue = 0
                let sectionsSum = 0
                let sortedVotingCopy = this.currentVoting.votingOptions.slice().sort((first, sec) => sec.id - first.id)
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
                this.currentVoting.votingOptions.forEach(opt => {
                    votes += opt.pluses
                })
                return votes
            },
        },
        methods: {
            calcLine(optionPluses) {
                let lineValue = optionPluses / this.getTotalVotes * 100
                if (isNaN(lineValue)) {
                    lineValue = 0
                }
                return lineValue
            },
            sortVoteOptions() {
                this.currentVoting.votingOptions.sort((first, sec) => sec.pluses - first.pluses)
            }
        }
    }
</script>

<style scoped>

</style>