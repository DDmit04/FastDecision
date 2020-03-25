<template>
    <v-card color="primary">
        <div id="hidenData" v-if="!dataIsReviled">
            <v-btn id="revealDataBtn" block class="white--text" color="accent" @click="revealData()">show voting link</v-btn>
        </div>
        <div v-else id="reveledData">
            <v-card-text>
                <v-col color="secondary">
                    <v-row align="center">
                        <v-col cols="10">
                            <v-text-field id="data" v-model="dataToReveal" ref="textToCopy"></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-btn id="copyDataBtn" color="secondary" @click="copyData()">copy</v-btn>
                        </v-col>
                    </v-row>
                </v-col>
            </v-card-text>
            <v-card-actions>
                <v-btn id="hideDataBtn" block class="white--text" color="accent" @click="hideData()">hide voting link</v-btn>
            </v-card-actions>
        </div>
    </v-card>
</template>

<script>
    export default {
        name: "dataRevealer",
        props: {
            dataToReveal: {
                required: true,
                type: [String, Number]
            }
        },
        data() {
            return {
                dataIsReviled: false
            }
        },
        methods: {
            revealData() {
                this.dataIsReviled = true
            },
            hideData() {
                this.dataIsReviled = false
            },
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