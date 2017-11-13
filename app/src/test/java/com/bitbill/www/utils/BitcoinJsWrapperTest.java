package com.bitbill.www.utils;

import com.bitbill.www.BuildConfig;
import com.bitbill.www.app.BitbillApp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by isanwenyu@163.com on 2017/11/10.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = BitbillApp.class)
public class BitcoinJsWrapperTest {
    private BitcoinJsWrapper mBitcoinJsWrapper;

    @Before
    public void setUp() throws Exception {
        mBitcoinJsWrapper = BitcoinJsWrapper.getInstance();
    }

    @Test
    public void getMnemonic() throws Exception {

        final String[] result = new String[1];
        final CountDownLatch latch = new CountDownLatch(3);
        mBitcoinJsWrapper.getMnemonic(new BitcoinJsWrapper.JsInterface.Callback() {

            @Override
            public void call(String key, String jsResult) {
                result[0] = jsResult;
                System.out.println("key = [" + key + "], jsResult = [" + jsResult + "]");
                latch.countDown();
            }
        });

        latch.await();
        // 此处有坑，如果不加这行代码，就会出现Handler没有执行Runnable的问题
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        System.out.println("result = " + result);
        Assert.assertEquals(1, result.length);
    }

    @Test
    public void mnemonicToSeedHex() throws Exception {
        mBitcoinJsWrapper.mnemonicToSeedHex("", "123456", new BitcoinJsWrapper.JsInterface.Callback() {
            @Override
            public void call(String key, String jsResult) {
                System.out.println("key = [" + key + "], jsResult = [" + jsResult + "]");
            }
        });

    }

    @Test
    public void getAddressBySeedHex() throws Exception {
        System.out.println("BitcoinJsWrapperTest.getAddressBySeedHex");

    }

    @Test
    public void getBitcoinAddressByMasterXPublicKey() throws Exception {

    }

    @Test
    public void getBitcoinMasterXPublicKey() throws Exception {

    }

}