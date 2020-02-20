<template>
    <div>
        <chart-loader :loadFunction="loadSearchedVotings"/>
    </div>
</template>

<script>
    import chartLoader from "../../components/charts/chartLoader.vue";
    import votingChart from "../../api/votingChart";

    export default {
        props: {
            stringToSearch: {
                required: true,
                type: String
            }
        },
        name: "searchChart",
        components: {
            chartLoader
        },
        methods: {
            async loadSearchedVotings(page) {
                const response = await votingChart.search(page, this.stringToSearch)
                const data = await response.json()
                return data
            },
        }
    }
</script>

<style scoped>

</style>