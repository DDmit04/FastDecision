<template>
    <div>
        <chart-loader v-if="topChartType == 'popular'" :loadFunction="loadPopularVotings"/>
        <chart-loader v-else-if="topChartType == 'newest'" :loadFunction="loadNewestVotings"/>
        <div v-else @load="$router.push({ name: routesNames.ERROR_PAGE })"></div>
    </div>
</template>

<script>
    import chartLoader from "../../components/charts/chartLoader.vue"
    import votingChart from "../../api/votingChart"
    import routesNames from "../../router/routesNames";

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
                routesNames: routesNames
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