package com.dzm.mypoker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.dzm.mypoker.databinding.ActivityMainBinding;
import com.dzm.mypoker.home.HomePageFragment;
import com.dzm.mypoker.learn.InfoPageFragment;
import com.dzm.mypoker.main.MainViewModel;
import com.dzm.mypoker.outs.OutsFragment;
import com.dzm.mypoker.rival.RivalFragment;
import com.dzm.mypoker.support.SupportFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_FRAGMENT = "tag_fragment";

    public static final int INDEX_HOME_PAGE = 0;
    public static final int INDEX_RIVAL_PAGE = 1;
    public static final int INDEX_OUTS_PAGE = 2;
    public static final int INDEX_INFO = 3;
    public static final int INDEX_SUPPORT = 4;

    private MainViewModel mViewModel;
    private ActivityMainBinding mDataBinding;
    private final Fragment[] mFragments = new Fragment[5];
    private final View.OnClickListener mClickListener = v -> showFragment(calculateIndex(v));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getChangeSelectedLoc().observe(this, integer -> {
            if (integer == null) return;
            showFragment(integer);
        });

        mDataBinding.tab1.setOnClickListener(mClickListener);
        mDataBinding.tab2.setOnClickListener(mClickListener);
        mDataBinding.tab3.setOnClickListener(mClickListener);
        mDataBinding.tab4.setOnClickListener(mClickListener);
        mDataBinding.tab5.setOnClickListener(mClickListener);

        showFragment(INDEX_HOME_PAGE);
    }

    private int calculateIndex(View v) {
        if (v == mDataBinding.tab1) {
            return 0;
        } else if (v == mDataBinding.tab2) {
            return 1;
        } else if (v == mDataBinding.tab3) {
            return 2;
        } else if (v == mDataBinding.tab4) {
            return 3;
        } else if (v == mDataBinding.tab5) {
            return 4;
        }
        return -1;
    }

    private void showFragment(int index) {
        if (index < 0) return;
        Fragment f = null;
        if (index == INDEX_HOME_PAGE) {
            if (mFragments[index] == null) {
                mFragments[index] = new HomePageFragment();
            }
            f = mFragments[index];
        } else if (index == INDEX_INFO) {
            if (mFragments[index] == null) {
                mFragments[index] = new InfoPageFragment();
            }
            f = mFragments[index];
        } else if (index == INDEX_RIVAL_PAGE) {
            if (mFragments[index] == null) {
                mFragments[index] = new RivalFragment();
            }
            f = mFragments[index];
        } else if (index == INDEX_SUPPORT) {
            if (mFragments[index] == null) {
                mFragments[index] = new SupportFragment();
            }
            f = mFragments[index];
        } else if (index == INDEX_OUTS_PAGE) {
            if (mFragments[index] == null) {
                mFragments[index] = new OutsFragment();
            }
            f = mFragments[index];
        }

        if (f == null) return;
        getSupportFragmentManager().beginTransaction()
                .replace(mDataBinding.fragmentContainer.getId(), f, TAG_FRAGMENT)
                .commitAllowingStateLoss();
    }
}