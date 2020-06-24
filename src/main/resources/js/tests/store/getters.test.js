import {getters} from '../../store/getters'

let mockSession
let mockState

describe('mutations test', () => {
    beforeEach(() => {
        mockSession = {
            votingId: 100,
            votingKey: 'votingKey'
        }
        mockState = {
            isDarkTheme: false,
            currentStoreVoting: null,
            currentSessionVotings: [mockSession],
            version: ''
        }
    })
    it('get existing voting session', () => {
        expect(getters.getVotingById(mockState)(mockSession.votingId)).toStrictEqual(mockSession)
    })
    it('get not existing voting session ', () => {
        expect(getters.getVotingById(mockState)(999)).toBeUndefined()
    })
})