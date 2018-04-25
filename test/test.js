/*
*
* simple tests
*
* */

var expect = chai.expect;

var seedHex = '6fc2a047d00e5e9d883231023c92b8353085042915947d44a4ca239c9f1f7ab24cdb340dfc536430abb766f348e484bc776d120fd729292f0cdd39b2e8dc54a4'
var mnemonic = 'favorite grape end strategy item horse first source popular cactus shine child'

var seedHexToAddress = bridge.seedHexToAddress(seedHex)
console.log('seedHexToAddress: ' + seedHexToAddress)
console.log('seedHexToPubAddr: ' + JSON.stringify(bridge.seedHexToPubAddr(seedHex)))
console.log('seedHexToPrivate: ' + bridge.seedHexToPrivate(seedHex).toString('hex'))

var mnemonicToAddress = bridge.seedToAddress(bridge.mnemonicToSeed(mnemonic))
console.log('mnemonicToSeed-seedToAddress: ' + mnemonicToAddress)

console.log('isValidAddress: ' + bridge.isValidAddress(seedHexToAddress))
console.log('isValidAddress: ' + bridge.isValidAddress('0x8617E340B3D01FA5F11F306F4090FD50E238070W'))
console.log('isValidChecksumAddress: ' + bridge.isValidChecksumAddress(seedHexToAddress))
console.log('isAddress: ' + bridge.isAddress('0x8617E340B3D01FA5F11F306F4090FD50E238070D'))
console.log('isAddress: ' + bridge.isAddress('0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb'))
console.log('isAddress: ' + bridge.isAddress('0xd1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb'))

console.log('ibanToAddress: ' + bridge.ibanToAddress('XE7338O073KYGTWWZN0F2WZ0R8PX5ZPPZS'))
console.log('addressToIban: ' + bridge.addressToIban('0x00c5496aee77c1ba1f0854206a26dda82a81d6d8'))

// get private key from keystore
var privateKey = bridge.getPrivateKeyFromKeystore('123456789', '{"version":3,"id":"ce14bddd-dc5b-4f24-b94c-1bae704f6866","address":"2a055947da8ba17ac751f2aa2ea5ecfee3db8c33","Crypto":{"ciphertext":"31743384b6bede7741d90445715e95600a108edb38fd118d65e07bbf2b1e2c68","cipherparams":{"iv":"8f7b036788323944affb3ddfe286a9d4"},"cipher":"aes-128-ctr","kdf":"scrypt","kdfparams":{"dklen":32,"salt":"d69affc8ef632ea27dedddacac00f714d17537c980193fb7dd26051d04eb4f57","n":8192,"r":8,"p":1},"mac":"581846f21e493383ae500012664d340c95dffb7897fb061519aedc9a9c96e915"}}')
console.log('get private key from keystore: ' + privateKey.toString('hex'))

var publicKey = bridge.privateToPublic(privateKey);
console.log('private to public|length: ' + publicKey.toString('hex') + '|' + publicKey.length)

console.log('public to address: ' + bridge.publicToAddress(publicKey).toString('hex'))
console.log('private to address: ' + bridge.privateToAddress(privateKey).toString('hex'))

var keyPairAddrArray = bridge.getKeyPairAddrFromKeystore('123456789', '{"version":3,"id":"ce14bddd-dc5b-4f24-b94c-1bae704f6866","address":"2a055947da8ba17ac751f2aa2ea5ecfee3db8c33","Crypto":{"ciphertext":"31743384b6bede7741d90445715e95600a108edb38fd118d65e07bbf2b1e2c68","cipherparams":{"iv":"8f7b036788323944affb3ddfe286a9d4"},"cipher":"aes-128-ctr","kdf":"scrypt","kdfparams":{"dklen":32,"salt":"d69affc8ef632ea27dedddacac00f714d17537c980193fb7dd26051d04eb4f57","n":8192,"r":8,"p":1},"mac":"581846f21e493383ae500012664d340c95dffb7897fb061519aedc9a9c96e915"}}')
console.log('getKeyPairAddrFromKeystore: ' + JSON.stringify(keyPairAddrArray))

console.log('getPubAddrFromPrivate: ' + JSON.stringify(bridge.getPubAddrFromPrivate(privateKey.toString('hex'))))


// generate eos key pair
bridge.generateEosKeyPair(function(eosKeyPair) {
    console.log('eos key pair - ' + 'publicKey: ' + eosKeyPair.publicKey + ' privateKey: ' + eosKeyPair.privateKey)
})

// build tx
console.log('buildEthTransaction:' + JSON.stringify(bridge.buildEthTransaction(privateKey.toString('hex'), 2441406250, '0xd46e8dd67c5d32be8058bb8eb970870f07244567', 0, 10e12, 30400)))


