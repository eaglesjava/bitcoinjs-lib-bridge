let bitcoin = require('bitcoinjs-lib')
let bip39 = require('bip39')
let bip32utils = require('bip32-utils')

var keychain = null

// 随机生成中文助记词，entropy： 长度， wordlist：
function generateMnemonicRandom (entropy, wordlist) {
	// Generate a random mnemonic (uses crypto.randomBytes under the hood), defaults to 128-bits of entropy
	var mnemonic = bip39.generateMnemonic(entropy, null, wordlist)
	return mnemonic
}

// 随机生成中文助记词，entropy： 长度
function generateMnemonicRandomCN (entropy) {
	return generateMnemonicRandom(entropy, bip39.wordlists.chinese_simplified)
}

function mnemonicToSeedHex (mnemonic, password) {
  	return bip39.mnemonicToSeedHex(mnemonic, password)
}

function getAddressBySeedHex (seedHex, index) {
	keychain = keychain || generateMainnetMasterKeychain(seedHex)
	return keychain.derive(index).getAddress()
}

function getMasterXPublicKey (seedHex) {
	keychain = keychain || generateMainnetMasterKeychain(seedHex)
	return keychain.neutered().toBase58()
}

function getAddressByMasterXPublicKey (xpub, index) {
	var node = bitcoin.HDNode.fromBase58(xpub)
	return node.derive(index).getAddress()
}

function generateMainnetMasterKeychain (seedHex) {
	var m = bitcoin.HDNode.fromSeedHex(seedHex)
	return m.derivePath("m/44'/0'/0'/0")
}

function generateTestnetMasterKeychain (seedHex) {
	var m = bitcoin.HDNode.fromSeedHex(seedHex)
	return m.derivePath("m/44'/1'/0'/0")
}

module.exports = {
	generateMnemonicRandom,
	generateMnemonicRandomCN,
	mnemonicToSeedHex,
	getAddressBySeedHex,
	getAddressByMasterXPublicKey,
	getMasterXPublicKey,
	bip39: bip39,
}
