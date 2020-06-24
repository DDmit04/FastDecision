import {flushPromises, localVueMock, mount, setupedRouterMock, setupedVuetifyMock, VuexMock} from '../baseTest'
import navbar from "../../components/navbar"
import routesNames from "../../router/routesNames";

let currentUserMock = {
    id: 1,
    username: 'username',
    userPic: null
}
let storeMock
let mutationsMock
let stateMock
let vuetifyMock = setupedVuetifyMock
let routerMock = setupedRouterMock

describe('common user navbar', () => {
    beforeEach(() => {
        stateMock = {
            currentUser: currentUserMock,
            isDarkTheme: false
        }
        mutationsMock = {changeThemeMutation: jest.fn(() => stateMock.isDarkTheme = !stateMock.isDarkTheme)}
        storeMock = new VuexMock.Store({
            state: stateMock,
            mutations: mutationsMock
        })
    })
    it('click on icon turns to main page', async () => {
        const wrapper = mount(navbar, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock})

        wrapper.find('#mainPageBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.MAIN)
        expect(wrapper.vm.$route.path).toBe('/')
    })
    it('click on username icon turns to user votings page', async () => {
        const wrapper = mount(navbar, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock})

        wrapper.find('#userVotingsBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.USER_VOTINGS_CHART)
        expect(wrapper.vm.$route.path).toBe('/dashboard/1')
    })
    it('switch theme', async () => {
        const wrapper = mount(navbar, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock})

        wrapper.find('#switchThemeBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$vuetify.theme.dark).toBeTruthy()
        expect(mutationsMock.changeThemeMutation).toBeCalled()
    })
    it('search votings', async () => {
        const wrapper = mount(navbar, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock})

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
        stateMock = {
            currentUser: null,
            isDarkTheme: false
        }
        mutationsMock = {changeThemeMutation: jest.fn(() => stateMock.isDarkTheme = !stateMock.isDarkTheme)}
        storeMock = new VuexMock.Store({
            state: stateMock,
            mutations: mutationsMock
        })
    })
    it('click on username icon calls auth modal', async () => {
        const wrapper = mount(navbar, {store: storeMock, vuetify: vuetifyMock, router: routerMock, localVue: localVueMock})

        wrapper.find('#userVotingsBtn').trigger('click')
        await flushPromises()

        expect(wrapper.vm.$data.modalIsActive).toBeTruthy()
        expect(wrapper.vm.$route.name).not.toBe(routesNames.USER_VOTINGS_CHART)
        expect(wrapper.vm.$route.path).not.toBe('/dashboard/1')
    })
})