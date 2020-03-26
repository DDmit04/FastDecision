import {flushPromises, localVue, setupedVuetify, shallowMount,} from '../../../baseTest'
import searchChart from "../../../../pages/charts/searchChart";
import votingChart from "../../../../api/votingChart";

votingChart.search = jest.fn()

let vuetify = setupedVuetify

let testStringToSearch = 'search'

describe('search votings test', () => {
    it('test votings search', async () => {
        const wrapper = shallowMount(searchChart, {vuetify, localVue,
            propsData: {stringToSearch: testStringToSearch}
        })

        wrapper.vm.loadSearchedVotings(0)
        await flushPromises()

        expect(votingChart.search).toBeCalledWith(0, testStringToSearch)
    })
})