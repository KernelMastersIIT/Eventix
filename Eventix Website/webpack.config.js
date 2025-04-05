const path = require('path')

module.exports = {
  mode: 'development',
  entry: './javascript/eventlist.js',
  output: {
    path: path.resolve(__dirname, 'pages'),
    filename: 'eventlist-bundle.js'
  },
  watch: true
}