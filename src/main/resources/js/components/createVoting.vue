<template>
    <div>
        <v-container>
            <v-card>
                <v-card-title>
                    <v-text-field v-model="newVote.voteTitle" placeholder="Vote title"/>
                </v-card-title>
                <v-card-text>
                    <v-text-field v-for="(option, index) in newVote.voteOptions" :key="index" v-model="option.voteDiscription"
                                  placeholder="Some option"/>
                    <v-btn @click="addOption">add other option</v-btn>
                    <v-btn @click="addVoting" :disabled="!voteReadyToAdd" :block=true :loading="buttonLoading">add voting</v-btn>
                </v-card-text>
            </v-card>
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
                    if(opt.voteDiscription.trim() != '') {
                        counter++
                    }
                })
                return (this.newVote.voteTitle.trim() != '' && counter >= 2)
            }
        },
        methods: {
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