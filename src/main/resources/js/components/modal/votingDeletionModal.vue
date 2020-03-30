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
    /**
     * Component to display voting deletion modal dialog
     * @displayName Deletion modal component
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        name: "votindDeletionModal",
        props: {
            /** Is dialog shown */
            modalIsActive: {
                required: true,
                type: Boolean,
                default: false
            },
        },
        computed: {
            active: {
                get() {
                    return this.modalIsActive
                },
                set(value) {
                    if (!value) {
                        /** Close dialog event */
                        this.$emit('close')
                    }
                }
            }
        },
        methods: {
            /**
             * @public
             * Close dialog and calls dialog accept event
             */
            deleteVoting() {
                this.active = false
                /** Accept dialog event */
                this.$emit('accept')
            }
        }
    }
</script>

<style scoped>

</style>