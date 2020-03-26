import {flushPromises, localVue, mount, setupedRouter, setupedVuetify, Vuex} from '../baseTest'
import navbar from "../../components/navbar"
import routesNames from "../../router/routesNames";

let store
let mutations
let state
let vuetify = setupedVuetify
let router = setupedRouter

describe('common user navbar', () => {
    beforeEach(() => {
        state = {
            currentUser: {
                id: 1,
                username: 'username',
                userPic: null
            },
            isDarkTheme: false
        }
        mutations = {changeThemeMutation: jest.fn(() => state.isDarkTheme = !state.isDarkTheme)}
        store = new Vuex.Store({
            state,
            mutations
        })
    })
    it('click on icon turns to main page', async () => {
        const wrapper = mount(navbar, {store, vuetify, router, localVue})

        wrapper.find('#mainPageBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.MAIN)
        expect(wrapper.vm.$route.path).toBe('/')
    })
    it('click on username icon turns to user votings page', async () => {
        const wrapper = mount(navbar, {store, vuetify, router, localVue})

        wrapper.find('#userVotingsBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.USER_VOTINGS_CHART)
        expect(wrapper.vm.$route.path).toBe('/dashboard/1')
    })
    it('switch theme', async () => {
        const wrapper = mount(navbar, {store, vuetify, router, localVue})

        wrapper.find('#switchThemeBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$vuetify.theme.dark).toBeTruthy()
        expect(mutations.changeThemeMutation).toBeCalled()
    })
    it('search votings', async () => {
        const wrapper = mount(navbar, {store, vuetify, router, localVue})

        const searchInput = wrapper.find("#votingSearch")
        searchInput.element.value = 'testSearch'
        searchInput.trigger('input')
        await flushPromises()

        wrapper.find("#searchBtn").trigger("click")
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.SEARCH_RESULTS)
        expect(wrapper.vm.$route.path).toBe('/search/testSearch')
        expect(wrapper.vm.$route.params.stringToSearch).toBe('testSearch')
    })
})

describe('empty user navbar', () => {
    beforeEach(() => {
        state = {
            currentUser: null,
            isDarkTheme: false
        }
        mutations = {changeThemeMutation: jest.fn(() => state.isDarkTheme = !state.isDarkTheme)}
        store = new Vuex.Store({
            state,
            mutations
        })
    })
    it('click on username icon calls auth modal', async () => {
        const wrapper = mount(navbar, {store, vuetify, router, localVue})

        wrapper.find('#userVotingsBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$data.modalIsActive).toBeTruthy()
        expect(wrapper.vm.$route.name).not.toBe(routesNames.USER_VOTINGS_CHART)
        expect(wrapper.vm.$route.path).not.toBe('/dashboard/1')
    })
})