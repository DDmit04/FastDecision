<template>
    <div>
        <v-layout justify-center class="mt-3 display-3 font-italic">
            Code: {{errorDisplayCode}}
        </v-layout>
        <v-layout justify-center class="mt-3 display-3 font-italic">
            {{errorDisplayText}}
        </v-layout>
        <v-layout justify-center class="mt-3 display-1 font-italic">
            <router-link :to="{ name: routesNames.MAIN }">Go to main</router-link>
        </v-layout>
    </div>
</template>

<script>
    import routesNames from "../router/routesNames"

    export default {
        name: "errorPage",
        props: {
            errorCode: {
                required: false,
                type: [String, Number]
            },
            errorReason: {
                required: false,
                type: String
            },
        },
        data() {
            return {
                routesNames: routesNames,
                errorDisplayText: this.errorReason || 'this is error page',
                errorDisplayCode: this.errorCode || 'unknown'
            }
        },
        created() {
            if(this.errorCode == 404) {
                this.errorDisplayText = 'Not found!'
            }
        }
    }
</script>

<style scoped>

</style>