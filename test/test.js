/*
*
* simple test
*
* */

var address = bridge.seedHexToAddress('6fc2a047d00e5e9d883231023c92b8353085042915947d44a4ca239c9f1f7ab24cdb340dfc536430abb766f348e484bc776d120fd729292f0cdd39b2e8dc54a4')
console.log('seedHexToAddress: ' + address)
console.log('seedHexToPubAddr: ' + JSON.stringify(bridge.seedHexToPubAddr('6fc2a047d00e5e9d883231023c92b8353085042915947d44a4ca239c9f1f7ab24cdb340dfc536430abb766f348e484bc776d120fd729292f0cdd39b2e8dc54a4')))
console.log('seedHexToPrivate: ' + bridge.seedHexToPrivate('6fc2a047d00e5e9d883231023c92b8353085042915947d44a4ca239c9f1f7ab24cdb340dfc536430abb766f348e484bc776d120fd729292f0cdd39b2e8dc54a4').toString('hex'))
var add = bridge.seedToAddress(bridge.mnemonicToSeed('favorite grape end strategy item horse first source popular cactus shine child'))
console.log('mnemonicToSeed-seedToAddress: ' + add)

console.log('isValidAddress: ' + bridge.isValidAddress(address))
console.log('isValidChecksumAddress: ' + bridge.isValidChecksumAddress(address))
console.log('isValidAddress: ' + bridge.isValidAddress('address'))

console.log('ibanToAddress: ' + bridge.ibanToAddress('XE7338O073KYGTWWZN0F2WZ0R8PX5ZPPZS'))
console.log('addressToIban: ' + bridge.addressToIban(address))
console.log('addressToIban: ' + bridge.addressToIban(add))



// generate eos key pair
bridge.generateEosKeyPair(function(eosKeyPair) {
    console.log('eos key pair - ' + 'publicKey: ' + eosKeyPair.publicKey + ' privateKey: ' + eosKeyPair.privateKey)
})

// get private key from keystore
var privateKey = bridge.getPrivateKeyFromKeystore('123456789', '{"version":3,"id":"ce14bddd-dc5b-4f24-b94c-1bae704f6866","address":"2a055947da8ba17ac751f2aa2ea5ecfee3db8c33","Crypto":{"ciphertext":"31743384b6bede7741d90445715e95600a108edb38fd118d65e07bbf2b1e2c68","cipherparams":{"iv":"8f7b036788323944affb3ddfe286a9d4"},"cipher":"aes-128-ctr","kdf":"scrypt","kdfparams":{"dklen":32,"salt":"d69affc8ef632ea27dedddacac00f714d17537c980193fb7dd26051d04eb4f57","n":8192,"r":8,"p":1},"mac":"581846f21e493383ae500012664d340c95dffb7897fb061519aedc9a9c96e915"}}')
console.log('get private key from keystore: ' + privateKey.toString('hex'))

var publicKey = bridge.privateToPublic(privateKey);
console.log('private to public|length: ' + publicKey.toString('hex') + '|' + publicKey.length)
console.log('public to address: ' + bridge.publicToAddress(publicKey).toString('hex'))
console.log('private to address: ' + bridge.privateToAddress(privateKey).toString('hex'))

var array1 = bridge.getKeyPairAddrFromKeystore('123456789', '{"version":3,"id":"ce14bddd-dc5b-4f24-b94c-1bae704f6866","address":"2a055947da8ba17ac751f2aa2ea5ecfee3db8c33","Crypto":{"ciphertext":"31743384b6bede7741d90445715e95600a108edb38fd118d65e07bbf2b1e2c68","cipherparams":{"iv":"8f7b036788323944affb3ddfe286a9d4"},"cipher":"aes-128-ctr","kdf":"scrypt","kdfparams":{"dklen":32,"salt":"d69affc8ef632ea27dedddacac00f714d17537c980193fb7dd26051d04eb4f57","n":8192,"r":8,"p":1},"mac":"581846f21e493383ae500012664d340c95dffb7897fb061519aedc9a9c96e915"}}')
console.log('getKeyPairAddrFromKeystore: ' + JSON.stringify(array1))

console.log('getPubAddrFromPrivate: ' + JSON.stringify(bridge.getPubAddrFromPrivate('ded772d10a77295b897824db5a4ab11c24a507a146cef2560460e491d242ccb9')))



// build tx
console.log('buildEthTransaction:' + JSON.stringify(bridge.buildEthTransaction(privateKey.toString('hex'), 2441406250, '0xd46e8dd67c5d32be8058bb8eb970870f07244567', 0, 10e12, 30400)))