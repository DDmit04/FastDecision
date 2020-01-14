<template>
    <div>
        <v-layout justify-center class="mt-3 display-3 font-italic">
            {{errorText}}
        </v-layout>
        <v-layout justify-center class="mt-3 display-1 font-italic">
            <router-link :to="{ name: rotesNames.MAIN }">Go to main</router-link>
        </v-layout>
    </div>
</template>

<script>
    import rotesNames from "../router/rotesNames"

    export default {
        name: "errorPage",
        props: {
            errorCode: {
                required: false,
                type: [String, Number]
            }
        },
        data() {
            return {
                rotesNames: rotesNames,
                errorText: 'Something go wrong :(',
            }
        },
        created() {
            if (this.errorCode == 404) {
                this.errorText = 'Not found error!'
            }
        }
    }
</script>

<style scoped>

</style>