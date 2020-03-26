import {flushPromises, localVue, mount, setupedRouter, setupedVuetify, shallowMount, Vuex} from '../../baseTest'
import abstractChart from "../../../components/charts/abstractChart"
import filters from "../../../filters/filters"
import server from "../../../api/server"
import routesNames from "../../../router/routesNames";

server.deleteOne = jest.fn(() => {
    return {ok: true}
})

let router = setupedRouter
let vuetify = setupedVuetify

let currentUser
let otherVotingOwner
let commonVoting
let otherCommonVoting
let testChartData
let testChartDataContent

let store
let state

describe('chart test', () => {
    beforeEach(() => {
        currentUser = {
            id: 1,
            username: 'username',
            roles: []
        }
        otherVotingOwner = {
            id: 2,
            username: 'otherUsername',
            roles: []
        }
        commonVoting = {
            id: 1,
            votingTitle: 'votingTitle',
            isProtectedVoting: false,
            totalVotes: 0,
            owner: null
        }
        otherCommonVoting = {
            id: 2,
            votingTitle: 'votingTitle',
            isProtectedVoting: false,
            totalVotes: 0,
            owner: null
        }
        testChartDataContent = []
        testChartDataContent.push(commonVoting)
        testChartDataContent.push(otherCommonVoting)
        testChartData = {
            content: testChartDataContent
        }
        
        state = {currentUser: currentUser}
        store = new Vuex.Store({state})
    })
    it('test go to voting', async () => {
        const wrapper = mount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })

        wrapper.find("#voting1").trigger("click")
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.CURRENT_VOTING)
        expect(wrapper.vm.$route.path).toBe('/' + commonVoting.id + '/vote')
    })
    it('test go to voting owner', async () => {
        commonVoting.owner = currentUser
        const wrapper = mount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })

        wrapper.find("#authorBtn1").trigger("click.stop")
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.USER_VOTINGS_CHART)
        expect(wrapper.vm.$route.path).toBe('/dashboard/' + currentUser.id)
    })
    it('test voitng count', () => {
        const wrapper = shallowMount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.findAll("v-card-stub").length).toBe(2)
    })
    it('test voitng title', () => {
        commonVoting.votingTitle = 'votingTitle'
        const wrapper = shallowMount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#votingTitle1").exists()).toBeTruthy()
        expect(wrapper.find("#votingTitle1").text()).toBe(filters.normalizeString('votingTitle'))
    })
    it('test voitng total votes', () => {
        commonVoting.totalVotes = 10
        const wrapper = shallowMount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#totalVotes1").exists()).toBeTruthy()
        expect(wrapper.find("#totalVotes1").text()).toBe('total votes: 10')
    })
    it('test unknown author', () => {
        commonVoting.owner = null
        const wrapper = shallowMount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#authorBtn1").exists()).toBeFalsy()
        expect(wrapper.find("#unknownAuthorBtn1").exists()).toBeTruthy()
        expect(wrapper.find("#unknownAuthorBtn1").text()).toBe(filters.normalizeString('Unknown'))
    })
    it('test defined voting author', () => {
        commonVoting.owner = otherVotingOwner
        const wrapper = shallowMount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#unknownAuthorBtn1").exists()).toBeFalsy()
        expect(wrapper.find("#authorBtn1").exists()).toBeTruthy()
        expect(wrapper.find("#authorBtn1").text()).toBe(filters.normalizeString('otherUsername'))
    })
    it('test is protected icon', () => {
        commonVoting.isProtectedVoting = true
        const wrapper = mount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#isProtecedVoting1").exists()).toBeTruthy()
    })
    it('test delete voting btn by it author', () => {
        commonVoting.owner = currentUser
        const wrapper = shallowMount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#deleteBtn1").exists()).toBeTruthy()
    })
    it('test delete voting btn by other user', () => {
        commonVoting.owner = otherVotingOwner
        const wrapper = mount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#deleteBtn1").exists()).toBeFalsy()
    })
    it('test delete voting btn by admin', () => {
        currentUser.roles = ['ADMIN']
        commonVoting.owner = currentUser
        const wrapper = shallowMount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        expect(wrapper.find("#deleteBtn1").exists()).toBeTruthy()
    })
    it('test call delete voting', async () => {
        currentUser.roles = ['ADMIN']
        commonVoting.owner = currentUser
        const wrapper = mount(abstractChart, {vuetify, router, localVue, store,
            propsData: {chartData: testChartData}
        })
        
        wrapper.find("#deleteBtn1").trigger("click")
        await flushPromises()
        
        expect(wrapper.vm.$data.deletedVoting).toBe(commonVoting)
        expect(wrapper.vm.$data.modalIsActive).toBe(true)
    })
    it('test delete voting', async () => {
        currentUser.roles = ['ADMIN']
        commonVoting.owner = currentUser
        const wrapper = mount(abstractChart, {vuetify, router, localVue, store, server,
            propsData: {chartData: testChartData}
        })
        
        wrapper.find("#deleteBtn1").trigger("click")
        await flushPromises()
        
        wrapper.vm.deleteVoting()
        await flushPromises()

        expect(wrapper.vm.$data.votings.length).toBe(1)
    })
})