package com.dzm.mypoker.support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dzm.mypoker.R;
import com.dzm.mypoker.databinding.FragmentSupportPageBinding;
import com.dzm.mypoker.utils.TestUtil;

public class SupportFragment extends Fragment {
    private FragmentSupportPageBinding mDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_support_page, container, false);
        initViews();
        return mDataBinding.getRoot();
    }

    private void initViews() {
        mDataBinding.btnTest1.setOnClickListener( v -> {
            TestUtil.test1();
        });
    }
}
