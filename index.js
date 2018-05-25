var bitcoin = require('bitcoinjs-lib');
var bip39 = require('bip39');
var wif = require('wif')

var BITCOIN_MAINNET_PATH = "m/44'/0'/0'/0";
var BITCOIN_MAINNET_CHANGE_PATH = "m/44'/0'/0'/1";
var BITCOIN_TESTNET_PATH = "m/44'/1'/0'/0";

// 随机生成中文助记词，entropy： 长度， wordlist：
function generateMnemonicRandom(entropy, wordlist) {
    // Generate a random mnemonic (uses crypto.randomBytes under the hood), defaults to 128-bits of entropy
    return bip39.generateMnemonic(entropy, null, wordlist);
}

// 随机生成中文助记词，entropy： 长度
function generateMnemonicRandomCN(entropy) {
    return generateMnemonicRandom(entropy, bip39.wordlists.chinese_simplified);
}

function mnemonicToSeedHex(mnemonic, password) {
    return bip39.mnemonicToSeedHex(mnemonic, password);
}

function validateMnemonic(mnemonic) {
    return bip39.validateMnemonic(mnemonic, bip39.wordlists.chinese_simplified) || bip39.validateMnemonic(mnemonic, bip39.wordlists.english);
}

function validateAddress(address) {
    try {
        bitcoin.address.toOutputScript(address);
        return true;
    } catch (e) {
        return false;
    }
}

function getBitcoinAddressBySeedHex(seedHex, index) {
    var bitcoinKeyChain = generateBitcoinMainnetMasterKeychain(seedHex);
    return bitcoinKeyChain.derive(index).getAddress();
}

function getBitcoinXPublicKeys(seedHex) {
    return new Array(getBitcoinMasterXPublicKey(seedHex), getBitcoinChangeXPublicKey(seedHex))
}

function getBitcoinMasterXPublicKey(seedHex) {
    var keychain = generateBitcoinMainnetMasterKeychain(seedHex);
    return keychain.neutered().toBase58();
}

function getBitcoinChangeXPublicKey(seedHex) {
    var keychain = generateBitcoinMainnetChangeKeychain(seedHex);
    return keychain.neutered().toBase58();
}

function getBitcoinAddressByMasterXPublicKey(xpub, index) {
    var node = bitcoin.HDNode.fromBase58(xpub);
    return node.derive(index).getAddress();
}

function getBitcoinContinuousAddressByMasterXPublicKey(xpub, fromIndex, inIndex) {
    var node = bitcoin.HDNode.fromBase58(xpub);
    var addresses = new Array();
    for (var i = fromIndex, j = 0; i <= inIndex; i++, j++) {
        addresses[j] = node.derive(i).getAddress();
    }
    return addresses;
}

function generateMainnetMasterKeychain(seedHex) {
    return bitcoin.HDNode.fromSeedHex(seedHex);
}

function generateBitcoinMainnetMasterKeychain(seedHex) {
    return generateMainnetMasterKeychain(seedHex).derivePath(BITCOIN_MAINNET_PATH);
}

function generateBitcoinMainnetChangeKeychain(seedHex) {
    return generateMainnetMasterKeychain(seedHex).derivePath(BITCOIN_MAINNET_CHANGE_PATH);
}

function generateBitcoinTestnetMasterKeychain(seedHex) {
    return generateMainnetMasterKeychain(seedHex).derivePath(BITCOIN_TESTNET_PATH);
}

function buildTransaction(seedHex, data) {
    data = JSON.parse(data);
    var inputs = data["inputs"];
    var outputs = data["outputs"];
    
    var keychain = generateBitcoinMainnetMasterKeychain(seedHex);
    var changeKeychain = generateBitcoinMainnetChangeKeychain(seedHex);
    var txb = new bitcoin.TransactionBuilder();
    var ecPairs = [];
    
    for (var i = 0; i < inputs.length; i++) {
        var input = inputs[i];
        txb.addInput(input["txHash"], input["index"]);
        var isChange = input["isChange"];
        ecPairs[i] = (isChange ? changeKeychain : keychain).derive(input["bip39Index"]);
    }
    
    for (var _i = 0; _i < outputs.length; _i++) {
        var output = outputs[_i];
        txb.addOutput(output["address"], output["amount"]);
    }
    
    for (var _i2 = ecPairs.length - 1; _i2 >= 0; _i2--) {
        txb.sign(_i2, ecPairs[_i2]);
    }
    
    return txb.build().toHex();
}

