<template>
    <div>
        <v-toolbar>
            <v-toolbar-title class="font-italic"><router-link :to="{ path: '/' }">Fast Decision</router-link></v-toolbar-title>
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
    }
</script>
<style>
    a { text-decoration: none }
</style>