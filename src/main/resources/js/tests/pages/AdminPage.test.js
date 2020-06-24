import {
    flushPromises,
    localVueMock,
    mount,
    setupedRouterMock,
    setupedVuetifyMock,
    shallowMount,
    VuexMock
} from '../baseTest'
import adminPage from "../../pages/adminPage";
import admin from "../../api/admin"
import routesNames from "../../router/routesNames";

admin.getAdmin = jest.fn(() => {
    return "body: {}"
})

let currentUser
let storeMock
let mutationsMock
let stateMock
let vuetifyMock = setupedVuetifyMock
let routerMock = setupedRouterMock

describe('admin page by admin', () => {
    beforeEach(() => {
        currentUser = {
            id: 1,
            username: 'username',
            roles: ['ADMIN']
        }
        stateMock = { currentUser }
        mutationsMock = {refreshCurrentUserRolesMutations: jest.fn()}
        storeMock = new VuexMock.Store({
            state: stateMock,
            mutations: mutationsMock
        })
    })
    it('admin panel existing components', () => {
        const wrapper = shallowMount(adminPage, {store: storeMock, vuetify: vuetifyMock, router: routerMock, admin, localVue: localVueMock})

        expect(wrapper.find("#adminControlPanel").exists()).toBeTruthy()
        expect(wrapper.find("#askAdminPanel").exists()).toBeFalsy()
    })
})

describe('admin page by common user', () => {
    beforeEach(() => {
        currentUser = {
            id: 1,
            username: 'username',
            roles: ['USER']
        }
        stateMock = { currentUser }
        mutationsMock = {refreshCurrentUserRolesMutations: jest.fn()}
        storeMock = new VuexMock.Store({
            state: stateMock,
            mutations: mutationsMock
        })
    })
    it('successful ask admin test', async () => {
        const wrapper = mount(adminPage, {store: storeMock, vuetify: vuetifyMock, router: routerMock, admin, localVue: localVueMock})

        wrapper.find('#askAdminBtn').trigger('click')
        await flushPromises()
        expect(mutationsMock.refreshCurrentUserRolesMutations).toBeCalled()
    })
    it('admin page existing components', () => {
        const wrapper = mount(adminPage, {store: storeMock, vuetify: vuetifyMock, router: routerMock, admin, localVue: localVueMock})

        expect(wrapper.find("#askAdminPanel").exists()).toBeTruthy()
        expect(wrapper.find("#adminControlPanel").exists()).toBeFalsy()
    })
})

describe('empty user try use admin page', () => {
    beforeEach(() => {
        stateMock = {currentUser: null}
        mutationsMock = {refreshCurrentUserRolesMutations: jest.fn()}
        storeMock = new VuexMock.Store({
            state: stateMock,
            mutations: mutationsMock
        })
    })
    it('empty user is redirected', () => {
        const wrapper = shallowMount(adminPage, {store: storeMock, vuetify: vuetifyMock, router: routerMock, admin, localVue: localVueMock})

        expect(wrapper.vm.$route.name).toBe(routesNames.MAIN)
        expect(wrapper.vm.$route.path).toBe('/')
    })
})