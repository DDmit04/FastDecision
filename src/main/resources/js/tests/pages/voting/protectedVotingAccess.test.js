import {flushPromises, localVueMock, mount, setupedRouterMock, setupedVuetifyMock} from '../../baseTest'
import protectedVotintgAccess from "../../../pages/voting/protectedVotintgAccess";
import routesNames from "../../../router/routesNames";

let routerMock = setupedRouterMock
let vuetifyMock = setupedVuetifyMock

describe('test voting access', () => {
    it('test access redirect', async () => {
        const wrapper = mount(protectedVotintgAccess, {vuetify: vuetifyMock, router: routerMock, localVue: localVueMock,
            propsData: {votingId: 1}
        })

        const votingKeyInput = wrapper.find("#votingAccessKey")
        votingKeyInput.element.value = "key"
        votingKeyInput.trigger("input")
        await flushPromises()

        wrapper.find("#accessVotingBtn").trigger("click")
        await flushPromises()

        expect(wrapper.vm.$route.name).toBe(routesNames.CURRENT_VOTING)
        expect(wrapper.vm.$route.path).toBe("/1/vote")
        expect(wrapper.vm.$route.params.votingId).toBe(1)
        expect(wrapper.vm.$route.query.votingKey).toBe("key")
    })
})