let bitcoin = require('bitcoinjs-lib')
let bip39 = require('bip39')
let bip32utils = require('bip32-utils')

let BITCOIN_MAINNET_PATH = "m/44'/0'/0'/0"
let BITCOIN_TESTNET_PATH = "m/44'/1'/0'/0"

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

function validateMnemonic(mnemonic) {
	return bip39.validateMnemonic(mnemonic, bip39.wordlists.chinese_simplified) || bip39.validateMnemonic(mnemonic, bip39.wordlists.english)
}

function validateAddress(address) {
    try {
        bitcoin.address.toOutputScript(address)
        return true
    } catch (e) {
        return false
    }
}

function getBitcoinAddressBySeedHex (seedHex, index) {
	var bitcoinKeyChain = generateBitcoinMainnetMasterKeychain(seedHex)
	return bitcoinKeyChain.derive(index).getAddress()
}

function getBitcoinMasterXPublicKey (seedHex) {
	var keychain = generateBitcoinMainnetMasterKeychain(seedHex)
	return keychain.neutered().toBase58()
}

function getBitcoinAddressByMasterXPublicKey (xpub, index) {
	var node = bitcoin.HDNode.fromBase58(xpub)
	return node.derive(index).getAddress()
}

function generateMainnetMasterKeychain (seedHex) {
	var m = bitcoin.HDNode.fromSeedHex(seedHex)
	return m
}

function generateBitcoinMainnetMasterKeychain (seedHex) {
	return generateMainnetMasterKeychain(seedHex).derivePath(BITCOIN_MAINNET_PATH)
}

function generateBitcoinTestnetMasterKeychain (seedHex) {
	return generateMainnetMasterKeychain(seedHex).derivePath(BITCOIN_TESTNET_PATH)
}

function buildTransaction(seedHex, datas) {
	var keychain = generateBitcoinMainnetMasterKeychain(seedHex)
	var data = JSON.parse(datas)

	var txb = new bitcoin.TransactionBuilder()

	var inputs = data["inputs"]
	var ecPairs = new Array()
	for (var i = 0; i < inputs.length; i++) {
		var input = inputs[i]
		txb.addInput(input["txHash"], input["index"])
		ecPairs[i] = keychain.derive(input["bip39Index"])
	}

	var outputs = data["outputs"]
	for (var i = 0; i < outputs.length; i++) {
		var output = outputs[i]
		txb.addOutput(output["address"], output["amount"])
	}

	for (var i = ecPairs.length - 1; i >= 0; i--) {
		txb.sign(i, ecPairs[i])
	}

	return txb.build().toHex()
}

module.exports = {
	generateMnemonicRandom,
	generateMnemonicRandomCN,
	mnemonicToSeedHex,
	validateMnemonic,
    validateAddress,
	getBitcoinAddressBySeedHex,
	getBitcoinAddressByMasterXPublicKey,
	getBitcoinMasterXPublicKey,
	buildTransaction,
	bip39: bip39,
}
