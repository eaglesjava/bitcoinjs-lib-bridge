let bitcoin = require('bitcoinjs-lib')
let bip39 = require('bip39')

function generateMnemonicRandom () {
	// Generate a random mnemonic (uses crypto.randomBytes under the hood), defaults to 128-bits of entropy
	var mnemonic = bip39.generateMnemonic()
	return mnemonic
}

function exceptionTest () {
	throw new Error('Non 5-bit word')
}

module.exports = {
	generateMnemonicRandom,
	exceptionTest
}
