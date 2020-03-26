import {flushPromises, localVue, setupedVuetify, shallowMount, Vuex} from '../../../baseTest'
import userChart from "../../../../pages/charts/userChart"
import votingChart from "../../../../api/votingChart";

votingChart.getUserPrivate = jest.fn()
votingChart.getUserPublic = jest.fn()

let vuetify = setupedVuetify
let currentUser

let store
let state

describe('user chart test', () => {
    beforeEach(() => {
        currentUser = {
            id: 1,
            username: 'username',
            roles: []
        }
        state = {currentUser: currentUser}
        store = new Vuex.Store({state})
    })
    it('test user chart by owner',  () => {
        const wrapper = shallowMount(userChart, {vuetify, localVue, store,
            propsData: {userId: 1}
        })

        expect(wrapper.find("#publicVotingsTab").exists()).toBeTruthy()
        expect(wrapper.find("#privateVotingsTab").exists()).toBeTruthy()
    })
    it('test user chart by other user', () => {
        const wrapper = shallowMount(userChart, {vuetify, localVue, store,
            propsData: {userId: 2}
        })

        expect(wrapper.find("#publicVotingsTab").exists()).toBeTruthy()
        expect(wrapper.find("#privateVotingsTab").exists()).toBeFalsy()
    })
    it('test public chart load function', async () => {
        const wrapper = shallowMount(userChart, {vuetify, localVue, store,
            propsData: {userId: 2}
        })

        wrapper.vm.loadUserPublic(wrapper.props().userId, 0)
        await flushPromises()

        expect(votingChart.getUserPublic).toBeCalledWith(wrapper.props().userId, 0)
    })
    it('test private chart load function', async () => {
        const wrapper = shallowMount(userChart, {vuetify, localVue, store,
            propsData: {userId: 1}
        })

        wrapper.vm.loadUserPrivate(wrapper.props().userId, 0)
        await flushPromises()

        expect(votingChart.getUserPrivate).toBeCalledWith(wrapper.props().userId, 0)
    })
})