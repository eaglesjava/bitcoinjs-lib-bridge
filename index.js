let bitcoin = require('bitcoinjs-lib')
let bip39 = require('bip39')

let BITCOIN_MAINNET_PATH = "m/44'/0'/0'/0"
let BITCOIN_TESTNET_PATH = "m/44'/1'/0'/0"

// 随机生成中文助记词，entropy： 长度， wordlist：
function generateMnemonicRandom (entropy, wordlist) {
	// Generate a random mnemonic (uses crypto.randomBytes under the hood), defaults to 128-bits of entropy
	let mnemonic = bip39.generateMnemonic(entropy, null, wordlist)
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
        return falseR
    }
}

function getBitcoinAddressBySeedHex (seedHex, index) {
	let bitcoinKeyChain = generateBitcoinMainnetMasterKeychain(seedHex)
	return bitcoinKeyChain.derive(index).getAddress()
}

function getBitcoinMasterXPublicKey (seedHex) {
	let keychain = generateBitcoinMainnetMasterKeychain(seedHex)
	return keychain.neutered().toBase58()
}

function getBitcoinAddressByMasterXPublicKey (xpub, index) {
	let node = bitcoin.HDNode.fromBase58(xpub)
	return node.derive(index).getAddress()
}

function generateMainnetMasterKeychain (seedHex) {
	let m = bitcoin.HDNode.fromSeedHex(seedHex)
	return m
}

function generateBitcoinMainnetMasterKeychain (seedHex) {
	return generateMainnetMasterKeychain(seedHex).derivePath(BITCOIN_MAINNET_PATH)
}

function generateBitcoinTestnetMasterKeychain (seedHex) {
	return generateMainnetMasterKeychain(seedHex).derivePath(BITCOIN_TESTNET_PATH)
}

function buildTransaction(seedHex, data) {
    data = JSON.parse(data)
	let keychain = generateBitcoinMainnetMasterKeychain(seedHex)

	let txb = new bitcoin.TransactionBuilder()

	let inputs = data["inputs"]
	let ecPairs = new Array()
	for (let i = 0; i < inputs.length; i++) {
		let input = inputs[i]
		txb.addInput(input["txHash"], input["index"])
		ecPairs[i] = keychain.derive(input["bip39Index"])
	}

	let outputs = data["outputs"]
	for (let i = 0; i < outputs.length; i++) {
		let output = outputs[i]
		txb.addOutput(output["address"], output["amount"])
	}

	for (let i = ecPairs.length - 1; i >= 0; i--) {
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
