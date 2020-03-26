import {flushPromises, localVue, mount, setupedRouter, Vuex} from '../baseTest'
import votingConnectMixin from "../../mixins/votingConnectMixin"
import * as ws from "../../utils/websocket";
import server from "../../api/server";

server.getOne = jest.fn()
ws.connectWebsocket = jest.fn()
ws.disconnectWebsocket = jest.fn()

const mixinComponent = {
    render() {},
    mixins: [votingConnectMixin]
}

let store
let actions
let getters
let router = setupedRouter

describe('test voting connect mixin', () => {
    beforeEach(() => {
        actions = {checkCurrentSessionVotingAction: jest.fn()}
        getters = {
            getVotingById: () => jest.fn(() => {
                return {
                    votingKey: ""
                }
            })
        }
        store = new Vuex.Store({
            actions,
            getters
        })
    })
    it('test connect to voting', async () => {
        const wrapper = mount(mixinComponent, {store, router, localVue, ws, server})

        const mockMixinCheckVotingSession = jest.fn()
        const mockMixinGetVoting = jest.fn()
        wrapper.vm.mixinGetVoting = mockMixinGetVoting
        wrapper.vm.mixinCheckVotingSession = mockMixinCheckVotingSession

        wrapper.vm.mixinConnectToVoting()
        await flushPromises()

        expect(wrapper.vm.mixinCheckVotingSession).toBeCalled()
        expect(wrapper.vm.mixinGetVoting).toBeCalled()
        expect(ws.connectWebsocket).toBeCalled()
    })
    it('test check session', async () => {
        const wrapper = mount(mixinComponent, {store, router, localVue, ws, server})

        wrapper.vm.mixinCheckVotingSession()
        await flushPromises()

        expect(actions.checkCurrentSessionVotingAction).toBeCalled()
    })
    it('test get voting', async () => {
        const wrapper = mount(mixinComponent, {store, router, localVue, ws, server})

        wrapper.vm.mixinGetVoting()
        await flushPromises()

        expect(server.getOne).toBeCalled()
    })
})