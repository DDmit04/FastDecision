<template>
    <div>
        <v-container @keyup.enter="tryAddVoting()">
            <v-col lg="8" sm="12">
                <v-card id="createVotingForm" color="primary">
                    <v-card-title>
                        <v-text-field id="newVotingTitle"
                                      v-model="newVoting.votingTitle"
                                      color="secondary"
                                      placeholder="Vote title"
                        />
                    </v-card-title>
                    <v-card-text>
                        <transition-group name="fade">
                            <v-text-field v-for="(option, index) in newVoting.votingOptions"
                                          :key="index"
                                          v-model="option.voteDiscription"
                                          color="secondary"
                                          placeholder="Some option"
                                          :id="'newVotingOption' + index"
                                          @click="checkOptionCount(index)"
                                          @focus="checkOptionCount(index)"
                            />
                        </transition-group>
                        <v-row>
                            <v-checkbox v-model="newVoting.isPrivateVoting"
                                        label="private voting"
                                        class="mx-2"
                                        :disabled="currentUser == null"
                                        id="newIsPrivateVoting"
                                        color="success"
                            />
                            <tooltip class="mt-5"
                                     :tooltipMessage="privateVotingTooltipMessage"
                                     :icon="alertIcon"/>
                        </v-row>
                        <v-row>
                            <v-checkbox v-model="newVoting.isProtectedVoting"
                                        label="protected voting"
                                        class="mx-2"
                                        id="newIsProtectedVoting"
                                        color="success"
                            />
                            <tooltip class="mt-5"
                                     :tooltipMessage="protectedVotingTooltipMessage"
                                     :icon="alertIcon"></tooltip>
                        </v-row>
                    </v-card-text>
                    <v-divider color="secondary"></v-divider>
                    <v-card-actions class="pa-3">
                        <v-btn id="addVotingBtn"
                               class="white--text"
                               color="accent"
                               @click="tryAddVoting()"
                               :disabled="!voteReadyToAdd"
                               :block=true
                               :loading="buttonLoading">
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
    import {mapActions, mapState} from 'vuex'
    import tooltip from "../../components/tooltip.vue"
    import router from "../../router/router";
    import rotesNames from "../../router/routesNames";

    /**
     * Page to create new voting
     * @displayName Create voting page
     * @example ./../../examples/pages/voting/createVoting.md
     * @author Dmitrochenkov Daniil
     * @version 1.1
     */
    export default {
        name: "createVoting",
        data() {
            return {
                alertIcon: mdiAlertCircleOutline,
                privateVotingTooltipMessage: 'Hide from charts and other users (auth users only)',
                protectedVotingTooltipMessage: 'Protect voting by key',
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
                }
            }
        },
        components: {
            tooltip
        },
        created() {
            this.newVoting.votingOptions.push(this.voteOption1)
            this.newVoting.votingOptions.push(this.voteOption2)
        },
        computed: {
            ...mapState(['currentVoting', 'currentUser']),
            /**
             * @public
             * Validate current voting
             * @returns {boolean} new voting is valid
             */
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
            /**
             * @public
             * Call add new voting option method if user select or click last voting option
             * @param {Number} index clicked option ID
             */
            checkOptionCount(index) {
                if (index == this.lastOptionIndex) {
                    this.addOption()
                    this.lastOptionIndex++
                }
            },
            /**
             * @public
             * If current voting is valid call add voting method
             */
            tryAddVoting() {
                if (this.voteReadyToAdd) {
                    this.addVoting()
                }
            },
            /**
             * @public
             * Send add voting request to server
             */
            async addVoting() {
                this.buttonLoading = true
                this.newVoting.votingOptions = this.newVoting.votingOptions.filter(option => option.voteDiscription != '')
                await this.addNewVotingAction(this.newVoting)
                await router.push({
                    name: rotesNames.CURRENT_VOTING,
                    params: {
                        votingId: this.currentVoting.id,
                        votingKey: this.currentVoting.votingKey
                    },
                })
            },
            /**
             * @public
             * Add new voting option
             */
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
    .fade-enter-active, .fade-leave-active {
        transition: opacity .5s;
    }

    .fade-enter, .fade-leave-to {
        opacity: 0;
    }
</style>