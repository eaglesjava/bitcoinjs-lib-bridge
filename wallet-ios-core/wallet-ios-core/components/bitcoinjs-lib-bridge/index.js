let bitcoin = require('bitcoinjs-lib')
let bip39 = require('bip39')

function generateMnemonicRandom (entropy, wordlist) {
	// Generate a random mnemonic (uses crypto.randomBytes under the hood), defaults to 128-bits of entropy
	var mnemonic = bip39.generateMnemonic(entropy, null, wordlist)
	return mnemonic
}

function generateMnemonicRandomCN (entropy) {
	return generateMnemonicRandom(entropy, bip39.wordlists.chinese_simplified)
}

module.exports = {
	generateMnemonicRandom,
	generateMnemonicRandomCN,
	bip39: bip39
}
