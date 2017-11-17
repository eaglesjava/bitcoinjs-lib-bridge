/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app;

import com.bitbill.www.common.base.model.Model;
import com.bitbill.www.model.app.network.AppApi;
import com.bitbill.www.model.app.prefs.AppPreferences;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */
public interface AppModel extends Model, AppApi, AppPreferences {

}
