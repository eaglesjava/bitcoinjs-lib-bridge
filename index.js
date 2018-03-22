var ethereumjsUtil = require('ethereumjs-util');
var hdkey = require('ethereumjs-wallet/hdkey');
var EthereumTx = require('ethereumjs-tx');

var Web3 = require('web3');
var web3 = new Web3();

var util = require('./util');

// jaxx path
var ETHEREUM_MAINNET_PATH = "m/44'/60'/0'/0/0";
var ETHEREUM_TESTNET_PATH = "m/44'/1'/0'/0";

var bip39 = require('bip39');

function mnemonicToSeed(mnemonic) {
	var seed = bip39.mnemonicToSeed(mnemonic);
	return seed;
}

function seedToAddress(seed) {
	var hd = hdkey.fromMasterSeed(seed);
	var wallet = hd.derivePath(ETHEREUM_MAINNET_PATH).getWallet();
	return wallet.getChecksumAddressString();
}

function seedHexToAddress(seedHex) {
	var seed = Buffer.from(seedHex, 'hex');
	return seedToAddress(seed);
}

function isValidAddress(address) {
	return ethereumjsUtil.isValidAddress(address)
}

function isValidChecksumAddress(address) {
	return ethereumjsUtil.isValidChecksumAddress(address)
}

function buildEthTransaction (amountWei, addressTo, nonce, privateKey, gasPrice, gasLimit, customData) {
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

function buildTokenTransaction (amountWei, addressTo, nonce, privateKey, contractAddress, gasLimit, gasPrice, customData) {
    var data = util.createTokenData(web3, amountWei, addressTo);
    if (customData) {
        console.error('User supplied custom data which is being ignored!');
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

module.exports = {
    mnemonicToSeed: mnemonicToSeed,
    seedToAddress: seedToAddress,
    seedHexToAddress: seedHexToAddress,
    isValidAddress: isValidAddress,
    isValidChecksumAddress: isValidChecksumAddress,
    buildEthTransaction: buildEthTransaction,
    buildTokenTransaction: buildTokenTransaction,
};

// for test
// var address = seedHexToAddress('6fc2a047d00e5e9d883231023c92b8353085042915947d44a4ca239c9f1f7ab24cdb340dfc536430abb766f348e484bc776d120fd729292f0cdd39b2e8dc54a4')
// console.log(address)
// console.log(seedToAddress(mnemonicToSeed('favorite grape end strategy item horse first source popular cactus shine child')))

// console.log(isValidAddress(address))
// console.log(isValidChecksumAddress(address))
// console.log(isValidAddress('address'))

