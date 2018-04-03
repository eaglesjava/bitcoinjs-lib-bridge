var util = require('ethereumjs-util');
var hdkey = require('ethereumjs-wallet/hdkey');
var Web3 = require('web3');
var web3 = new Web3();

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
    return util.isValidAddress(address)
}

function isValidChecksumAddress(address) {
    return util.isValidChecksumAddress(address)
}

function createTokenData(amount, address) {
    //send max for tokens issue use big number library to parse value amount
    var ABI = web3.toBigNumber(amount, 10).toString(16); //amount;//parseInt(amount).toString(16);
    while (ABI.length < 64)
        ABI = '0' + ABI;
    address = address.substr(2);
    while (address.length < 64)
        address = '0' + address;
    var ethData = address + ABI;
    return '0xa9059cbb' + ethData;
}

module.exports = {
    mnemonicToSeed: mnemonicToSeed,
    seedToAddress: seedToAddress,
    seedHexToAddress: seedHexToAddress,
    isValidAddress: isValidAddress,
    isValidChecksumAddress: isValidChecksumAddress,
    createTokenData: createTokenData
};

// for test
// var address = seedHexToAddress('6fc2a047d00e5e9d883231023c92b8353085042915947d44a4ca239c9f1f7ab24cdb340dfc536430abb766f348e484bc776d120fd729292f0cdd39b2e8dc54a4')
// console.log(address)
// console.log(seedToAddress(mnemonicToSeed('favorite grape end strategy item horse first source popular cactus shine child')))

// console.log(isValidAddress(address))
// console.log(isValidChecksumAddress(address))
// console.log(isValidAddress('address'))

