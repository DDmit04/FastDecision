<template>
    <div>
        <chart-loader v-if="topChartType == 'popular'" :loadFunction="loadPopularVotings"/>
        <chart-loader v-else-if="topChartType == 'newest'" :loadFunction="loadNewestVotings"/>
        <div v-else @load="$router.push({ name: rotesNames.ERROR_PAGE })"></div>
    </div>
</template>

<script>
    import chartLoader from "./chartLoader.vue"
    import votingChart from "../../api/votingChart"
    import rotesNames from "../../router/rotesNames";

    export default {
        props: {
            topChartType: {
                required: true,
                type: String
            },
        },
        name: "publicChart",
        data() {
            return {
                rotesNames: rotesNames
            }
        },
        components: {
            chartLoader
        },
        methods: {
            async loadPopularVotings() {
                const response = await votingChart.getPopular()
                const data = await response.json()
                return data
            },
            async loadNewestVotings() {
                const response = await votingChart.getNewest()
                const data = await response.json()
                return data
            }
        }
    }
</script>

<style scoped>

</style>