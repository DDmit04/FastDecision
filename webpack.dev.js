const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
    mode: 'development',
    devtool: 'source-map',
    devServer: {
        contentBase: './dist',
        compress: true,
        port: 8002,
        allowedHosts: [
            'localhost:9002'
        ],
        stats: 'minimal',
        clientLogLevel: 'error',
    },
});