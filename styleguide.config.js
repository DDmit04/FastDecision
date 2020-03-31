const path = require('path')

module.exports = {
    webpackConfig: require('./webpack.common'),
    styleguideDir: "src/docs/styleguide",
    require: [
        path.join(__dirname, 'src/main/resources/js/examples/styleguideConfigGlobal.js'),
        path.join(__dirname, 'src/main/resources/js/examples/styleguideStyleConfig.styles.scss')
    ],
    renderRootJsx: path.join(__dirname, 'src/main/resources/js/examples/styleguideConfigRender.js'),
    sections: [
        {
            name: 'Pages',
            sections: [
                {
                    name: 'Core',
                    components: "src/main/resources/js/pages/*.vue"
                },
                {
                    name: 'Voting',
                    components: "src/main/resources/js/pages/voting/*.vue"

                },
                {
                    name: "Charts",
                    components: "src/main/resources/js/pages/charts/*.vue"
                }
            ]
        },
        {
            name: 'Components',
            sections: [
                {
                    name: 'Core',
                    components: "src/main/resources/js/components/*.vue"
                },
                {
                    name: 'Charts',
                    components: "src/main/resources/js/components/charts/*.vue"
                },
                {
                    name: 'Modal',
                    components: "src/main/resources/js/components/modal/*.vue"
                }
            ]
        },
        {
            name: 'Mixins',
            components: "src/main/resources/js/mixins/*.js"
        }
    ]
}