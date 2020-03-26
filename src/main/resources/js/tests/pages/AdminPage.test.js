import {flushPromises, localVue, mount, setupedRouter, setupedVuetify, shallowMount, Vuex} from '../baseTest'
import adminPage from "../../pages/adminPage";
import admin from "../../api/admin"
import routesNames from "../../router/routesNames";

admin.getAdmin = jest.fn(() => {
    return "body: {}"
})

let store
let mutations
let state
let vuetify = setupedVuetify
let router = setupedRouter

describe('admin page by admin', () => {
    beforeEach(() => {
        state = {
            currentUser: {
                id: 1,
                username: 'username',
                roles: ['ADMIN']
            }
        }
        mutations = {refreshCurrentUserRolesMutations: jest.fn()}
        store = new Vuex.Store({
            state,
            mutations
        })
    })
    it('admin panel existing components', () => {
        const wrapper = shallowMount(adminPage, {store, vuetify, router, admin, localVue})

        expect(wrapper.find("#adminControlPanel").exists()).toBeTruthy()
        expect(wrapper.find("#askAdminPanel").exists()).toBeFalsy()
    })
})

describe('admin page by common user', () => {
    beforeEach(() => {
        state = {
            currentUser: {
                id: 1,
                username: 'username',
                roles: ['USER']
            }
        }
        mutations = {refreshCurrentUserRolesMutations: jest.fn()}
        store = new Vuex.Store({
            state,
            mutations
        })
    })
    it('successful ask admin test', async () => {
        const wrapper = mount(adminPage, {store, vuetify, router, admin, localVue})

        wrapper.find('#askAdminBtn').trigger('click')
        await flushPromises()
        expect(mutations.refreshCurrentUserRolesMutations).toBeCalled()
    })
    it('admin page existing components', () => {
        const wrapper = mount(adminPage, {store, vuetify, router, admin, localVue})

        expect(wrapper.find("#askAdminPanel").exists()).toBeTruthy()
        expect(wrapper.find("#adminControlPanel").exists()).toBeFalsy()
    })
})

describe('empty user try use admin page', () => {
    beforeEach(() => {
        state = {currentUser: null}
        mutations = {refreshCurrentUserRolesMutations: jest.fn()}
        store = new Vuex.Store({
            state,
            mutations
        })
    })
    it('empty user is redirected', () => {
        const wrapper = shallowMount(adminPage, {store, vuetify, router, admin, localVue})

        expect(wrapper.vm.$route.name).toBe(routesNames.MAIN)
        expect(wrapper.vm.$route.path).toBe('/')
    })
})