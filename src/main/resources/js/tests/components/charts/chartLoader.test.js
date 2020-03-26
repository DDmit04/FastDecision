import {flushPromises, localVue, setupedVuetify, shallowMount} from '../../baseTest'
import chartLoader from "../../../components/charts/chartLoader";

let commonVoting
let loadFunction

let vuetify = setupedVuetify

describe('chart loader test', () => {
    beforeEach(() => {
        commonVoting = {
            id: 1,
            votingTitle: 'votingTitle',
            isProtectedVoting: false,
            totalVotes: 0,
            owner: null
        }
        loadFunction = jest.fn((currentPage, userId) => {
            return {
                number: 0,
                content: [commonVoting]
            }
        })
    })
    it('test load data', async () => {
        const wrapper = shallowMount(chartLoader, {
            vuetify, localVue,
            propsData: {
                userId: null,
                loadFunction: loadFunction
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
            vuetify, localVue,
            propsData: {
                userId: null,
                loadFunction: loadFunction
            }
        })
        await flushPromises()
        wrapper.setData({currentPage : 2})
        await flushPromises()
        expect(loadFunction).toBeCalledTimes(3)
        expect(wrapper.vm.$data.currentPage).toBe(1)
    })
})