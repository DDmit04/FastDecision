import {flushPromises, localVueMock, setupedVuetifyMock, shallowMount,} from '../../../baseTest'
import searchChart from "../../../../pages/charts/searchChart";
import votingChart from "../../../../api/votingChart";

votingChart.search = jest.fn()

let vuetifyMock = setupedVuetifyMock

let testStringToSearch = 'search'

describe('search votings test', () => {
    it('test votings search', async () => {
        const wrapper = shallowMount(searchChart, {vuetify: vuetifyMock, localVue: localVueMock,
            propsData: {stringToSearch: testStringToSearch}
        })

        wrapper.vm.loadSearchedVotings(0)
        await flushPromises()

        expect(votingChart.search).toBeCalledWith(0, testStringToSearch)
    })
})