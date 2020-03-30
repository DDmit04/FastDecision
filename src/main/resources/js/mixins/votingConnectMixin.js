import {mapActions, mapGetters} from "vuex";
import api from "../api/server";
import {connectWebsocket, disconnectWebsocket} from "../utils/websocket";
import router from "../router/router"
import routesNames from "../router/routesNames";

/**
 * Mixin to fetch voting and connect voting websocket
 * @displayName Voting connect mixin
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
export default {
    data() {
        return {
            mixinVotingKey: 'public',
            mixinVoting: null,
            mixinVotingId: null
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
        /**
         * @public
         * Fetch voting, check store voting session and connect websocket
         * @param{Number} votingId voting ID to connect
         * @param{String} votingKey voting key to connect
         */
        async mixinConnectToVoting(votingId, votingKey) {
            this.mixinVotingId = votingId
            await this.mixinCheckVotingSession(votingId, votingKey)
            await this.mixinGetVoting(votingId, this.mixinVotingKey)
            await connectWebsocket(votingId, this.mixinVotingKey)
        },
        /**
         * @public
         * Check store for current voting session (add or update voting session data)
         * @param{Number} votingId voting ID for add or update voting session data
         * @param{String} votingKey voting key for add or update voting session data
         */
        async mixinCheckVotingSession(votingId, votingKey) {
            await this.checkCurrentSessionVotingAction({
                votingId: votingId,
                votingKey: votingKey
            })
            let localStoreVoting = await this.getVotingById(votingId)
            this.mixinVotingKey = localStoreVoting.votingKey
        },
        /**
         * @public
         * Fetch voting from server in case of 403 http error redirect to voting access page
         * @param{Number} votingId voting ID for fetch
         * @param{String} votingKey voting key to fetch
         */
        async mixinGetVoting(votingId, votingKey) {
            let data = null
            try {
                data = await api.getOne(votingId, votingKey)
            } catch (err) {
                if (err.status == 403) {
                    router.push({
                        name: routesNames.PROTECTED_VOTING_ACCESS,
                        params: {
                            votingId: this.mixinVotingId
                        }
                    })
                } else {
                    throw err;
                }
            }
            this.mixinVoting = data
        },
    }
}