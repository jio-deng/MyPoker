package com.dzm.mypoker.rival;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dzm.mypoker.R;
import com.dzm.mypoker.bean.RivalResult;
import com.dzm.mypoker.databinding.FragmentRivalPageBinding;
import com.dzm.mypoker.main.MainViewModel;

import java.text.DecimalFormat;

public class RivalFragment extends Fragment {
    private static final String TAG = "RivalFragment";

    private FragmentRivalPageBinding mDataBinding;
    private MainViewModel mViewModel;
    private RivalAdapter mAdapter;
    private RivalResult mData;

    private final DecimalFormat mDf = new DecimalFormat("#00.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rival_page, container, false);
        initViews();
        return mDataBinding.getRoot();
    }

    private void initViews() {
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        mViewModel.getRivalLiveData().observe(getActivity(), rivalResult -> {
            mData = rivalResult;
            getAdapter().setData(mData);
            refreshDataDisplay();
        });
        mData = mViewModel.getRivalLiveData().getValue();
        refreshDataDisplay();
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mDataBinding.recyclerView.setAdapter(getAdapter());
    }

    public RivalAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new RivalAdapter();
            mAdapter.setData(mData);
        }
        return mAdapter;
    }

    private void refreshDataDisplay() {
        if (mData == null) return;
        int total = mData.totalCombination;
        int win = mData.winCombination;
        int tie = mData.tieCombination;
        if (total <= 0) {
            Log.e(TAG, "refreshDataDisplay: total <= 0 ????   total=" + total);
            return;
        }
        mDataBinding.result1.setText(String.valueOf(total));
        mDataBinding.result2.setText(String.valueOf(win));
        mDataBinding.result3.setText(String.valueOf(tie));
        StringBuilder sb = new StringBuilder();
        sb.append(mDf.format(((float) total - win - tie) / total * 100 )).append("%获胜");
        if (tie > 0) {
            sb.append(",").append(mDf.format(((float) tie) / total * 100)).append("%平局");
        }
        mDataBinding.result4.setText(sb.toString());
    }
}
