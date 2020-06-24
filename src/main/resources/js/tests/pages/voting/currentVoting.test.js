import {flushPromises, localVueMock, mount, setupedRouterMock, setupedVuetifyMock, VuexMock} from '../../baseTest'
import currentVoting from "../../../pages/voting/currentVoting"
import * as ws from "../../../utils/websocket";
import routesNames from "../../../router/routesNames";

ws.connectWebsocket = jest.fn()
ws.sendVote = jest.fn()

let myMixinMock = {
    data() {
        return {
            mixinVotingKey: 'modifiedKey',
            mixinVoting: {
                id: testedVotingId,
                votingOptions: [{
                    id: testedOptionId
                }]
            },
            mixinVotingId: testedVotingId
        }
    },
    methods: {
        async mixinConnectToVoting(votingId, votingKey) {},
        async mixinCheckVotingSession(votingId, votingKey) {},
        async mixinGetVoting(votingId, votingKey) {},
    }
}

let testedVotingId = 1
let testedOptionId = 1
let testVotingKey = "testKey"
let votingProp = {
    id: 1,
    votingKey: "votingPropKey",
    votingOptions: [{id: 2}, {id: 3}]
}

let storeMock
let actionsMock
let gettersMock
let vuetifyMock = setupedVuetifyMock
let routerMock = setupedRouterMock

describe('test current voting', () => {
    beforeEach(() => {
        actionsMock = {
            addNewVotingAction: jest.fn(),
            checkCurrentSessionVotingAction: jest.fn()
        }
        gettersMock = {getVotingById: jest.fn()}
        storeMock = new VuexMock.Store({
            actions: actionsMock,
            getters: gettersMock
        })
        myMixinMock.methods.mixinConnectToVoting = jest.fn()
        myMixinMock.methods.mixinCheckVotingSession = jest.fn()
        myMixinMock.methods.mixinGetVoting = jest.fn()
    })
    it('test create component without ready voting prop', async () => {
        const wrapper = mount(currentVoting, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock, ws,
            mixins: [myMixinMock],
            propsData: {
                votingId: 1,
                votingKey: testVotingKey,
            }
        })

        await flushPromises()

        expect(wrapper.vm.currentVoting).not.toBeNull()
        expect(myMixinMock.methods.mixinConnectToVoting).toBeCalled()
        expect(wrapper.vm.currentVoting).toStrictEqual(myMixinMock.data().mixinVoting)
        expect(wrapper.vm.modifiedVotingKey).toBe(myMixinMock.data().mixinVotingKey)
    })
    it('test create component with ready voting prop', async () => {
        const wrapper = mount(currentVoting, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock, ws,
            mixins: [myMixinMock],
            propsData: {
                votingId: testedVotingId,
                votingKey: testVotingKey,
                currentVotingProp: votingProp
            }
        })

        await flushPromises()

        expect(wrapper.vm.currentVoting).not.toBeNull()
        expect(myMixinMock.methods.mixinConnectToVoting).not.toBeCalled()
        expect(ws.connectWebsocket).toBeCalledWith(wrapper.props().currentVotingProp.id, wrapper.props().votingKey)
        expect(wrapper.vm.currentVoting).toStrictEqual(wrapper.props().currentVotingProp)
        expect(wrapper.vm.modifiedVotingKey).toBe(wrapper.props().votingKey)
    })
    it('test go to results', async () => {
        const wrapper = mount(currentVoting, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock, ws,
            mixins: [myMixinMock],
            propsData: {votingId: testedVotingId}
        })

        await flushPromises()
        wrapper.find("#goToResultsBtn").trigger("click")
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.VOTING_RESULTS)
        expect(wrapper.vm.$route.path).toBe("/" + wrapper.props().votingId + "/results")
        expect(wrapper.vm.$route.params.votingId).toBe(wrapper.vm.currentVoting.id)
        expect(wrapper.vm.$route.params.votingKey).toBe(wrapper.vm.modifiedVotingKey)
        expect(wrapper.vm.$route.params.currentVotingProp).toStrictEqual(wrapper.vm.currentVoting)
    })
    it('do vote test', async () => {
        let testedVotingKey = "public"
        const wrapper = mount(currentVoting, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock, ws,
            mixins: [myMixinMock],
            propsData: {votingId: testedVotingId}
        })

        //skip mixin actions in 'created' hook
        await flushPromises()

        wrapper.setData({
            selectedOptionIndex: 0,
            modifiedVotingKey: testedVotingKey
        })
        await flushPromises()

        wrapper.find("#doVoteBtn").trigger("click")
        await flushPromises()

        //args: currentVoting.votingOptions[this.selectedOptionIndex].id, votingId, modifiedVotingKey
        expect(ws.sendVote).toBeCalledWith(testedOptionId, testedVotingId, testedVotingKey)
    })
})