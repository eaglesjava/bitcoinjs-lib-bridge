package com.mcxtzhang.indexlib.IndexBar.utils;

import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.helper.IIndexBarDataHelper;
import com.mcxtzhang.indexlib.IndexBar.helper.IndexBarDataHelperImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 索引帮助类
 * Created by isanwenyu@163.com on 2017/12/25.
 */

public class IndexHelper {
    private static final String TAG = "IndexHelper";

    //#在最后面（默认的数据源）
    public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    //是否需要根据实际的数据来生成索引数据源（例如 只有 A B C 三种tag，那么索引栏就 A B C 三项）
    private boolean isNeedRealIndex = true;
    //索引数据源
    private List<String> mIndexDatas;

    //以下是帮助类
    //汉语->拼音，拼音->tag
    private IIndexBarDataHelper mDataHelper;

    //以下边变量是外部set进来的
    private TextView mPressedShowTextView;//用于特写显示正在被触摸的index值
    private boolean isSourceDatasAlreadySorted;//源数据 已经有序？
    private List<? extends BaseIndexPinyinBean> mSourceDatas;//Adapter的数据源
    private int mHeaderViewCount = 0;

    public IndexHelper(List<? extends BaseIndexPinyinBean> datas) {
        initIndexDatas();
        mDataHelper = new IndexBarDataHelperImpl();
        setSourceDatas(datas);
    }


    /**
     * 一定要在设置数据源{@link #setSourceDatas(List)}之前调用
     *
     * @param needRealIndex
     * @return
     */
    public IndexHelper setNeedRealIndex(boolean needRealIndex) {
        isNeedRealIndex = needRealIndex;
        initIndexDatas();
        return this;
    }

    private void initIndexDatas() {
        if (isNeedRealIndex) {
            mIndexDatas = new ArrayList<>();
        } else {
            mIndexDatas = Arrays.asList(INDEX_STRING);
        }
    }

    public IndexHelper setSourceDatas(List<? extends BaseIndexPinyinBean> mSourceDatas) {
        this.mSourceDatas = mSourceDatas;
        initSourceDatas();//对数据源进行初始化
        return this;
    }

    /**
     * 初始化原始数据源，并取出索引数据源
     *
     * @return
     */
    private void initSourceDatas() {
        //add by zhangxutong 2016 09 08 :解决源数据为空 或者size为0的情况,
        if (null == mSourceDatas || mSourceDatas.isEmpty()) {
            return;
        }
        if (!isSourceDatasAlreadySorted) {
            //排序sourceDatas
            mDataHelper.sortSourceDatas(mSourceDatas);
        } else {
            //汉语->拼音
            mDataHelper.convert(mSourceDatas);
            //拼音->tag
            mDataHelper.fillInexTag(mSourceDatas);
        }
        if (isNeedRealIndex) {
            mDataHelper.getSortedIndexDatas(mSourceDatas, mIndexDatas);
        }
        //sortData();
    }

    public int getHeaderViewCount() {
        return mHeaderViewCount;
    }

    /**
     * 设置Headerview的Count
     *
     * @param headerViewCount
     * @return
     */
    public IndexHelper setHeaderViewCount(int headerViewCount) {
        mHeaderViewCount = headerViewCount;
        return this;
    }

    public boolean isSourceDatasAlreadySorted() {
        return isSourceDatasAlreadySorted;
    }

    /**
     * 源数据 是否已经有序
     *
     * @param sourceDatasAlreadySorted
     * @return
     */
    public IndexHelper setSourceDatasAlreadySorted(boolean sourceDatasAlreadySorted) {
        isSourceDatasAlreadySorted = sourceDatasAlreadySorted;
        return this;
    }

}
