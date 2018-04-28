var ethereumjsUtil = require('ethereumjs-util');
var hdkey = require('ethereumjs-wallet/hdkey');
var EthereumTx = require('ethereumjs-tx');

var Web3 = require('web3');
var web3 = new Web3();

var util = require('./util');

var icap = require('ethereumjs-icap');

var keythereum = require('keythereum');

// jaxx path
var ETHEREUM_MAINNET_PATH = "m/44'/60'/0'/0/0";
var ETHEREUM_TESTNET_PATH = "m/44'/1'/0'/0";

var bip39 = require('bip39');

/**
 * Mnemonic to seed
 * @param {String} mnemonic
 * @return {Buffer} seed
 */
function mnemonicToSeed(mnemonic) {
	var seed = bip39.mnemonicToSeed(mnemonic);
	return seed;
}

/**
 * Seed to address
 * @param {Buffer} seed
 * @return {String} address
 */
function seedToAddress(seed) {
	var hd = hdkey.fromMasterSeed(seed);
	var wallet = hd.derivePath(ETHEREUM_MAINNET_PATH).getWallet();
	return ethereumjsUtil.bufferToHex(wallet.getAddress());
}

/**
 * Seed to checksum address
 * @param {Buffer} seed
 * @return {String} checksum address
 */
function seedToChecksumAddress(seed) {
    var hd = hdkey.fromMasterSeed(seed);
    var wallet = hd.derivePath(ETHEREUM_MAINNET_PATH).getWallet();
    return wallet.getChecksumAddressString();
}

/**
 * Hex-encoded seed to checksum address
 * @param {String} seedHex: Hex-encoded seed
 * @return {String} checksum address
 */
function seedHexToAddress(seedHex) {
	var seed = Buffer.from(seedHex, 'hex');
	return seedToChecksumAddress(seed);
}

/**
 * Hex-encoded seed to [publicKey, address]
 * @param {String} seedHex: Hex-encoded seed
 * @return {Object} [String, String]
 */
function seedHexToPubAddr(seedHex) {
    var seed = Buffer.from(seedHex, 'hex');
    var hd = hdkey.fromMasterSeed(seed);
    var wallet = hd.derivePath(ETHEREUM_MAINNET_PATH).getWallet();
    var publicKey = wallet.getPublicKey().toString('hex');
    var address = wallet.getChecksumAddressString();
    return [publicKey, address];
}

/**
 * Hex-encoded seed to privateKey
 * @param {String} seedHex: Hex-encoded seed
 * @return {Buffer} privateKey
 */
function seedHexToPrivate(seedHex) {
    var seed = Buffer.from(seedHex, 'hex');
    var hd = hdkey.fromMasterSeed(seed);
    var wallet = hd.derivePath(ETHEREUM_MAINNET_PATH).getWallet();
    return wallet.getPrivateKey();
}

/**
 * verify address
 * @param {String} address
 * @return {Boolean}
 */
function isValidAddress(address) {
	return ethereumjsUtil.isValidAddress(address)
}

/**
 * verify checksum address
 * @param {String} address
 * @return {Boolean}
 */
function isValidChecksumAddress(address) {
	return ethereumjsUtil.isValidChecksumAddress(address)
}

/**
 * Checks if the given string is an address
 *
 * @method isAddress
 * @param {String} address the given HEX adress
 * @return {Boolean}
 */
function isAddress(address) {
    return web3.isAddress(address)
}

/**
 * iban to address
 * @param {String} iban
 * @return {String} address
 */
function ibanToAddress(iban) {
    return ethereumjsUtil.toChecksumAddress(icap.toAddress(iban))
}

/**
 * address to iban
 * @param {String} address
 * @return {String} iban
 */
function addressToIban(address) {
    return icap.fromAddress(address, false, true)
}

/**
 * build a eth transaction
 * @param {Number|String} amountWei
 * @param {String} addressTo
 * @param {Number|String} nonce
 * @param {Number|String} gasPrice
 * @param {Number|String} gasLimit
 * @param {String} customData
 * @param {String} privateKey: Hex-encoded
 * @return {Array} [txid, serializedTx]
 */
