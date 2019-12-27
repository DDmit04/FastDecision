<template>
    <div>
        <v-layout v-if="loading" justify-center>
            <v-progress-circular
                    :size="70"
                    :width="7"
                    color="success"
                    indeterminate
            ></v-progress-circular>
        </v-layout>
        <v-col v-else lg="8" sm="12">
            <v-layout v-if="chart.length == 0" justify-center class="mt-3 display-3 font-italic">
                Nothing Here!
            </v-layout>
            <v-card v-else v-for="voting in chart" color="primary" :key="voting.id" @click="goToVoting(voting.id)">
                <v-card-title class="title">
                    {{voting.voteTitle}}
                    <v-layout justify-end>
                        total votes: {{voting.totalVotes}}
                    </v-layout>
                </v-card-title>
                <v-divider color="secondary"></v-divider>
            </v-card>
        </v-col>
    </div>
</template>

<script>
    import chartApi from '../api/votingChart'

    export default {
        props: ['chartType'],
        name: "abstractChart",
        data() {
            return {
                loading: true,
                chart: []
            }
        },
        async created() {
            let response = null
            if (this.chartType == 'popular') {
                response = await chartApi.getPopular()
            } else if (this.chartType == 'newest') {
                response = await chartApi.getNewest()
            } else {
                await this.$router.push({name: 'main'})
            }
            const data = await response.json()
            this.chart = data
            this.loading = false
        },
        methods: {
            goToVoting(votingId) {
                this.$router.push({name: 'currentVoting', params: {votingId: votingId}})
            }
        }
    }
</script>

<style scoped>

</style>