describe('seed', function() {
    it('seedHexToAddress', function() {
        expect(seedHexToAddress).to.be.equal('0x9124bae940c2321DEd56f89B7e185b8785942303');
    });

    it('seedHexToPubAddr', function() {
        expect(JSON.stringify(bridge.seedHexToPubAddr(seedHex))).to.be.equal('["e351cdae507b5c6f7d88e6966ff10d13be1668372cf1d2c60b26c851fbd41c3a6bf2452de42cdb6532aef93070760b32c0bb8f055ee258cc2973aab1d396aa53","0x9124bae940c2321DEd56f89B7e185b8785942303"]');
    });

    it('seedHexToPrivate', function() {
        expect(bridge.seedHexToPrivate(seedHex).toString('hex')).to.be.equal('f21c74d3bf4464e1472343ce5bbd62a572afcf51e36d6b65ac003fe53c3dca3d');
    });

    it('seedToAddress', function() {
        expect(bridge.seedToAddress(bridge.mnemonicToSeed(mnemonic))).to.be.equal('0x9124bae940c2321ded56f89b7e185b8785942303');
    });
});

describe('mnemonic', function() {
    it('mnemonicToSeed', function() {
        expect(bridge.mnemonicToSeed(mnemonic).toString('hex')).to.be.equal(seedHex);
    });
});

describe('verify address', function() {
    it('isValidAddress true', function() {
        expect(bridge.isValidAddress(seedHexToAddress)).to.be.equal(true);
    });

    it('isValidAddress false', function() {
        expect(bridge.isValidAddress('0x8617E340B3D01FA5F11F306F4090FD50E238070W')).to.be.equal(false);
    });

    it('isValidChecksumAddress', function() {
        expect(bridge.isValidChecksumAddress(seedHexToAddress)).to.be.equal(true);
    });

    it('isAddress true', function() {
        expect(bridge.isAddress('0x8617E340B3D01FA5F11F306F4090FD50E238070D')).to.be.equal(true);
        expect(bridge.isAddress('0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb')).to.be.equal(true);
    });

    it('isAddress false', function() {
        expect(bridge.isAddress('0xd1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb')).to.be.equal(false);
    });
});

describe('iban', function() {
    it('ibanToAddress', function() {
        expect(bridge.ibanToAddress('XE7338O073KYGTWWZN0F2WZ0R8PX5ZPPZS')).to.be.equal('0x00c5496aee77c1ba1f0854206a26dda82a81d6d8');
    });

    it('ibanToAddress', function() {
        expect(bridge.addressToIban('0x00c5496aee77c1ba1f0854206a26dda82a81d6d8')).to.be.equal('XE7338O073KYGTWWZN0F2WZ0R8PX5ZPPZS');
    });
});

describe('import keystore', function() {
    it('getPrivateKeyFromKeystore', function() {
        expect(privateKey.toString('hex')).to.be.equal('ded772d10a77295b897824db5a4ab11c24a507a146cef2560460e491d242ccb9');
    });

    it('getKeyPairAddrFromKeystore', function() {
        expect(JSON.stringify(keyPairAddrArray)).to.be.equal('["ded772d10a77295b897824db5a4ab11c24a507a146cef2560460e491d242ccb9","18ad4ff97d0337b6434826daa2142137afac8cb39c28485414bb77289b26dd54de8a73efd888d904267d36fc42ebe6b10db2a337a5ceb3e7972aa532cd58a817","0x2a055947dA8bA17Ac751f2Aa2EA5EcfEe3Db8C33"]');
    });
});

describe('publicKey and privateKey', function() {
    it('privateToPublic', function() {
        expect(publicKey.toString('hex')).to.be.equal('18ad4ff97d0337b6434826daa2142137afac8cb39c28485414bb77289b26dd54de8a73efd888d904267d36fc42ebe6b10db2a337a5ceb3e7972aa532cd58a817');
    });

    it('privateToAddress', function() {
        expect(bridge.privateToAddress(privateKey).toString('hex')).to.be.equal('2a055947da8ba17ac751f2aa2ea5ecfee3db8c33');
    });

    it('publicToAddress', function() {
        expect(bridge.publicToAddress(publicKey).toString('hex')).to.be.equal('2a055947da8ba17ac751f2aa2ea5ecfee3db8c33');
    });

    it('getPubAddrFromPrivate', function() {
        expect(JSON.stringify(bridge.getPubAddrFromPrivate(privateKey.toString('hex')))).to.be.equal('["18ad4ff97d0337b6434826daa2142137afac8cb39c28485414bb77289b26dd54de8a73efd888d904267d36fc42ebe6b10db2a337a5ceb3e7972aa532cd58a817","0x2a055947dA8bA17Ac751f2Aa2EA5EcfEe3Db8C33"]');
    });
});