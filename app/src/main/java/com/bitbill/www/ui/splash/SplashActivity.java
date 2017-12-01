package com.bitbill.www.ui.splash;

import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.utils.AnimationUtils;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity<SplashMvpPresenter> implements SplashMvpView {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @Inject
    SplashMvpPresenter<AppModel, SplashMvpView> mSplashMvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        flContent.startAnimation(AnimationUtils.getAlphaAnimation(0.8f, 1.0f, 2000, new Animation.AnimationListener() {
            /**
             * <p>Notifies the start of the animation.</p>
             *
             * @param animation The started animation.
             */
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**
             * <p>Notifies the end of the animation. This callback is not invoked
             * for animations with repeat count set to INFINITE.</p>
             *
             * @param animation The animation which reached its end.
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                // 根据是否是第一次进入判断跳转到引导页还是首页
                getMvpPresenter().checkBrowsed();

            }

            /**
             * <p>Notifies the repetition of the animation.</p>
             *
             * @param animation The animation which was repeated.
             */
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }));
    }

    @Override
    public SplashMvpPresenter getMvpPresenter() {
        return mSplashMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void isGuideBrowsed(boolean guideBrowsed) {
        if (guideBrowsed) {
            //浏览过引导页直接跳转到首页
            MainActivity.start(SplashActivity.this);
        } else {
            GuideActivity.start(SplashActivity.this);
            finish();
        }
    }
}
