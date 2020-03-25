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
                const data = await votingChart.search(page, this.stringToSearch)
                return data
            },
        }
    }
</script>

<style scoped>

</style>