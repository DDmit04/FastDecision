import {flushPromises, localVueMock, mount, setupedRouterMock, setupedVuetifyMock} from '../baseTest'
import dataRevealer from "../../components/dataRevealer";

let routerMock = setupedRouterMock
let vuetifyMock = setupedVuetifyMock

let testDataToReveal = 'dataToReveal'

describe('data reviler test', () => {
    it('reveal data', async () => {
        const wrapper = mount(dataRevealer, {vuetify: vuetifyMock, router: routerMock, localVue: localVueMock,
            propsData: {dataToReveal: testDataToReveal,}
        })

        wrapper.find("#revealDataBtn").trigger("click")
        await flushPromises()

        expect(wrapper.find("#hidenData").exists()).toBeFalsy()
        expect(wrapper.find("#reveledData").exists()).toBeTruthy()
        expect(wrapper.find("#data").exists()).toBeTruthy()
        expect(wrapper.find("#data").element.value).toBe(testDataToReveal)
    })
    it('hide data', async () => {
        const wrapper = mount(dataRevealer, {vuetify: vuetifyMock, router: routerMock, localVue: localVueMock,
            propsData: {dataToReveal: testDataToReveal,},
            data() {
                return {dataIsReviled: true}
            }
        })
        wrapper.find("#hideDataBtn").trigger("click")
        await flushPromises()

        expect(wrapper.find("#hidenData").exists()).toBeTruthy()
        expect(wrapper.find("#reveledData").exists()).toBeFalsy()
        expect(wrapper.find("#data").exists()).toBeFalsy()
    })
})