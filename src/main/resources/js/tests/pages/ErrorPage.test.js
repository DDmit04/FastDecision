import {localVueMock, setupedRouterMock, setupedVuetifyMock, shallowMount} from '../baseTest'
import errorPage from "../../pages/errorPage";

let vuetifyMock = setupedVuetifyMock
let routerMock = setupedRouterMock

describe('testing error page', () => {
    it('some error (with props)', () => {
        const errorReason = 'forbidden'
        const errorCode = 403

        const wrapper = shallowMount(errorPage, {localVue: localVueMock, vuetify: vuetifyMock, router: routerMock,
            propsData: {
                errorCode: errorCode,
                errorReason: errorReason
            }
        })

        expect(wrapper.vm.$data.errorDisplayCode).toBe(errorCode)
        expect(wrapper.vm.$data.errorDisplayText).toBe(errorReason)
    })
    it('empty error (default props)', () => {
        const wrapper = shallowMount(errorPage, {localVue: localVueMock, vuetify: vuetifyMock, router: routerMock})

        expect(wrapper.vm.$data.errorDisplayCode).toBe(wrapper.vm.$data.errorDisplayCode)
        expect(wrapper.vm.$data.errorDisplayText).toBe(wrapper.vm.$data.errorDisplayText)
    })
})