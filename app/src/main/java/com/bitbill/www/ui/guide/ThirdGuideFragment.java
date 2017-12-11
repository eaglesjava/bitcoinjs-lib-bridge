package com.bitbill.www.ui.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.R;

/**
 * Created by isanwenyu@163.com on 2017/11/25.
 */
public class ThirdGuideFragment extends BaseGuideFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_third, container, false);
    }

    @Override
    public int[] getChildViewIds() {
        return new int[]{
                R.id.guide_item_contact,
                R.id.guide_label
        };
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_guide_third;
    }
}
