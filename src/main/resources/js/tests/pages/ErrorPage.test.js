import {localVue, setupedRouter, setupedVuetify, shallowMount} from '../baseTest'
import errorPage from "../../pages/errorPage";

let vuetify = setupedVuetify
let router = setupedRouter

describe('testing error page', () => {
    it('some error (with props)', () => {
        let errorReason = 'forbidden'
        let errorCode = 403
        const wrapper = shallowMount(errorPage, {localVue, vuetify, router,
            propsData: {
                errorCode: errorCode,
                errorReason: errorReason
            }
        })

        expect(wrapper.vm.$data.errorDisplayCode).toBe(errorCode)
        expect(wrapper.vm.$data.errorDisplayText).toBe(errorReason)
    })
    it('empty error (default props)', () => {
        const wrapper = shallowMount(errorPage, {localVue, vuetify, router})

        expect(wrapper.vm.$data.errorDisplayCode).toBe(wrapper.vm.$data.errorDisplayCode)
        expect(wrapper.vm.$data.errorDisplayText).toBe(wrapper.vm.$data.errorDisplayText)
    })
})