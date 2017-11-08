let bitcoin = require('bitcoinjs-lib')
let bip39 = require('bip39')

// your code here
function myFunction () {
	return bitcoin.ECPair.makeRandom().toWIF()
}

function bip39Test () {
	// Generate a random mnemonic (uses crypto.randomBytes under the hood), defaults to 128-bits of entropy
	var mnemonic = bip39.generateMnemonic()
	return mnemonic
}

function paramsTest (num) {
	return num + 5
}

module.exports = {
	myFunction,
	bip39Test,
	paramsTest
}
