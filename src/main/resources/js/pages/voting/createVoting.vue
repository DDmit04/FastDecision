<template>
    <div>
        <v-container @keyup.enter="tryAddVoting()">
            <v-col lg="8" sm="12">
                <v-card color="primary">
                    <v-card-title >
                        <v-text-field color="secondary" v-model="newVoting.votingTitle" placeholder="Vote title"/>
                    </v-card-title>
                    <v-card-text>
                        <v-text-field v-for="(option, index) in newVoting.votingOptions" :key="index"
                                      v-model="option.voteDiscription"
                                      color="secondary"
                                      placeholder="Some option"
                                      @click="checkOptionCount(index)"
                                      @focus="checkOptionCount(index)"
                        />
                        <v-row>
                            <v-checkbox v-model="newVoting.isPrivateVoting" class="mx-2" label="private voting"
                                        color="success"/>
                            <v-tooltip right>
                                <template v-slot:activator="{ on }">
                                    <v-icon class="mt-5" v-on="on">{{alertIcon}}</v-icon>
                                </template>
                                <span>hide from charts and other users</span>
                            </v-tooltip>
                        </v-row>
                        <v-row>
                            <v-checkbox v-model="newVoting.isProtectedVoting" class="mx-2" label="protected voting"
                                        color="success"/>
                            <v-tooltip right>
                                <template v-slot:activator="{ on }">
                                    <v-icon class="mt-5" v-on="on">{{alertIcon}}</v-icon>
                                </template>
                                <span>hide from charts and other users</span>
                            </v-tooltip>
                        </v-row>
                    </v-card-text>
                    <v-divider color="secondary"></v-divider>
                    <v-card-actions class="pa-3">
                        <v-btn class="white--text" color="accent" @click="addVoting" :disabled="!voteReadyToAdd"
                               :block=true :loading="buttonLoading">
                            add voting
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-container>
    </div>
</template>

<script>
    import {mdiAlertCircleOutline} from '@mdi/js'
    import {mapActions} from 'vuex'

    export default {
        name: "createVoting",
        data() {
            return {
                on: false,
                alertIcon: mdiAlertCircleOutline,
                buttonLoading: false,
                lastOptionIndex: 1,
                newVoting: {
                    id: null,
                    votingTitle: '',
                    votingOptions: [],
                    isPrivateVoting: false,
                    isProtectedVoting: false
                },
                voteOption1: {
                    id: null,
                    voteDiscription: '',
                    pluses: 0,
                },
                voteOption2: {
                    id: null,
                    voteDiscription: '',
                    pluses: 0,
                },
            }
        },
        created() {
            this.newVoting.votingOptions.push(this.voteOption1)
            this.newVoting.votingOptions.push(this.voteOption2)
        },
        computed: {
            voteReadyToAdd() {
                let isReady = false
                let counter = 0
                this.newVoting.votingOptions.forEach(opt => {
                    if (opt.voteDiscription.trim() != '') {
                        counter++
                    }
                })
                isReady = this.newVoting.votingTitle.trim() != '' && counter >= 2
                return isReady
            }
        },
        methods: {
            ...mapActions(["addNewVotingAction"]),
            checkOptionCount(index) {
                if (index == this.lastOptionIndex) {
                    this.addOption()
                    this.lastOptionIndex++
                }
            },
            tryAddVoting() {
                if (this.voteReadyToAdd) {
                    this.addVoting()
                }
            },
            async addVoting() {
                this.buttonLoading = true
                this.newVoting.votingOptions = this.newVoting.votingOptions.filter(option => option.voteDiscription != '')
                this.addNewVotingAction(this.newVoting)
            },
            addOption() {
                if (this.newVoting.votingOptions.length < 10) {
                    let newOption = {
                        id: '',
                        voteDiscription: '',
                        pluses: 0,
                    }
                    this.newVoting.votingOptions.push(newOption)
                }
            },
        }
    }
</script>

<style scoped>

</style>