function buildEthTransaction(privateKey, amountWei, addressTo, nonce, gasPrice, gasLimit, customData) {
    privateKey = Buffer.from(privateKey, 'hex');
    var transaction = new EthereumTx({
        nonce: web3.toHex(nonce),
        gasPrice: web3.toHex(gasPrice),
        gasLimit: web3.toHex(gasLimit),
        to: addressTo,
        value: web3.toHex(amountWei),
        data: (customData && customData.length > 0) ? customData : '0x',
        chainId: 1
    });
    transaction.sign(privateKey);
    var txid = ('0x' + transaction.hash().toString('hex'));
    var serializedTx = ('0x' + transaction.serialize().toString('hex'));

    console.log(JSON.stringify(transaction));

    return [txid, serializedTx];
}

/**
 * build a eth transaction by Hex-encoded seed
 * @param {String} seedHex: Hex-encoded seed
 * @param {Number|String} amountWei
 * @param {String} addressTo
 * @param {Number|String} nonce
 * @param {Number|String} gasPrice
 * @param {Number|String} gasLimit
 * @param {String} customData
 * @return {Array} [txid, serializedTx]
 */
function buildEthTxBySeedHex(seedHex, amountWei, addressTo, nonce, gasPrice, gasLimit, customData) {
    var privateKey = seedHexToPrivate(seedHex);
    return buildEthTransaction(privateKey, amountWei, addressTo, nonce, gasPrice, gasLimit, customData)
}

/**
 * build a token transaction
 * @param {String} privateKey: Hex-encoded
 * @param {Number|String} amountWei
 * @param {String} addressTo
 * @param {Number|String} nonce
 * @param {Number|String} gasPrice
 * @param {Number|String} gasLimit
 * @param {String} contractAddress
 * @return {Array} [txid, serializedTx]
 */
function buildTokenTransaction(amountWei, addressTo, nonce, contractAddress, gasLimit, gasPrice, privateKey) {
    privateKey = Buffer.from(privateKey, 'hex');
    var data = util.createTokenData(web3, amountWei, addressTo);
    //  console.log('Data', data);
    var raw = util.mapEthTransaction(web3, contractAddress, '0', nonce, gasPrice, gasLimit, data);
    // console.log(raw);
    var transaction = new EthereumTx(raw);
    //console.log(transaction);
    transaction.sign(privateKey);
    var serializedTx = ('0x' + transaction.serialize().toString('hex'));
    var txid = ('0x' + transaction.hash().toString('hex'));
    return [txid, serializedTx];
}

/**
 * build a token transaction by Hex-encoded seed
 * @param {String} seedHex: Hex-encoded seed
 * @param {Number|String} amountWei
 * @param {String} addressTo
 * @param {Number|String} nonce
 * @param {Number|String} gasPrice
 * @param {Number|String} gasLimit
 * @param {String} contractAddress
 * @return {Array} [txid, serializedTx]
 */
function buildTokenTxBySeedHex(seedHex, amountWei, addressTo, nonce, contractAddress, gasLimit, gasPrice) {
    var privateKey = seedHexToPrivate(seedHex);
    return buildTokenTransaction(amountWei, addressTo, nonce, contractAddress, gasLimit, gasPrice, privateKey)
}

/**
 * build a eos map transaction
 * @param {String} privateKey: Hex-encoded
 * @param {String} eosPublicKey
 * @param {Number|String} nonce
 * @param {Number|String} gasPrice
 * @param {Number|String} gasLimit
 * @param {String} contractAddress
 * @return {Array} [txid, serializedTx]
 */
function buildMapEosTransaction(eosPublicKey, nonce, contractAddress, gasLimit, gasPrice, privateKey) {
    privateKey = Buffer.from(privateKey, 'hex');
    var data = util.getTxData('register', ['string'], [eosPublicKey]);
    //  console.log('Data', data);
    var raw = util.mapEthTransaction(web3, contractAddress, '0', nonce, gasPrice, gasLimit, data);
    // console.log(raw);
    var transaction = new EthereumTx(raw);
    //console.log(transaction);
    transaction.sign(privateKey);
    var serializedTx = ('0x' + transaction.serialize().toString('hex'));
    var txid = ('0x' + transaction.hash().toString('hex'));
    return [txid, serializedTx];
}

/**
 * build a eos map transaction by Hex-encoded seed
 * @param {String} seedHex: Hex-encoded seed
 * @param {String} eosPublicKey
 * @param {Number|String} nonce
 * @param {Number|String} gasPrice
 * @param {Number|String} gasLimit
 * @param {String} contractAddress
 * @return {Array} [txid, serializedTx]
 */
function buildMapEosTxBySeedHex(seedHex, eosPublicKey, nonce, contractAddress, gasLimit, gasPrice) {
    var privateKey = seedHexToPrivate(seedHex);
    return buildMapEosTransaction(eosPublicKey, nonce, contractAddress, gasLimit, gasPrice, privateKey)
}

