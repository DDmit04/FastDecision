import {flushPromises, localVue, mount, setupedVuetify, shallowMount, Vuex} from '../../baseTest'
import createVoting from "../../../pages/voting/createVoting";

let store
let actions
let vuetify = setupedVuetify

describe('test create voting', () => {
    beforeEach(() => {
        actions = {addNewVotingAction: jest.fn()}
        store = new Vuex.Store({actions})
    })
    it('add new option on click', async () => {
        const wrapper = shallowMount(createVoting, {store, vuetify, localVue})

        wrapper.find("#newVotingOption1").vm.$emit("click")
        await flushPromises()

        expect(wrapper.findAll("v-text-field-stub").length).toBe(4)
        expect(wrapper.find("#newVotingOption2").exists()).toBeTruthy()
    })
    it('add new option on focus', async () => {
        const wrapper = shallowMount(createVoting, {store, vuetify, localVue})

        wrapper.find("#newVotingOption1").vm.$emit("focus")
        await flushPromises()

        expect(wrapper.findAll("v-text-field-stub").length).toBe(4)
        expect(wrapper.find("#newVotingOption2").exists()).toBeTruthy()
    })
    it('add new voting', async () => {
        const wrapper = mount(createVoting, {store, vuetify, localVue})

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

        expect(actions.addNewVotingAction).toBeCalled()
    })
})