<template>
    <div>
        <v-layout justify-center class="mt-3 display-3 font-italic">
            Page not found!
        </v-layout>
        <v-layout justify-center class="mt-3 display-1 font-italic">
            <router-link :to="{ name: 'main' }">Go to main</router-link>
        </v-layout>
    </div>
</template>

<script>
    export default {
        name: "pageNotFound"
    }
</script>

<style scoped>

</style>