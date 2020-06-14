<template>
    <div class="ma-2">
        <div v-if="userIsLoading">
            <v-progress-circular
                    id="loadCircle"
                    :size="70"
                    :width="7"
                    color="success"
                    indeterminate
            />
        </div>
        <div v-else>
            <v-avatar
                    class="mr-2"
                    size="35">
                <img :src="loadedUser.userPic">
            </v-avatar>
            <span v-if="loadedUser.id != currentUser.id" class="text-button">{{loadedUser.username}}</span>
            <span v-else class="text-button">Your votings</span>
        </div>
    </div>
</template>

<script>
    import user from "../api/user";
    import {mapState} from 'vuex'

    export default {
        name: "userTitle",
        props: {
            userId: {
                required: true,
                type: [Number, String]
            }
        },
        data() {
            return {
                loadedUser: null,
                userIsLoading: true
            }
        },
        async created() {
            await this.loadUserData()
            await console.log(this.currentUser)
            await console.log(this.loadedUser)
        },
        computed: {
            ...mapState(['currentUser'])
        },
        methods: {
            /**
             *
             **/
            async loadUserData() {
                this.userIsLoading = true
                this.loadedUser = await user.getUserData(this.userId)
                this.userIsLoading = false
            },
        }
    }
</script>

<style scoped>

</style>