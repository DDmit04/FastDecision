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

    /**
     * Page which download popular or newest votings
     * @displayName Popular or newest voting page
     * @example [none]
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        props: {
            /**
             * Which votings load poular or newest
             * @values newest, popular
             */
            topChartType: {
                required: true,
                type: String,
                default: 'newest'
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
            /**
             * @public
             * Returns page of votings sorted by total votes
             * @param page{Number} number of page to download
             * @returns {Promise<void>} page of votings downloaded from server
             */
            async loadPopularVotings(page) {
                const data = await votingChart.getPopular(page)
                return data
            },
            /**
             * @public
             * Returns page of votings sorted by creation date
             * @param page{Number} number of page to download
             * @returns {Promise<void>} page of votings downloaded from server
             */
            async loadNewestVotings(page) {
                const data = await votingChart.getNewest(page)
                return data
            }
        }
    }
</script>

<style scoped>

</style>