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
    /**
     * Component wrapper for <v-tooltip>
     * @displayName Tooltip wrapper component
     * @author Dmitrochenkov Daniil
     * @version 1.0
     */
    export default {
        name: "tooltip",
        props: {
            /** Custom tooltip icon */
            icon: {
                required: true,
                type: String,
                default: ""
            },
            /** Custom tooltip message */
            tooltipMessage: {
                required: true,
                type: String,
                default: "No message"
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