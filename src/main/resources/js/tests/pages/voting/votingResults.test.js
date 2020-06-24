import {flushPromises, localVueMock, mount, setupedVuetifyMock, shallowMount, VuexMock} from '../../baseTest'
import votingResults from "../../../pages/voting/votingResults";
import * as ws from "../../../utils/websocket";
import Donut from 'vue-css-donut-chart'


let testedVotingId = 1
let myMixinMock = {
    data() {
        return {
            mixinVotingKey: 'modifiedKey',
            mixinVoting: {
                id: testedVotingId,
                votingTitle: 'title',
                votingOptions: [
                    {
                        id: 1,
                        voteDiscription: 'option0',
                        pluses: 4
                    },
                    {
                        id: 2,
                        voteDiscription: 'option1',
                        pluses: 6
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

ws.addHandler = jest.fn()
ws.connectWebsocket = jest.fn()

localVueMock.use(Donut)

let testVotingKey = 'testKey'
let votingProp = {
    id: 1,
    votingTitle: "titleFromProp",
    votingKey: "votingPropKey",
    votingOptions: [
        {
            id: 3,
            voteDiscription: 'option2',
            pluses: 5
        },
        {
            id: 4,
            voteDiscription: 'option3',
            pluses: 7
        }]
}

let storeMock
let actionsMock
let gettersMock
let vuetifyMock = setupedVuetifyMock

describe('test voting results', () => {
    beforeEach(() => {
        actionsMock = {addNewVotingAction: jest.fn(),}
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
        const wrapper = mount(votingResults, {store: storeMock, vuetify: vuetifyMock, localVue: localVueMock, ws,
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
    })
    it('test create component with ready voting prop', async () => {
        const wrapper = mount(votingResults, {store: storeMock, vuetify: vuetifyMock, localVue: localVueMock, ws,
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
    })
    it('test voting results visual', async () => {
        const wrapper = shallowMount(votingResults, {store: storeMock, vuetify: vuetifyMock, localVue: localVueMock, ws,
            mixins: [myMixinMock],
            propsData: {votingId: testedVotingId}
        })

        await flushPromises()


        expect(wrapper.find("#votingResultsTitle").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsTitle").text()).toBe(wrapper.vm.currentVoting.votingTitle)

        expect(wrapper.find("#votingResultsOptions0").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription0").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription0").text())
            .toBe(wrapper.vm.currentVoting.votingOptions[0].voteDiscription)
        expect(wrapper.find("#votingResultsPluses0").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsPluses0").text())
            .toBe(wrapper.vm.currentVoting.votingOptions[0].pluses + " votes")

        expect(wrapper.find("#votingResultsOptions1").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription1").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription1").text())
            .toBe(wrapper.vm.currentVoting.votingOptions[1].voteDiscription)
        expect(wrapper.find("#votingResultsPluses1").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsPluses1").text())
            .toBe(wrapper.vm.currentVoting.votingOptions[1].pluses + " votes")
    })
    it('test votes line %', async () => {
        const wrapper = shallowMount(votingResults, {store: storeMock, vuetify: vuetifyMock, localVue: localVueMock, ws,
            mixins: [myMixinMock],
            propsData: {votingId: testedVotingId},
        })

        await flushPromises()
        let totalVotes = 0
        myMixinMock.data().mixinVoting.votingOptions.forEach(v => totalVotes+= v.pluses)

        expect(wrapper.find("#optionPlusesCount0").text())
            .toBe((myMixinMock.data().mixinVoting.votingOptions[0].pluses / totalVotes * 100).toFixed(1) + " %")
        expect(wrapper.find("#optionPlusesCount1").text())
            .toBe((myMixinMock.data().mixinVoting.votingOptions[1].pluses / totalVotes * 100).toFixed(1) + " %")

    })
})