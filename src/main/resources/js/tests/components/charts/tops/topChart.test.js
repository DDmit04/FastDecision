import {flushPromises, localVueMock, setupedVuetifyMock, shallowMount,} from '../../../baseTest'
import topChart from "../../../../pages/charts/topChart";
import votingChart from "../../../../api/votingChart";

votingChart.getPopular = jest.fn()
votingChart.getNewest = jest.fn()

let vuetifyMock = setupedVuetifyMock

let chartTypePopular = 'popular'
let chartTypeNewest = 'newest'

describe('top chart test', () => {
    it('test popular chart load function', async () => {
        const wrapper = shallowMount(topChart, {vuetify: vuetifyMock, localVue: localVueMock,
            propsData: {topChartType: chartTypePopular}
        })

        wrapper.vm.loadPopularVotings(0)
        await flushPromises()

        expect(votingChart.getPopular).toBeCalledWith(0)
    })
    it('test newest chart load function', async () => {
        const wrapper = shallowMount(topChart, {vuetify: vuetifyMock, localVue: localVueMock,
            propsData: {topChartType: chartTypeNewest}
        })

        wrapper.vm.loadNewestVotings(0)
        await flushPromises()

        expect(votingChart.getNewest).toBeCalledWith(0)
    })
})