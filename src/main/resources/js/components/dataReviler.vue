<template>
    <v-card color="primary">
        <div v-if="!dataRiviled">
            <v-btn block class="white--text" color="accent" @click="revilData()">show voting link</v-btn>
        </div>
        <div v-else>
            <v-card-text>
                <v-col color="secondary">
                    <v-row align="center">
                        <v-col cols="10">
                            <v-text-field v-model="dataToRevil" ref="textToCopy"></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-btn color="secondary" @click="copyData()">copy</v-btn>
                        </v-col>
                    </v-row>
                </v-col>
            </v-card-text>
            <v-card-actions>
                <v-btn block class="white--text" color="accent" @click="hideData()">hide voting link</v-btn>
            </v-card-actions>
        </div>
    </v-card>
</template>

<script>
    export default {
        name: "linkReviler",
        props: {
            dataToRevil: {
                required: true,
                type: [String, Number]
            }
        },
        data() {
            return {
                dataRiviled: false
            }
        },
        methods: {
            revilData() {
                this.dataRiviled = true
            },
            hideData() {
                this.dataRiviled = false
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