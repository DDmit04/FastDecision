import {
    flushPromises,
    localVueMock,
    mount,
    setupedRouterMock,
    setupedVuetifyMock,
    shallowMount,
    VuexMock
} from '../../baseTest'
import createVoting from "../../../pages/voting/createVoting";
import routesNames from "../../../router/routesNames";

let currentStoreVotingMock = {
    id: 1,
    votingKey: 'public',
    votingOptions: [{id:2}, {id:3}]
}
let storeMock
let actionsMock
let stateMock
let vuetifyMock = setupedVuetifyMock
let routerMock = setupedRouterMock

describe('test create voting', () => {
    beforeEach(() => {
        actionsMock = {
            addNewVotingAction: jest.fn()
        }
        stateMock = {
            currentStoreVoting: currentStoreVotingMock
        }
        storeMock = new VuexMock.Store({
            state: stateMock,
            actions: actionsMock
        })
    })
    it('add new option on click', async () => {
        const wrapper = shallowMount(createVoting, {store: storeMock, vuetify: vuetifyMock, localVue: localVueMock})

        wrapper.find("#newVotingOption1").vm.$emit("click")
        await flushPromises()

        expect(wrapper.findAll("v-text-field-stub").length).toBe(4)
        expect(wrapper.find("#newVotingOption2").exists()).toBeTruthy()
    })
    it('add new option on focus', async () => {
        const wrapper = shallowMount(createVoting, {store: storeMock, vuetify: vuetifyMock, localVue: localVueMock})

        wrapper.find("#newVotingOption1").vm.$emit("focus")
        await flushPromises()

        expect(wrapper.findAll("v-text-field-stub").length).toBe(4)
        expect(wrapper.find("#newVotingOption2").exists()).toBeTruthy()
    })
    it('add new voting', async () => {
        const wrapper = mount(createVoting, {store: storeMock, vuetify: vuetifyMock, localVue: localVueMock, router: routerMock})

        const votingTitle = wrapper.find("#newVotingTitle")
        votingTitle.element.value = 'title'
        votingTitle.trigger('input')
        await flushPromises()

        const votingOption0 = wrapper.find("#newVotingOption0")
        votingOption0.element.value = 'option0'
        votingOption0.trigger('input')
        await flushPromises()

        const votingOption1 = wrapper.find("#newVotingOption1")
        votingOption1.element.value = 'option1'
        votingOption1.trigger('input')
        await flushPromises()

        wrapper.find("#addVotingBtn").trigger("click")
        
        expect(actionsMock.addNewVotingAction).toBeCalled()
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.CURRENT_VOTING)
        expect(wrapper.vm.$route.path).toBe("/" + stateMock.currentStoreVoting.id + "/vote")
        expect(wrapper.vm.$route.params.votingId).toBe(stateMock.currentStoreVoting.id)
        expect(wrapper.vm.$route.params.votingKey).toBe(stateMock.currentStoreVoting.votingKey)
        expect(wrapper.vm.$route.params.currentVotingProp).toStrictEqual(stateMock.currentStoreVoting)
    })
})