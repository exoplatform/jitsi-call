const path = require("path");
const merge = require("webpack-merge");
const webpackCommonConfig = require("./webpack.common.js");

// the display name of the war
const app = "jitsi";

const config = merge(webpackCommonConfig, {
  output: {
    path: path.resolve(__dirname, `../../../target/frontend/public/${app}/`)
  }
});

module.exports = config;
