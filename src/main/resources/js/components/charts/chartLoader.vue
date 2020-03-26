<template>
    <div class="ma-2">
        <v-layout v-if="dataIsLoading" justify-center>
            <v-progress-circular
                    id="loadCircle"
                    :size="70"
                    :width="7"
                    color="success"
                    indeterminate
            />
        </v-layout>
        <div v-else>
            <abstract-chart id="loadedChartData"
                            :chartData="loadedChartData"/>
            <v-layout v-if="loadedChartData.content.length > 0"
                      justify-center>
                <v-pagination v-model="currentPage"
                              :length="loadedChartData.totalPages"
                              :total-visible="visiblePages">
                </v-pagination>
            </v-layout>
        </div>
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
            userId: {
                required: false,
                type: [Number, String]
            },
        },
        name: "chartLoader",
        data() {
            return {
                loadedChartData: null,
                dataIsLoading: true,
                currentPage: 1,
                visiblePages: 5
            }
        },
        components: {
            abstractChart
        },
        watch: {
            currentPage(newVal, oldVal) {
                this.loadPage()
            }
        },
        async created() {
            this.loadPage()
        },
        methods: {
            async loadPage() {
                this.dataIsLoading = true
                const data = await this.loadFunction(this.currentPage - 1, this.userId)
                this.currentPage = data.number + 1
                this.loadedChartData = data
                this.dataIsLoading = false
            },
        }
    }
</script>

<style scoped>

</style>