package com.bitbill.www;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.bitbill.www.crypto.utils.ConvertUtils.hexString2Bytes;
import static com.bitbill.www.crypto.utils.EncodeUtils.base64Encode;
import static com.bitbill.www.crypto.utils.EncryptUtils.decryptAES;
import static com.bitbill.www.crypto.utils.EncryptUtils.decryptBase64AES;
import static com.bitbill.www.crypto.utils.EncryptUtils.decryptHexStringAES;
import static com.bitbill.www.crypto.utils.EncryptUtils.encryptAES;
import static com.bitbill.www.crypto.utils.EncryptUtils.encryptAES2Base64;
import static com.bitbill.www.crypto.utils.EncryptUtils.encryptAES2HexString;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/6
 *     desc  : EncryptUtils单元测试
 * </pre>
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 21)
public class EncryptUtilsTest {


    String dataAES = "11111111111111111111111111111111";
    String keyAES = "11111111111111111111111111111111";
    String resAES = "E56E26F5608B8D268F2556E198A0E01B";
    byte[] bytesDataAES = hexString2Bytes(dataAES);
    byte[] bytesKeyAES = hexString2Bytes(keyAES);
    byte[] bytesResAES = hexString2Bytes(resAES);

    @Test
    public void testEncryptAES() throws Exception {
        Assert.assertThat(encryptAES(bytesDataAES, bytesKeyAES)).isEqualTo(bytesResAES);
        Assert.assertThat(encryptAES2HexString(bytesDataAES, bytesKeyAES)).isEqualTo(resAES);
        Assert.assertThat(encryptAES2Base64(bytesDataAES, bytesKeyAES)).isEqualTo(base64Encode
                (bytesResAES));
    }

    @Test
    public void testDecryptAES() throws Exception {
        Assert.assertThat(decryptAES(bytesResAES, bytesKeyAES)).isEqualTo(bytesDataAES);
        Assert.assertThat(decryptHexStringAES(resAES, bytesKeyAES)).isEqualTo(bytesDataAES);
        Assert.assertThat(decryptBase64AES(base64Encode(bytesResAES), bytesKeyAES)).isEqualTo
                (bytesDataAES);
    }

}