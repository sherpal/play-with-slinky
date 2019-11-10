var merge = require('webpack-merge');
var core = require('./webpack-core.config.js')

var generatedConfig = require("./scalajs.webpack.config.js");
const entries = {};
entries[Object.keys(generatedConfig.entry)[0]] = "scalajs";

module.exports = merge(core, {
  devtool: "cheap-module-eval-source-map",
  entry: entries,
  module: {
    noParse: (content) => {
      return content.endsWith("-fastopt.js");
    },
    rules: [
      {
        test: /\-fastopt.js$/,
        use: [ require.resolve('./fastopt-loader.js') ]
      }
    ]
  },
     devServer: {
     // redirecting calls to http://localhost:8080/play/...
     // to play server under http://localhost:9000/...
       proxy: {
         "/play": {
           target: "http://localhost:9000",
           pathRewrite: {"^/play" : ""}
         }
       },
       historyApiFallback: true
     }
})
