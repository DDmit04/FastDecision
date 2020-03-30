<template>
    <div>
        <v-layout justify-center
                  class="mt-3 display-3 font-italic"
                  id="errorCode">
            Code: {{errorDisplayCode}}
        </v-layout>
        <v-layout justify-center
                  class="mt-3 display-3 font-italic"
                  id="errorText">
            {{errorDisplayText}}
        </v-layout>
        <v-layout justify-center
                  class="mt-3 display-1 font-italic">
            <router-link :to="{ name: routesNames.MAIN }">Go to main</router-link>
        </v-layout>
    </div>
</template>

<script>
    import routesNames from "../router/routesNames"

    /**
     * Error page foe handle all http errors
     * @displayName Error page
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        name: "errorPage",
        props: {
            /** http error code */
            errorCode: {
                required: false,
                type: [String, Number],
                default: 'unknown'
            },
            /** http error reason */
            errorReason: {
                required: false,
                type: String,
                default: 'this is error page'
            },
        },
        data() {
            return {
                routesNames: routesNames,
                errorDisplayText: this.errorReason,
                errorDisplayCode: this.errorCode
            }
        },
        created() {
            if(this.errorCode == 404) {
                this.errorDisplayText = 'not found!'
            }
        }
    }
</script>

<style scoped>

</style>