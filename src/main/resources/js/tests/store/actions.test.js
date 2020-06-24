import {actions} from '../../store/actions'
import serverValidation from "../../api/serverValidation"
import server from "../../api/server"
import router from "../../router/router"
import routesNames from "../../router/routesNames";

let mockSession
let mockNewSession
let mockUser
let mockState
let commit = jest.fn()
let getters = {
    getVotingById() {
    }
}

describe('actions test', () => {
    beforeEach(() => {
        mockSession = {
            votingId: 100,
            votingKey: 'votingKey'
        }
        mockNewSession = {
            votingId: 101,
            votingKey: 'newVotingKey'
        }
        mockUser = {
            id: 1,
            roles: ['USER']
        }
        mockState = {
            currentUser: mockUser,
            isDarkTheme: false,
            currentStoreVoting: null,
            currentSessionVotings: [mockSession],
            version: ''
        }
    })
    it('check current session voting old valid session', async () => {

        getters.getVotingById = jest.fn(() => mockSession)
        serverValidation.validateKey = jest.fn(() => {
            return {keyIsValid: true}
        })

        await actions.checkCurrentSessionVotingAction({commit, getters}, mockSession)

        expect(commit).not.toBeCalled()
    })
    it('check current session voting new valid session', async () => {

        getters.getVotingById = jest.fn(() => null)
        serverValidation.validateKey = jest.fn(() => {
            return {keyIsValid: true}
        })

        await actions.checkCurrentSessionVotingAction({commit, getters}, mockNewSession)

        expect(commit).toBeCalledWith("addCurrentSessionVotingMutation", mockNewSession)
    })
    it('check current session voting old invalid session', async () => {

        getters.getVotingById = jest.fn(() => mockSession)
        serverValidation.validateKey = jest.fn(() => {
            return {keyIsValid: false}
        })

        await actions.checkCurrentSessionVotingAction({commit, getters}, mockNewSession)

        expect(commit).toBeCalledWith("deleteInvalidVotingSessionMutation", mockSession)
        expect(commit).toBeCalledWith("addCurrentSessionVotingMutation", mockNewSession)
    })
    it('add new voting', async () => {
        const newVoting = {id: null}
        const newServerVoting = {id: 1}
        router.push = jest.fn()
        server.addVoting = jest.fn(() => newServerVoting)

        await actions.addNewVotingAction({commit}, newVoting)

        expect(server.addVoting).toBeCalledWith(newVoting)
        expect(commit).toBeCalledWith("addCurrentSessionVotingMutation", newServerVoting)
    })
    it('add new voting failure with array of errors', async () => {
        const newVoting = {id: null}
        const errorObject = {
            status: 400,
            data: {
                errors: ['someError1', 'someError2']
            }
        }
        router.push = jest.fn()
        server.addVoting = jest.fn(() => {
            throw errorObject
        })

        await actions.addNewVotingAction({commit}, newVoting)

        expect(router.push).toBeCalledWith({
            name: routesNames.ERROR_PAGE,
            params: {
                errorCode: errorObject.status,
                errorReason: errorObject.data.errors.join(", ")
            }
        })
    })
    it('add new voting failure with error message', async () => {
        const errorObject = {
            status: 400,
            data: {
                errors: [],
                message: "errorMessage"
            }
        }
        const newVoting = {id: null}
        router.push = jest.fn()
        server.addVoting = jest.fn(() => {
            throw errorObject
        })

        await actions.addNewVotingAction({commit}, newVoting)

        expect(router.push).toBeCalledWith({
            name: routesNames.ERROR_PAGE,
            params: {
                errorCode: errorObject.status,
                errorReason: errorObject.data.message
            }
        })
    })
})