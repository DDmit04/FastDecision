import {flushPromises, localVue, mount, setupedRouter, setupedVuetify, Vuex} from '../../baseTest'
import currentVoting from "../../../pages/voting/currentVoting"
import * as ws from "../../../utils/websocket";
import routesNames from "../../../router/routesNames";

ws.sendVote = jest.fn()

let testedVotingId = 1
let testedOptionId = 1
let myMixin = {
    data() {
        return {
            mixinVotingKey: 'public',
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


let store
let actions
let getters
let vuetify = setupedVuetify
let router = setupedRouter

describe('test create voting', () => {
    beforeEach(() => {
        actions = {
            addNewVotingAction: jest.fn(),
            checkCurrentSessionVotingAction: jest.fn()
        }
        getters = {getVotingById: jest.fn()}
        store = new Vuex.Store({
            actions,
            getters
        })
    })
    it('test add new option on click', async () => {
        let testedVotingKey = "public"
        const wrapper = mount(currentVoting, {store, vuetify, router, localVue, ws,
            mixins: [myMixin],
            propsData: {votingId: testedVotingId}
        })

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
    it('test go to results', async () => {
        const wrapper = mount(currentVoting, {store, vuetify, router, localVue, ws,
            mixins: [myMixin],
            propsData: {votingId: 1},
        })

        await flushPromises()
        wrapper.find("#goToResultsBtn").trigger("click")
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.VOTING_RESULTS)
        expect(wrapper.vm.$route.path).toBe("/" + wrapper.props().votingId + "/results")
        expect(wrapper.vm.$route.params.votingId).toBe(wrapper.props().votingId)
        expect(wrapper.vm.$route.params.votingKey).toBe("public")
    })
})