package com.dzm.mypoker.learn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dzm.mypoker.R;
import com.dzm.mypoker.databinding.FragmentInfoPageBinding;
import com.dzm.mypoker.webview.WebViewActivity;

public class InfoPageFragment extends Fragment {

    private FragmentInfoPageBinding mDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_page, container, false);
        initViews();
        return mDataBinding.getRoot();
    }

    private void initViews() {
        mDataBinding.resource1.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_1), false, getString(R.string.info_res_1)));
        mDataBinding.resource2.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_2), false, getString(R.string.info_res_2)));
        mDataBinding.resource3.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_3), false, getString(R.string.info_res_3)));
        mDataBinding.resource4.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_4), false, getString(R.string.info_res_4)));
        mDataBinding.resource5.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_5), false, getString(R.string.info_res_5)));
        mDataBinding.resource6.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_6), false, getString(R.string.info_res_6)));
        mDataBinding.resource7.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_7), false, getString(R.string.info_res_7)));
        mDataBinding.resource8.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_8), false, getString(R.string.info_res_8)));
        mDataBinding.resource9.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_9), false, getString(R.string.info_res_9)));
        mDataBinding.resource10.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_10), false, getString(R.string.info_res_10)));
        mDataBinding.resource11.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_11), false, getString(R.string.info_res_11)));
        mDataBinding.resource12.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_12), false, getString(R.string.info_res_12)));
        mDataBinding.resource13.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_13), false, getString(R.string.info_res_13)));
        mDataBinding.resource14.setOnClickListener( v ->
                WebViewActivity.launch(getContext(), getString(R.string.info_link_14), false, getString(R.string.info_res_14)));

    }
}
