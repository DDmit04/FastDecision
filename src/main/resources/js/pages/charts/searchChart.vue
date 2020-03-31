<template>
    <div>
        <chart-loader :loadFunction="loadSearchedVotings"/>
    </div>
</template>

<script>
    import chartLoader from "../../components/charts/chartLoader.vue";
    import votingChart from "../../api/votingChart";

    /**
     * Page which download voting search result
     * @displayName Searched voting page
     * @example [none]
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        props: {
            /** String to search */
            stringToSearch: {
                required: true,
                type: String,
                default: "serach"
            }
        },
        name: "searchChart",
        components: {
            chartLoader
        },
        methods: {
            /**
             * @public
             * Returns page of searched votings
             * @param page{Number} number of page to download
             * @returns {Promise<void>} page of votings downloaded from server
             */
            async loadSearchedVotings(page) {
                const data = await votingChart.search(page, this.stringToSearch)
                return data
            },
        }
    }
</script>

<style scoped>

</style>