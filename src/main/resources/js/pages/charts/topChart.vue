<template>
    <div>
        <chart-loader v-if="topChartType == 'popular'"
                      id="popularVotings"
                      :loadFunction="loadPopularVotings"
        />
        <chart-loader v-else-if="topChartType == 'newest'"
                      id="newestVotings"
                      :loadFunction="loadNewestVotings"
        />
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
                //['newest', 'popular']
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
            async loadPopularVotings(page) {
                const data = await votingChart.getPopular(page)
                return data
            },
            async loadNewestVotings(page) {
                const data = await votingChart.getNewest(page)
                return data
            }
        }
    }
</script>

<style scoped>

</style>