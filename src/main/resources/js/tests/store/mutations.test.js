import {mutations} from '../../store/mutations'

let mockSession
let mockUser
let mockState

describe('mutations test', () => {
    beforeEach(() => {
        mockSession = {
            votingId: 100,
            votingKey: 'votingKey'
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
    it('refresh current user roles', () => {
        const updatedUserRoles = ["ADMIN", "USER"]

        mutations.refreshCurrentUserRolesMutations(mockState, updatedUserRoles)

        expect(mockState.currentUser.roles).toStrictEqual([...new Set(mockUser.roles.concat(updatedUserRoles))])
    })
    it('change theme', () => {
        const isDarkValue = mockState.isDarkTheme

        mutations.changeThemeMutation(mockState)

        expect(mockState.isDarkTheme).toBe(!isDarkValue)
    })
    it('add current session voting', () => {
        const newSessionVoting = {
            id: 1,
            votingKey: "newSessionVotingKey"
        }

        mutations.addCurrentSessionVotingMutation(mockState, newSessionVoting)

        expect(mockState.currentStoreVoting).toBe(newSessionVoting)
        expect(mockState.currentSessionVotings.filter(session => session.votingId == newSessionVoting.id).length == 1).toBeTruthy()
        expect(mockState.currentSessionVotings.filter(session => session.votingKey == newSessionVoting.votingKey).length == 1).toBeTruthy()
    })
    it('add current session voting default key', () => {
        const newSessionVoting = {
            id: 1
        }

        mutations.addCurrentSessionVotingMutation(mockState, newSessionVoting)

        expect(mockState.currentStoreVoting).toBe(newSessionVoting)
        expect(mockState.currentSessionVotings.filter(session => session.votingId == newSessionVoting.id).length == 1).toBeTruthy()
        expect(mockState.currentSessionVotings.filter(session => session.votingKey == 'public').length == 1).toBeTruthy()
    })
    it('add current session voting different ID form', () => {
        const newSessionVoting = {
            votingId: 1,
            votingKey: "newSessionVotingKey"
        }

        mutations.addCurrentSessionVotingMutation(mockState, newSessionVoting)

        expect(mockState.currentStoreVoting).toBe(newSessionVoting)
        expect(mockState.currentSessionVotings.filter(session => session.votingId == newSessionVoting.votingId).length == 1).toBeTruthy()
        expect(mockState.currentSessionVotings.filter(session => session.votingKey == newSessionVoting.votingKey).length == 1).toBeTruthy()
    })
    it('delete invalid voting session', () => {
        const invalidSessionVotingId = mockSession.votingId

        mutations.deleteInvalidVotingSessionMutation(mockState, invalidSessionVotingId)

        expect(mockState.currentSessionVotings.includes(mockSession)).toBeFalsy()
    })
})