package com.bitbill.www.ui.guide;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.ui.wallet.importing.ImportWalletActivity;
import com.bitbill.www.ui.wallet.init.CreateWalletIdActivity;
import com.rd.PageIndicatorView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页
 * Created by isanwenyu@163.com on 2017/11/14.
 */
public class GuideActivity extends BaseActivity<GuideMvpPresenter> implements BaseViewControl {

    private static final String TAG = "GuideActivity";
    final float PARALLAX_COEFFICIENT = 1.2f;
    final float DISTANCE_COEFFICIENT = 0.5f;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.phone_center)
    ImageView centerView;

    FragmentAdapter mAdapter;

    SparseArray<int[]> mLayoutViewIdsMap = new SparseArray<int[]>();
    @Inject
    GuideMvpPresenter<AppModel, GuideMvpView> mGuideMvpPresenter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static void start(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    private void addGuide(BaseGuideFragment fragment) {
        mAdapter.addItem(fragment);
        mLayoutViewIdsMap.put(fragment.getRootViewId(), fragment.getChildViewIds());
    }

    @Override
    public GuideMvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        setContentView(getLayoutId());
        // 通过注解绑定控件
        setUnBinder(ButterKnife.bind(this));
        init(savedInstanceState);
        initView();
        initData();

    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.viewPager);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        addGuide(new FirstGuideFragment());
        addGuide(new SecondGuideFragment());
        addGuide(new ThirdGuideFragment());

        mViewPager.setAdapter(mAdapter);

        mViewPager.setPageTransformer(true, new ParallaxTransformer(PARALLAX_COEFFICIENT, DISTANCE_COEFFICIENT));
        mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @OnClick({R.id.btn_create_wallet, R.id.btn_import_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_wallet:
                //跳转到创建钱包界面
                CreateWalletIdActivity.start(GuideActivity.this, null, true, TAG);
                break;
            case R.id.btn_import_wallet:
                //跳转到导入钱包界面
                ImportWalletActivity.start(GuideActivity.this, TAG);
                break;
        }
    }


    class ParallaxTransformer implements ViewPager.PageTransformer {

        float parallaxCoefficient;
        float distanceCoefficient;

        public ParallaxTransformer(float parallaxCoefficient, float distanceCoefficient) {
            this.parallaxCoefficient = parallaxCoefficient;
            this.distanceCoefficient = distanceCoefficient;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void transformPage(View page, float position) {
            float scrollXOffset = page.getWidth() * parallaxCoefficient;

            ViewGroup pageViewWrapper = (ViewGroup) page;
            @SuppressWarnings("SuspiciousMethodCalls")
            int[] layer = mLayoutViewIdsMap.get(pageViewWrapper.getId());
            for (int id : layer) {
                View view = page.findViewById(id);
                if (view != null) {
                    view.setTranslationX(scrollXOffset * position);
                }
                scrollXOffset *= distanceCoefficient;
            }
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public GuidePageChangeListener() {
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == 0) {
                centerView.setAlpha(1 - positionOffset);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
