package com.bitbill.www.common.base.view;

import android.os.Bundle;

/**
 * <pre>
 * 基础布局相关接口
 * Created by zhuyuanbao on 2016/3/3.
 * Copyright (c) 2016 www.zhengshijr.com. All rights reserved.
 * </pre>
 */
public interface BaseViewControl {
    void onBeforeSetContentLayout();

    void init(Bundle savedInstanceState);

    void initView();

    void initData();

    int getLayoutId();
}
