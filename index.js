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

var secp256k1 = require('secp256k1');
var bip39 = require('bip39');

function mnemonicToSeed(mnemonic) {
	var seed = bip39.mnemonicToSeed(mnemonic);
	return seed;
}

function seedToAddress(seed) {
	var hd = hdkey.fromMasterSeed(seed);
	var wallet = hd.derivePath(ETHEREUM_MAINNET_PATH).getWallet();
	return ethereumjsUtil.bufferToHex(wallet.getAddress());
}

function seedToChecksumAddress(seed) {
    var hd = hdkey.fromMasterSeed(seed);
    var wallet = hd.derivePath(ETHEREUM_MAINNET_PATH).getWallet();
    return wallet.getChecksumAddressString();
}

function seedHexToAddress(seedHex) {
	var seed = Buffer.from(seedHex, 'hex');
	return seedToChecksumAddress(seed);
}

function isValidAddress(address) {
	return ethereumjsUtil.isValidAddress(address)
}

function isValidChecksumAddress(address) {
	return ethereumjsUtil.isValidChecksumAddress(address)
}

function ibanToAddress(iban) {
    return icap.toAddress(iban)
}

function addressToIban(address) {
    return icap.fromAddress(address, false, true)
}

function buildEthTransaction(amountWei, addressTo, nonce, privateKey, gasPrice, gasLimit, customData) {
    var transaction = new EthereumTx({
        nonce: web3.toHex(nonce),
        gasPrice: web3.toHex(gasPrice),
        gasLimit: web3.toHex(gasLimit),
        to: addressTo,
        value: web3.toHex(amountWei),
        data: (customData && customData.length > 5) ? customData : null
    });
    if (customData && customData.length)
        transaction.data = customData;
    transaction.sign(privateKey);
    var txid = ('0x' + transaction.hash().toString('hex'));
    var serializedTx = transaction.serialize().toString('hex');

    return {
        txid: txid,
        serializedTx: serializedTx,
        addressTo: addressTo,
        transactionEth: transaction,
    };
}

function buildTokenTransaction(amountWei, addressTo, nonce, contractAddress, gasLimit, gasPrice, customData, privateKey) {
    var data = util.createTokenData(web3, amountWei, addressTo);
    if (customData) {
        // console.error('User supplied custom data which is being ignored!');
        console.log('Custom Data', customData);
    }
    //  console.log('Data', data);
    var raw = util.mapEthTransaction(web3, contractAddress, '0', nonce, gasPrice, gasLimit, data);
    // console.log(raw);
    var transaction = new EthereumTx(raw);
    //console.log(transaction);
    transaction.sign(privateKey);
    var serializedTx = transaction.serialize().toString('hex');
    var txid = ('0x' + transaction.hash().toString('hex'));
    return {
        txid: txid,
        serializedTx: serializedTx,
        addressTo: addressTo,
        transactionEth: transaction,
    };
}

function buildMapEosTransaction(eosPublicKey, nonce, contractAddress, gasLimit, gasPrice, customData, privateKey) {
    var data = util.getTxData('register', ['string'], [eosPublicKey]);
    if (customData) {
        // console.error('User supplied custom data which is being ignored!');
        console.log('Custom Data', customData);
    }
    //  console.log('Data', data);
    var raw = util.mapEthTransaction(web3, contractAddress, '0', nonce, gasPrice, gasLimit, data);
    // console.log(raw);
    var transaction = new EthereumTx(raw);
    //console.log(transaction);
    transaction.sign(privateKey);
    var serializedTx = transaction.serialize().toString('hex');
    var txid = ('0x' + transaction.hash().toString('hex'));
    return {
        txid: txid,
        serializedTx: serializedTx,
        transactionEth: transaction,
    };
}

function generateEosKeyPair(cb) {
    return util.generateEosKeyPair(cb);
}

function getPrivateKeyFromKeystore (password, keystoreContent) {
    var keyObject = JSON.parse(keystoreContent);
    return keythereum.recover(password, keyObject).toString('hex');
}

/**
 * Derive Ethereum publicKey from private key.
 * @param {buffer|string} privateKey ECDSA private key.
 * @return {string} Hex-encoded Ethereum publicKey.
 */
function privateToPublic(privateKey) {
    var privateKeyBuffer;
    privateKeyBuffer = Buffer.from(privateKey, 'hex');
    if (privateKeyBuffer.length < 32) {
        privateKeyBuffer = Buffer.concat([
            Buffer.alloc(32 - privateKeyBuffer.length, 0),
            privateKeyBuffer
        ]);
    }
   return secp256k1.publicKeyCreate(privateKeyBuffer, false).slice(1).toString("hex");
}

/**
 * Derive Ethereum address from private key.
 * @param {buffer|string} privateKey ECDSA private key.
 * @return {string} Hex-encoded Ethereum address.
 */
function privateToAddress(privateKey) {
    return keythereum.privateKeyToAddress(privateKey);
}

module.exports = {
    mnemonicToSeed: mnemonicToSeed,
    seedToAddress: seedToAddress,
    seedToChecksumAddress: seedToChecksumAddress,
    seedHexToAddress: seedHexToAddress,
    isValidAddress: isValidAddress,
    isValidChecksumAddress: isValidChecksumAddress,
    buildEthTransaction: buildEthTransaction,
    buildTokenTransaction: buildTokenTransaction,
    buildMapEosTransaction: buildMapEosTransaction,
    generateEosKeyPair: generateEosKeyPair,
    ibanToAddress: ibanToAddress,
    addressToIban: addressToIban,
    getPrivateKeyFromKeystore: getPrivateKeyFromKeystore,
    privateToPublic: privateToPublic,
    privateToAddress: privateToAddress,
};

