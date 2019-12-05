<template>
    <div>
        <v-toolbar>
            <v-toolbar-title @click="goToMain" class="font-italic">Fast Decision</v-toolbar-title>
        </v-toolbar>
        <v-app>
            <router-view/>
        </v-app>
    </div>
</template>
<script>

    export default {
        data() {
            return {}
        },
        methods: {
            goToMain() {
                this.$router.push('/')
            }
        }
    }
</script>
<style>

</style>