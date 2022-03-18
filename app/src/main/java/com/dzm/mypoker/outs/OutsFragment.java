package com.dzm.mypoker.outs;

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
import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.bean.OutsResult;
import com.dzm.mypoker.databinding.FragmentOutsPageBinding;
import com.dzm.mypoker.main.MainViewModel;

import java.util.List;

public class OutsFragment extends Fragment {
    private static final String TAG = "OutsFragment";

    private FragmentOutsPageBinding mDataBinding;
    private MainViewModel mViewModel;
    private OutsAdapter mAdapter;
    private OutsResult mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_outs_page, container, false);
        initViews();
        return mDataBinding.getRoot();
    }

    private void initViews() {
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        mViewModel.getOutsLiveData().observe(getActivity(), rivalResult -> {
            mData = rivalResult;
            getAdapter().setData(mData);
            refreshDataDisplay();
        });
        mData = mViewModel.getOutsLiveData().getValue();
        refreshDataDisplay();
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mDataBinding.recyclerView.setAdapter(getAdapter());
    }

    public OutsAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new OutsAdapter();
            mAdapter.setData(mData);
        }
        return mAdapter;
    }

    private void refreshDataDisplay() {
        if (mData == null) return;
        int total = mData.totalCombination;
        if (total <= 0) {
            Log.e(TAG, "refreshDataDisplay: total <= 0 ????   total=" + total);
            return;
        }
        int up = 0;
        for (List<List<Card>> l : mData.map.values()) {
            up += l.size();
        }
        mDataBinding.result1.setText(String.valueOf(total));
        mDataBinding.result2.setText(String.valueOf(up));
    }
}
