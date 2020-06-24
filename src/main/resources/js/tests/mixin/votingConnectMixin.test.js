import {flushPromises, localVueMock, mount, setupedRouterMock, VuexMock} from '../baseTest'
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

let storeMock
let actionsMock
let gettersMock
let routerMock = setupedRouterMock

describe('test voting connect mixin', () => {
    beforeEach(() => {
        actionsMock = {checkCurrentSessionVotingAction: jest.fn()}
        gettersMock = {
            getVotingById: () => jest.fn(() => {
                return {
                    votingKey: ""
                }
            })
        }
        storeMock = new VuexMock.Store({
            actions: actionsMock,
            getters: gettersMock
        })
    })
    it('test connect to voting', async () => {
        const wrapper = mount(mixinComponent, {store: storeMock, router: routerMock, localVue: localVueMock, ws, server})

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
        const wrapper = mount(mixinComponent, {store: storeMock, router: routerMock, localVue: localVueMock, ws, server})

        wrapper.vm.mixinCheckVotingSession()
        await flushPromises()

        expect(actionsMock.checkCurrentSessionVotingAction).toBeCalled()
    })
    it('test get voting', async () => {
        const wrapper = mount(mixinComponent, {store: storeMock, router: routerMock, localVue: localVueMock, ws, server})

        wrapper.vm.mixinGetVoting()
        await flushPromises()

        expect(server.getOne).toBeCalled()
    })
})