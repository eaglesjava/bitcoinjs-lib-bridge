package com.bitbill.www.ui.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bitbill.www.R;

/**
 * Created by isanwenyu@163.com on 2017/11/25.
 */
public class FirstGuideFragment extends BaseGuideFragment {

    final long ANIMATION_DURATION = 500;
    final long ANIMATION_OFFSET = 200;

    private int[] mAnimationViewIds = {
            R.id.guide_item_xrp, R.id.guide_item_ltc, R.id.guide_item_btc,
            R.id.guide_item_eth, R.id.guide_item_eos, R.id.guide_item_neo
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_first, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 0; i < mAnimationViewIds.length; i++) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.guide_items);
            animation.setDuration(ANIMATION_DURATION);
            animation.setStartOffset(ANIMATION_OFFSET * i);
            view.findViewById(mAnimationViewIds[i]).startAnimation(animation);
        }
    }

    @Override
    public int[] getChildViewIds() {
        return new int[]{
                R.id.guide_group,
                R.id.guide_label
        };
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_guide_first;
    }
}
