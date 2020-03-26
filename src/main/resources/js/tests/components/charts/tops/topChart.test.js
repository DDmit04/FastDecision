import {flushPromises, localVue, setupedVuetify, shallowMount,} from '../../../baseTest'
import topChart from "../../../../pages/charts/topChart";
import votingChart from "../../../../api/votingChart";

votingChart.getPopular = jest.fn()
votingChart.getNewest = jest.fn()

let vuetify = setupedVuetify

let chartTypePopular = 'popular'
let chartTypeNewest = 'newest'

describe('top chart test', () => {
    it('test popular chart load function', async () => {
        const wrapper = shallowMount(topChart, {vuetify, localVue,
            propsData: {topChartType: chartTypePopular}
        })

        wrapper.vm.loadPopularVotings(0)
        await flushPromises()

        expect(votingChart.getPopular).toBeCalledWith(0)
    })
    it('test newest chart load function', async () => {
        const wrapper = shallowMount(topChart, {vuetify, localVue,
            propsData: {topChartType: chartTypeNewest}
        })

        wrapper.vm.loadNewestVotings(0)
        await flushPromises()

        expect(votingChart.getNewest).toBeCalledWith(0)
    })
})