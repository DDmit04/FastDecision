import {flushPromises, localVue, setupedRouter, setupedVuetify, shallowMount, Vuex} from '../../baseTest'
import votingResults from "../../../pages/voting/votingResults";
import * as ws from "../../../utils/websocket";
import Donut from 'vue-css-donut-chart'


let testedVotingId = 1
let myMixin = {
    data() {
        return {
            mixinVotingKey: 'public',
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

localVue.use(Donut)

let store
let actions
let getters
let vuetify = setupedVuetify

describe('test voting results', () => {
    beforeEach(() => {
        actions = {addNewVotingAction: jest.fn(),}
        getters = {getVotingById: jest.fn()}
        store = new Vuex.Store({
            actions,
            getters
        })
    })
    it('test voting results visual', async () => {
        const wrapper = shallowMount(votingResults, {store, vuetify, router: setupedRouter, localVue, ws,
            mixins: [myMixin],
            propsData: {votingId: testedVotingId}
        })

        await flushPromises()

        expect(wrapper.find("#votingResultsTitle").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsTitle").text()).toBe(myMixin.data().mixinVoting.votingTitle)

        expect(wrapper.find("#votingResultsOptions0").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription0").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription0").text())
            .toBe(myMixin.data().mixinVoting.votingOptions[0].voteDiscription)
        expect(wrapper.find("#votingResultsPluses0").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsPluses0").text())
            .toBe(myMixin.data().mixinVoting.votingOptions[0].pluses + " votes")

        expect(wrapper.find("#votingResultsOptions1").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription1").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsDiscription1").text())
            .toBe(myMixin.data().mixinVoting.votingOptions[1].voteDiscription)
        expect(wrapper.find("#votingResultsPluses1").exists()).toBeTruthy()
        expect(wrapper.find("#votingResultsPluses1").text())
            .toBe(myMixin.data().mixinVoting.votingOptions[1].pluses + " votes")
    })
    it('test votes line %', async () => {
        const wrapper = shallowMount(votingResults, {store, vuetify, router: setupedRouter, localVue, ws,
            mixins: [myMixin],
            propsData: {votingId: testedVotingId},
        })

        await flushPromises()
        let totalVotes = 0
        myMixin.data().mixinVoting.votingOptions.forEach(v => totalVotes+= v.pluses)

        expect(wrapper.find("#optionPlusesCount0").text())
            .toBe((myMixin.data().mixinVoting.votingOptions[0].pluses / totalVotes * 100).toFixed(1) + " %")
        expect(wrapper.find("#optionPlusesCount1").text())
            .toBe((myMixin.data().mixinVoting.votingOptions[1].pluses / totalVotes * 100).toFixed(1) + " %")

    })
})