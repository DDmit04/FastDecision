<template>
    <div>
        <votindDeletionModal :modalIsActive="modalIsActive"
                             :voting="deletedVoting"
                             :deletionFunction="deleteVoting"
                             @close="modalIsActive = false"/>
        <v-col lg="8" sm="12">
            <v-layout v-if="chartData.length == 0" justify-center class="mt-3 display-3 font-italic">
                Nothing Here!
            </v-layout>
            <v-card v-else v-for="voting in chartData" color="primary" :key="voting.id" @click="goToVoting(voting.id)">
                <v-card-title class="title">
                    <v-layout justify-start>
                        {{voting.votingTitle}}
                    </v-layout>
                    <v-layout justify-center>
                        author:
                        <router-link v-if="voting.owner != null" to="/">
                            <v-btn :to = "{name: 'userChart', params: {userId: voting.owner.id} }" color="accent" class="ml-2">
                                {{voting.owner.username | normalizeUsername}}
                            </v-btn>
                        </router-link>
                        <v-btn v-else color="accent" class="ml-2">
                            Unknown
                        </v-btn>
                    </v-layout>
                    <v-layout justify-end>
                        total votes: {{voting.totalVotes}}
                        <v-btn v-if="voting.owner != null && voting.owner.id == currentUser.id"  @click.stop="callDeleteVoting(voting)"
                               class="ml-2" color="accent">
                            <v-icon>{{closeIcon}}</v-icon>
                        </v-btn>
                    </v-layout>
                </v-card-title>
                <v-divider color="secondary"></v-divider>
            </v-card>
        </v-col>
    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import {mdiClose} from '@mdi/js'
    import votindDeletionModal from "../modal/votindDeletionModal.vue";

    export default {
        props: {
            chartData: {
                required: true,
                type: Array
            },
        },
        name: "abstractChart",
        data() {
            return {
                modalIsActive: false,
                deletedVoting: null,
                closeIcon: mdiClose,
            }
        },
        components: {
            votindDeletionModal
        },
        filters: {
            normalizeUsername(value) {
                let filteredValue = value
                if (value.length > 15) {
                    filteredValue = value.slice(0, 13) + '...'
                }
                return filteredValue
            }
        },
        computed: {
            ...mapState(['currentUser'])
        },
        methods: {
            goToVoting(votingId) {
                this.$router.push({name: 'currentVoting', params: {votingId: votingId}})
            },
            callDeleteVoting(voting) {
                this.deletedVoting = voting
                this.modalIsActive = true
            },
            deleteVoting() {
                if(this.deletedVoting != null) {
                    let deletedVotingIndex = this.chartData.indexOf(this.deletedVoting)
                    this.chartData.splice(deletedVotingIndex, 1)
                }
            },
        }
    }
</script>

<style scoped>

</style>