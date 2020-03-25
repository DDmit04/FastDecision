<template>
    <div>
        <v-tooltip right>
            <template v-slot:activator="{ on }">
                <v-icon v-on="on">{{icon}}</v-icon>
            </template>
            <span>{{tooltipMessage}}</span>
        </v-tooltip>
    </div>
</template>

<script>
    export default {
        name: "tooltip",
        props: {
            icon: {
                required: true,
                type: String
            },
            tooltipMessage: {
                required: true,
                type: String
            }
        },
        data() {
            return {
                on: false
            }
        }
    }
</script>

<style scoped>

</style>