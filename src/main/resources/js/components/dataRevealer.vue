<template>
    <v-card color="primary">
        <div v-if="!dataIsReviled"id="hidenData">
            <v-btn id="revealDataBtn"
                   class="white--text"
                   color="accent"
                   block
                   @click="revealData()"
            >
                show voting link
            </v-btn>
        </div>
        <div v-else id="reveledData">
            <v-card-text>
                <v-col color="secondary">
                    <v-row align="center">
                        <v-col cols="10">
                            <v-text-field id="data"
                                          v-model="dataToReveal"
                                          ref="textToCopy"
                            />
                        </v-col>
                        <v-col cols="2">
                            <v-btn id="copyDataBtn"
                                   color="secondary"
                                   @click="copyData()">
                                copy
                            </v-btn>
                        </v-col>
                    </v-row>
                </v-col>
            </v-card-text>
            <v-card-actions>
                <v-btn id="hideDataBtn"
                       class="white--text"
                       color="accent"
                       block
                       @click="hideData()">
                    hide voting link
                </v-btn>
            </v-card-actions>
        </div>
    </v-card>
</template>

<script>
    /**
     * Component to hide/show some data
     * @displayName Data revealer component
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        name: "dataRevealer",
        props: {
            /** Data to hide/show in component */
            dataToReveal: {
                required: true,
                type: [String, Number],
                default: "No data to reveal"
            }
        },
        data() {
            return {
                dataIsReviled: false
            }
        },
        methods: {
            /**
             * @public
             * Show hiden data
             */
            revealData() {
                this.dataIsReviled = true
            },
            /**
             * @public
             * Hide shown data
             */
            hideData() {
                this.dataIsReviled = false
            },
            /**
             * @public
             * Copy shown data to client buffer
             */
            copyData() {
                let textToCopy = this.$refs.textToCopy.$el.querySelector('input')
                textToCopy.select()
                document.execCommand("copy");
            }
        }
    }
</script>

<style scoped>

</style>