import {flushPromises, localVueMock, setupedVuetifyMock, shallowMount} from '../../baseTest'
import chartLoader from "../../../components/charts/chartLoader";

let commonVoting
let loadFunctionMock

let vuetifyMock = setupedVuetifyMock

describe('chart loader test', () => {
    beforeEach(() => {
        commonVoting = {
            id: 1,
            votingTitle: 'votingTitle',
            isProtectedVoting: false,
            totalVotes: 0,
            owner: null
        }
        loadFunctionMock = jest.fn((currentPage, userId) => {
            return {
                number: 0,
                content: [commonVoting]
            }
        })
    })
    it('test load data', async () => {
        const wrapper = shallowMount(chartLoader, {
            vuetify: vuetifyMock, localVue: localVueMock,
            propsData: {
                userId: null,
                loadFunction: loadFunctionMock
            }
        })
        expect(wrapper.find("#loadCircle").exists()).toBeTruthy()
        expect(wrapper.find("#loadedChartData").exists()).toBeFalsy()

        await flushPromises()

        expect(wrapper.find("#loadCircle").exists()).toBeFalsy()
        expect(wrapper.find("#loadedChartData").exists()).toBeTruthy()
    })
    it('test load next page', async () => {
        const wrapper = shallowMount(chartLoader, {
            vuetify: vuetifyMock, localVue: localVueMock,
            propsData: {
                userId: null,
                loadFunction: loadFunctionMock
            }
        })
        await flushPromises()
        wrapper.setData({currentPage : 2})
        await flushPromises()
        expect(loadFunctionMock).toBeCalledTimes(3)
        expect(wrapper.vm.$data.currentPage).toBe(1)
    })
})