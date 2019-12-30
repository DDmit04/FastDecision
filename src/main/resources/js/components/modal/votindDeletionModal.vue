<template>
    <v-dialog v-model="active"
              max-width="500"
              min-width="200">
        <v-card>
            <v-card-title class="headline">
                Voting deletion
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text class="headline mt-2">
                Are you sure you want to delete this voting?
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
                <v-btn color="accent" @click="deleteVoting()">Yes</v-btn>
                <v-btn color="danger" @click.stop="active = false">No</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import server from "../../api/server"

    export default {
        props: {
            modalIsActive: {
                required: true,
                type: Boolean
            },
            voting: {
                required: false,
                type: Object
            },
            deletionFunction: {
                required: true,
                type: Function
            },
        },
        name: "votindDeletionModal",
        data() {
            return {}
        },
        computed: {
            active: {
                get() {
                    return this.modalIsActive
                },
                set(value) {
                    if (!value) {
                        this.$emit('close')
                    }
                }
            }
        },
        methods: {
            async deleteVoting() {
                const result = await server.deleteOne(this.voting.id)
                this.active = false
                if(result.ok) {
                    await this.deletionFunction()
                }
            }
        }
    }
</script>

<style scoped>

</style>