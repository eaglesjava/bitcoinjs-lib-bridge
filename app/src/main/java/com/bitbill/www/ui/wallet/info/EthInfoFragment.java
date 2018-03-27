package com.bitbill.www.ui.wallet.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.R;
import com.bitbill.www.common.base.model.entity.TabItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EthInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EthInfoFragment extends Fragment {

    public EthInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EthInfoFragment.
     * @param tabItem
     */
    // TODO: Rename and change types and number of parameters
    public static EthInfoFragment newInstance(TabItem tabItem) {
        EthInfoFragment fragment = new EthInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eth_info, container, false);
    }

}
