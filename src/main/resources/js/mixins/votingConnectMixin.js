import {mapActions, mapGetters} from "vuex";
import api from "../api/server";
import {connectWebsocket, disconnectWebsocket} from "../utils/websocket";
import router from "../router/router"
import routesNames from "../router/routesNames";

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
            const response = await api.getOne(votingId, votingKey).catch(err => {
                if(err.status == 403) {
                    router.push({
                        name: routesNames.PROTECTED_VOTING_ACCESS,
                        params: {
                            votingId: this.votingId
                        }
                    })
                } else {
                    throw err;
                }
            })
            const data = await response.json()
            this.mixinVoting = data
        },
    }
}