/**
 * Generate eos keyPair.
 * @param {Function} cb is a Callback function, function params is {publicKey, privateKey}.
 */
function generateEosKeyPair(cb) {
    util.generateEosKeyPair(cb);
}

/**
 * Recover plaintext private key from secret-storage key object.
 * @param {string} password.
 * @param {string} keystoreContent: keystore file content.
 * @return {Buffer} Plaintext private key.
 */
function getPrivateKeyFromKeystore (password, keystoreContent) {
    var keyObject = JSON.parse(keystoreContent);
    return keythereum.recover(password, keyObject);
}

/**
 * Returns the ethereum public key of a given private key
 * @param {Buffer} privateKey A private key must be 256 bits wide
 * @return {Buffer}
 */
function privateToPublic(privateKey) {
    return ethereumjsUtil.privateToPublic(privateKey);
}

/**
 * Returns the ethereum address of a given public key.
 * Accepts "Ethereum public keys" and SEC1 encoded keys.
 * @param {Buffer} pubKey The two points of an uncompressed key, unless sanitize is enabled
 * @param {Boolean} [sanitize=false] Accept public keys in other formats
 * @return {Buffer}
 */
function publicToAddress(pubKey, sanitize) {
    return ethereumjsUtil.publicToAddress(pubKey, sanitize);
}

/**
 * Returns the ethereum address of a given private key
 * @param {Buffer} privateKey A private key must be 256 bits wide
 * @return {Buffer}
 */
function privateToAddress(privateKey) {
    return ethereumjsUtil.privateToAddress(privateKey);
}

/**
 * Get privateKey&publicKey&address from secret-storage keystore file.
 * @param {string} password.
 * @param {string} keystoreContent: keystore file content.
 * @return {Array} [privateKey, publicKey, address].
 */
function getKeyPairAddrFromKeystore (password, keystoreContent) {
    var privateKey = getPrivateKeyFromKeystore(password, keystoreContent);
    var publicKey = privateToPublic(privateKey);
    var address = "0x" + privateToAddress(privateKey).toString('hex');
    address = ethereumjsUtil.toChecksumAddress(address);

    return [privateKey.toString('hex'), publicKey.toString('hex'), address]
}

/**
 * Get publicKey&address from privateKey.
 * @param {String|Buffer} privateKey ECDSA private key.
 * @return {Array} [publicKey, address].
 */
function getPubAddrFromPrivate(privateKey) {
    var privateKeyBuffer = keythereum.str2buf(privateKey);
    if (privateKeyBuffer.length < 32) {
        privateKeyBuffer = Buffer.concat([
            Buffer.alloc(32 - privateKeyBuffer.length, 0),
            privateKeyBuffer
        ]);
    }

    var publicKey = privateToPublic(privateKeyBuffer);
    var address = "0x" + privateToAddress(privateKeyBuffer).toString('hex');
    address = ethereumjsUtil.toChecksumAddress(address);

    return [publicKey.toString('hex'), address]
}


module.exports = {
    mnemonicToSeed: mnemonicToSeed,
    seedToAddress: seedToAddress,
    seedToChecksumAddress: seedToChecksumAddress,
    seedHexToAddress: seedHexToAddress,
    seedHexToPubAddr: seedHexToPubAddr,
    seedHexToPrivate: seedHexToPrivate,
    isValidAddress: isValidAddress,
    isValidChecksumAddress: isValidChecksumAddress,
    isAddress: isAddress,
    buildEthTransaction: buildEthTransaction,
    buildEthTxBySeedHex: buildEthTxBySeedHex,
    buildTokenTransaction: buildTokenTransaction,
    buildTokenTxBySeedHex: buildTokenTxBySeedHex,
    buildMapEosTransaction: buildMapEosTransaction,
    buildMapEosTxBySeedHex: buildMapEosTxBySeedHex,
    generateEosKeyPair: generateEosKeyPair,
    ibanToAddress: ibanToAddress,
    addressToIban: addressToIban,
    getPrivateKeyFromKeystore: getPrivateKeyFromKeystore,
    privateToPublic: privateToPublic,
    publicToAddress: publicToAddress,
    privateToAddress: privateToAddress,
    getKeyPairAddrFromKeystore: getKeyPairAddrFromKeystore,
    getPubAddrFromPrivate: getPubAddrFromPrivate,
};