function buildRBFTransaction(seedHex, data) {
    data = JSON.parse(data);
    var inputs = data["inputs"];
    var outputs = data["outputs"];

    var keychain = generateBitcoinMainnetMasterKeychain(seedHex);
    var changeKeychain = generateBitcoinMainnetChangeKeychain(seedHex);
    var txb = new bitcoin.TransactionBuilder();
    var ecPairs = [];

    for (var i = 0; i < inputs.length; i++) {
        var input = inputs[i];
        txb.addInput(input["txHash"], input["index"], 0xfffffffd);
        var isChange = input["isChange"];
        ecPairs[i] = (isChange ? changeKeychain : keychain).derive(input["bip39Index"]);
    }

    for (var _i = 0; _i < outputs.length; _i++) {
        var output = outputs[_i];
        txb.addOutput(output["address"], output["amount"]);
    }

    for (var _i2 = ecPairs.length - 1; _i2 >= 0; _i2--) {
        txb.sign(_i2, ecPairs[_i2]);
    }

    return txb.build().toHex();
}

function buildTransactionWithOnePrivateKey(privateKey, data) {
    data = JSON.parse(data);
    var inputs = data["inputs"];
    var outputs = data["outputs"];
    
    var txb = new bitcoin.TransactionBuilder();
    
    var ecPair = ecpairFromPrivateKey(privateKey)
    
    for (var i = 0; i < inputs.length; i++) {
        var input = inputs[i];
        txb.addInput(input["txHash"], input["index"]);
    }
    
    for (var _i = 0; _i < outputs.length; _i++) {
        var output = outputs[_i];
        txb.addOutput(output["address"], output["amount"]);
    }
    
    for (var _i2 = inputs.length - 1; _i2 >= 0; _i2--) {
        txb.sign(_i2, ecPair);
    }
    
    return txb.build().toHex();
}

function buildRBFTxWithOnePrivateKey(privateKey, data) {
    data = JSON.parse(data);
    var inputs = data["inputs"];
    var outputs = data["outputs"];

    var txb = new bitcoin.TransactionBuilder();

    var ecPair = ecpairFromPrivateKey(privateKey)

    for (var i = 0; i < inputs.length; i++) {
        var input = inputs[i];
        txb.addInput(input["txHash"], input["index"], 0xfffffffd);
    }

    for (var _i = 0; _i < outputs.length; _i++) {
        var output = outputs[_i];
        txb.addOutput(output["address"], output["amount"]);
    }

    for (var _i2 = inputs.length - 1; _i2 >= 0; _i2--) {
        txb.sign(_i2, ecPair);
    }

    return txb.build().toHex();
}

function getPublicKeyFromPrivateKey(privateKey) {
    var keyPair = ecpairFromPrivateKey(privateKey)
    var pubKeyBuffer = keyPair.getPublicKeyBuffer()
    return pubKeyBuffer.toString('hex')
}

function getAddressFromPublicKey(publicKey) {
    var pubKeyBuffer = new Buffer(publicKey, 'hex')
    var keyPair = bitcoin.ECPair.fromPublicKeyBuffer(pubKeyBuffer)
    return keyPair.getAddress()
}

function getAddressFromPrivateKey(privateKey) {
    var keyPair = ecpairFromPrivateKey(privateKey)
    return keyPair.getAddress()
}

function getPublicKeyAndArressFormPrivateKey(privateKey) {
    return [getPublicKeyFromPrivateKey(privateKey), getAddressFromPrivateKey(privateKey)]
}

function ecpairFromPrivateKey(privateKey) {
    var prvKey = new Buffer(privateKey, 'hex')
    var key = wif.encode(128, prvKey, false)
    var keyPair = bitcoin.ECPair.fromWIF(key)
    return keyPair
}

module.exports = {
    generateMnemonicRandom: generateMnemonicRandom,
    generateMnemonicRandomCN: generateMnemonicRandomCN,
    mnemonicToSeedHex: mnemonicToSeedHex,
    validateMnemonic: validateMnemonic,
    validateAddress: validateAddress,
    getBitcoinAddressBySeedHex: getBitcoinAddressBySeedHex,
    getBitcoinAddressByMasterXPublicKey: getBitcoinAddressByMasterXPublicKey,
    getBitcoinContinuousAddressByMasterXPublicKey: getBitcoinContinuousAddressByMasterXPublicKey,
    getBitcoinMasterXPublicKey: getBitcoinMasterXPublicKey,
    buildTransaction: buildTransaction,
    buildTransactionWithOnePrivateKey: buildTransactionWithOnePrivateKey,
    getBitcoinChangeXPublicKey: getBitcoinChangeXPublicKey,
    getBitcoinXPublicKeys: getBitcoinXPublicKeys,
    bip39: bip39,
    getPublicKeyFromPrivateKey: getPublicKeyFromPrivateKey,
    getAddressFromPrivateKey: getAddressFromPrivateKey,
    getAddressFromPublicKey: getAddressFromPublicKey,
    getPublicKeyAndArressFormPrivateKey: getPublicKeyAndArressFormPrivateKey,
    buildRBFTransaction: buildRBFTransaction,
    buildRBFTxWithOnePrivateKey: buildRBFTxWithOnePrivateKey
};
