/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.entity;


import com.bitbill.www.common.utils.JsonUtils;

import java.io.Serializable;

/**
 * Created by isanwenyu@163.com on 2017/7/18.
 */
public abstract class Entity implements Serializable {

    public String jsonString() {
        return JsonUtils.serialize(this);
    }
}
