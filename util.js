/**
 * utils for eth wallet
 *
 */


var createTokenData = function(web3, amount, address) {
    //send max for tokens issue use big number library to parse value amount
    var ABI = web3.toBigNumber(amount, 10).toString(16); //amount;//parseInt(amount).toString(16);
    while (ABI.length < 64)
        ABI = '0' + ABI;
    address = address.substr(2);
    while (address.length < 64)
        address = '0' + address;
    var ethData = address + ABI;
    return '0xa9059cbb' + ethData;
};

var mapEthTransaction = function(web3, addressTo, amount, nonce, gasPrice, gasLimit, data) {
    return {
        nonce: web3.toHex(nonce),
        gasPrice: web3.toHex(gasPrice),
        gasLimit: web3.toHex(gasLimit),
        to: addressTo,
        value: web3.toHex(amount),
        data: data
    };
};

module.exports = {
    createTokenData: createTokenData,
    mapEthTransaction: mapEthTransaction,
};