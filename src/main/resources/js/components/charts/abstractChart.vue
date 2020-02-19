<template>
    <div>
        <votingDeletionModal :modalIsActive="modalIsActive"
                             @close="modalIsActive = false"
                             @accept="deleteVoting()"/>
        <v-col lg="8" sm="12">
            <v-layout v-if="votings.length == 0" justify-center class="mt-3 display-3 font-italic">
                Nothing Here!
            </v-layout>
            <v-card v-else v-for="voting in votings" color="primary" :key="voting.id" @click="goToVoting(voting.id)">
                <v-card-title class="title">
                    <v-layout justify-start>
                        {{voting.votingTitle | normalizeString}}
                    </v-layout>
                    <v-layout justify-center>
                        author:
                            <v-btn v-if="voting.owner != null" @click.stop="goToUser(voting.owner.id)" color="accent"
                                   class="ml-2">
                                {{voting.owner.username | normalizeString}}
                            </v-btn>
                        <v-btn v-else color="accent" disabled class="ml-2">
                            Unknown
                        </v-btn>
                    </v-layout>
                    <v-layout justify-end>
                        total votes: {{voting.totalVotes}}
                        <v-btn v-if="canDelete(voting)"
                               @click.stop="callDeleteVoting(voting)"
                               class="ml-4" color="accent">
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
    import votingDeletionModal from "../modal/votingDeletionModal.vue"
    import server from "../../api/server"
    import routesNames from "../../router/routesNames";

    export default {
        props: {
            chartData: {
                required: true,
                type: Object
            },
        },
        name: "abstractChart",
        data() {
            return {
                modalIsActive: false,
                deletedVoting: null,
                votings: this.chartData.content,
                closeIcon: mdiClose,
                currentPage: 1,
            }
        },
        components: {
            votingDeletionModal
        },
        filters: {
            normalizeString(value) {
                let filteredValue = value
                if (value.length >= 13) {
                    filteredValue = value.slice(0, 12) + '...'
                } else {
                    let placeholder = '_'
                    filteredValue += placeholder.repeat(15 - value.length)
                }
                return filteredValue
            },
        },
        computed: {
            ...mapState(['currentUser']),
        },
        methods: {
            goToVoting(votingId) {
                this.$router.push({name: routesNames.CURRENT_VOTING, params: {votingId: votingId}})
            },
            goToUser(votingOwnerId) {
                this.$router.push({name: routesNames.USER_VOTINGS_CHART, params: {userId: votingOwnerId} })
            },
            callDeleteVoting(voting) {
                this.deletedVoting = voting
                this.modalIsActive = true
            },
            canDelete(voting) {
                let userIsOwner = (voting.owner != null && this.currentUser != null) && voting.owner.id == this.currentUser.id
                let currentUserIsAdmin = this.currentUser != null && this.currentUser.roles.includes('ADMIN')
                return userIsOwner || currentUserIsAdmin
            },
            async deleteVoting() {
                if (this.deletedVoting != null) {
                    const result = await server.deleteOne(this.deletedVoting.id)
                    if(result.ok) {
                        let deletedVotingIndex = this.votings.indexOf(this.deletedVoting)
                        this.votings.splice(deletedVotingIndex, 1)
                    }
                }
            },
        }
    }
</script>

<style scoped>

</style>