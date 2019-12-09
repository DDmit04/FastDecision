<template>
    <div>
        <v-container>
            <v-col lg="8" sm="12">
                <v-card>
                    <v-card-title>
                        <v-text-field v-model="newVote.voteTitle" placeholder="Vote title"/>
                    </v-card-title>
                    <v-card-text>
                        <v-text-field v-for="(option, index) in newVote.voteOptions" :key="index"
                                      v-model="option.voteDiscription"
                                      placeholder="Some option"
                                      @click="checkOptionCount(index)"/>
                    </v-card-text>
                    <v-card-actions class="pa-3">
                        <v-btn color="primary" @click="addVoting" :disabled="!voteReadyToAdd" :block=true :loading="buttonLoading">
                            add voting
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-container>
    </div>
</template>

<script>
    import api from '../api/server'

    export default {
        name: "createVoting",
        data() {
            return {
                buttonLoading: false,
                lastOptionIndex: 1,
                newVote: {
                    id: null,
                    voted: false,
                    ended: false,
                    voteTitle: '',
                    voteOptions: []
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
            this.newVote.voteOptions.push(this.voteOption1)
            this.newVote.voteOptions.push(this.voteOption2)
        },
        computed: {
            voteReadyToAdd() {
                let counter = 0
                this.newVote.voteOptions.forEach(opt => {
                    if (opt.voteDiscription.trim() != '') {
                        counter++
                    }
                })
                return (this.newVote.voteTitle.trim() != '' && counter >= 2)
            }
        },
        methods: {
            checkOptionCount(index) {
                if (index == this.lastOptionIndex) {
                    this.addOption()
                    this.lastOptionIndex++
                }
            },
            async addVoting() {
                this.buttonLoading = true
                this.newVote.voteOptions = this.newVote.voteOptions.filter(option => option.voteDiscription != '')
                const response = await api.addVoting(this.newVote)
                const data = await response.json()
                await this.$router.push({name: 'currentVoting', params: {votingId: data.id}})
            },
            addOption() {
                let newOption = {
                    id: '',
                    voteDiscription: '',
                    pluses: 0,
                }
                this.newVote.voteOptions.push(newOption)
            },
        }
    }
</script>

<style scoped>

</style>