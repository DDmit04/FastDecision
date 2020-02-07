import {mapActions, mapGetters} from "vuex";
import api from "../api/server";
import {connectWebsocket, disconnectWebsocket} from "../utils/websocket";

export default {
    data() {
        return {
            mixinVotingKey: 'public',
            mixinVoting: null
        }
    },
    destroyed() {
        disconnectWebsocket()
    },
    computed: {
        ...mapGetters(["getVotingById"])
    },
    methods: {
        ...mapActions(["checkCurrentSessionVotingAction"]),
        async mixinConnectToVoting(votingId, votingKey) {
            await this.mixinCheckVotingSession(votingId, votingKey)
            await this.mixinGetVoting(votingId, this.mixinVotingKey)
            await connectWebsocket(votingId, this.mixinVotingKey)
        },
        async mixinCheckVotingSession(votingId, votingKey) {
            await this.checkCurrentSessionVotingAction({
                votingId: votingId,
                votingKey: votingKey
            })
            let localStoreVoting = await this.getVotingById(votingId)
            this.mixinVotingKey = localStoreVoting.votingKey
        },
        async mixinGetVoting(votingId, votingKey) {
            const response = await api.getOne(votingId, votingKey)
            const data = await response.json()
            this.mixinVoting = data
        },
    }
}