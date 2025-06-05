module.exports = {
  devServer: {

    port: 8088,
    // 跨域
    proxy: {
      '/user': {
        target: 'http://localhost:8888',
        changeOrigin: true,

      },
      '/bill': {
        target: 'http://localhost:8888',
        changeOrigin: true,

      },
      '/consumptionType': {
        target: 'http://localhost:8888',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '',
        },
      },
    },
  },
}
