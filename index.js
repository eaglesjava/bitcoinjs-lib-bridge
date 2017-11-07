let bitcoin = require('bitcoinjs-lib')

// your code here
function myFunction () {
	return bitcoin.ECPair.makeRandom().toWIF()
}

module.exports = {
	myFunction
}
