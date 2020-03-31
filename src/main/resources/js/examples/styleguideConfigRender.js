import vuetifyPlugin from "../../plugins/vuetifyPlugin";

export default previewComponent => {
    return {
        vuetify: vuetifyPlugin,
        render(createElement) {
            return createElement(
                'v-app',
                {
                    props: {
                        id: 'v-app'
                    }
                },
                [createElement(Object.assign(previewComponent))]
            )
        }
    }
}