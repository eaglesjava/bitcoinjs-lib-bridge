package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.model.entity.TabItem;
import com.bitbill.www.common.base.view.MvpView;

import java.util.List;

/**
 * Created by isanwenyu on 2018/3/27.
 */

public interface TabsMvpView extends MvpView {

    void loadTabsSuccess(List<TabItem> tabItems);
}
