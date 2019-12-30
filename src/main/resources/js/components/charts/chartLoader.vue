<template>
    <div class="ma-2">
        <v-layout v-if="dataIsLoading" justify-center>
            <v-progress-circular
                    :size="70"
                    :width="7"
                    color="success"
                    indeterminate
            />
        </v-layout>
        <abstract-chart v-else :chartData="loadedChartData"/>
    </div>
</template>

<script>
    import abstractChart from "./abstractChart.vue"

    export default {
        props: {
            loadFunction: {
                required: true,
                type: Function
            },
            firstArg: {
                required: false,
                type: Number
            },
        },
        name: "chartLoader",
        data() {
            return {
                loadedChartData: [],
                dataIsLoading: true
            }
        },
        components: {
            abstractChart
        },
        async created() {
            const data = await this.loadFunction(this.firstArg)
            this.loadedChartData = data
            this.dataIsLoading = false
        }
    }
</script>

<style scoped>

</style>