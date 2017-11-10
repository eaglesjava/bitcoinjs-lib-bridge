# bitcoinjs-lib-bridge

## 安装依赖
`sudo npm install bip39`
 
`sudo npm install bip32-utils`

## 转译命令
`browserify index.js --standalone bridge > bitcoinjs-lib-app.js`

## 函数调用格式
```
bridge.methodName('param1', 'param2')
```

## 公开的函数
名称 | 功能 | 参数 | 返回值
--- | --- | --- | ---
generateMnemonicRandom | 随机产生助记词 | entroy: 长度 <br> wordlist: 词库列表，语言 | 助记词字符串，以空格隔开
generateMnemonicRandomCN | 随机产生中文助记词 | entroy: 长度 | 助记词字符串，以空格隔开
mnemonicToSeedHex | 助记词生成seed | mnemonic: 助记词字符串，以空格隔开 <br> password: 密码 | seedHex
getAddressBySeedHex | 根据seed生成指定index的地址 | seedHex: seed的十六进制字符串 <br> index：index | 地址
getAddressByMasterXPublicKey | 根据主xpub生成指定index的地址 | xpub: 主扩展公钥 <br> index：index | 地址
getMasterXPublicKey | 获取主扩展公钥 | seedHex: seed的十六进制字符串 | 主扩展公钥

