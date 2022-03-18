package com.dzm.mypoker.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.dzm.mypoker.R;
import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.bean.OutsResult;
import com.dzm.mypoker.bean.RivalResult;
import com.dzm.mypoker.bean.WinRateResult;
import com.dzm.mypoker.databinding.FragmentHomePageBinding;
import com.dzm.mypoker.databinding.ItemSelectCardBinding;
import com.dzm.mypoker.main.MainViewModel;
import com.dzm.mypoker.select.ui.SelectCardDialogFragment;
import com.dzm.mypoker.utils.CalculateUtils;
import com.dzm.mypoker.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dzm.mypoker.MainActivity.INDEX_RIVAL_PAGE;
import static com.dzm.mypoker.utils.CalculateUtils.INVALID;

public class HomePageFragment extends Fragment {
    private static final String TAG = "HomePageFragment";
    private static final int TOTAL_LENGTH= 9;

    private FragmentHomePageBinding mDataBinding;
    private MainViewModel mViewModel;

    private static final int PUBLIC_CARDS = 0;
    private static final int YOUR_CARDS = 1;
    private static final int RIVAL_CARDS = 2;

    private int curSelectIndex = -1;
    private MutableLiveData<Card> mCardLiveData;
    private boolean mWinRateCalculating = false;

    private final ItemSelectCardBinding[] mAllItemDataBinding = new ItemSelectCardBinding[TOTAL_LENGTH];
    private Card[] mAllItemCards = new Card[TOTAL_LENGTH];

    private final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calculateIndex(v);
            SelectCardDialogFragment.showSelectCardDialog(getActivity().getSupportFragmentManager(), mCardLiveData);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false);
        initViews();
        return mDataBinding.getRoot();
    }

    private void initViews() {
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        Log.i(TAG, "initViews: vm=" + mViewModel);

        mAllItemDataBinding[0] = mDataBinding.common1;
        mAllItemDataBinding[1] = mDataBinding.common2;
        mAllItemDataBinding[2] = mDataBinding.common3;
        mAllItemDataBinding[3] = mDataBinding.common4;
        mAllItemDataBinding[4] = mDataBinding.common5;
        mAllItemDataBinding[5] = mDataBinding.mine1;
        mAllItemDataBinding[6] = mDataBinding.mine2;
        mAllItemDataBinding[7] = mDataBinding.rival1;
        mAllItemDataBinding[8] = mDataBinding.rival2;

        mDataBinding.common1.getRoot().setOnClickListener(mListener);
        mDataBinding.common2.getRoot().setOnClickListener(mListener);
        mDataBinding.common3.getRoot().setOnClickListener(mListener);
        mDataBinding.common4.getRoot().setOnClickListener(mListener);
        mDataBinding.common5.getRoot().setOnClickListener(mListener);
        mDataBinding.mine1.getRoot().setOnClickListener(mListener);
        mDataBinding.mine2.getRoot().setOnClickListener(mListener);
        mDataBinding.rival1.getRoot().setOnClickListener(mListener);
        mDataBinding.rival2.getRoot().setOnClickListener(mListener);

        mCardLiveData = new MutableLiveData<>();
        mCardLiveData.observe(getActivity(), card -> {
            if (curSelectIndex == -1) return;
            if (card.color != -1) {
                for (int i = 0; i < mAllItemCards.length; i ++) {
                    if (i != curSelectIndex
                            && mAllItemCards[i].value == card.value
                            && mAllItemCards[i].color == card.color) {
                        mAllItemCards[i] = new Card(-1, -1);
                        Util.refreshCardItem(mAllItemDataBinding[i], mAllItemCards[i]);
                    }
                }
            }
            mAllItemCards[curSelectIndex] = card;
            Util.refreshCardItem(mAllItemDataBinding[curSelectIndex], card);
            mViewModel.getHomePageLiveData().setValue(mAllItemCards);
        });

        mViewModel.getWinRateResult().observe(getActivity(), winRateResult -> {
            float total = winRateResult.totalCombination, win = winRateResult.winCombination,
                    tie = winRateResult.tieCombination;
            float winRate = total == 0f ? 0f : win / total;
            float tieRate = total == 0f ? 0f : tie / total;
            winRate *= 100;
            tieRate *= 100;
            StringBuilder sb = new StringBuilder();
            sb.append("胜率:").append(winRate).append("%").append("\n");
            sb.append("平局率:").append(tieRate).append("%").append("\n");
            mDataBinding.showResult.setText(sb.toString());
        });

        mDataBinding.calculateWinRate.setOnClickListener( v -> {
            if (mWinRateCalculating) return;
            int result1 = CalculateUtils.judgeYourCards2(getCards(YOUR_CARDS));
            int result2 = CalculateUtils.judgeYourCards2(getCards(RIVAL_CARDS));
            if (result1 == INVALID || result2 == INVALID) {
                Toast.makeText(getActivity(), "result is valid!", Toast.LENGTH_SHORT).show();
                return;
            }
            mWinRateCalculating = true;
            WinRateResult winRateResult = CalculateUtils.calculateWinRateResult(
                    getCards(PUBLIC_CARDS), getCards(YOUR_CARDS), getCards(RIVAL_CARDS));
            mViewModel.getWinRateResult().setValue(winRateResult);
            mDataBinding.showResult.setVisibility(View.VISIBLE);
            mWinRateCalculating = false;
        });

        mDataBinding.calculateRivals.setOnClickListener( v -> {
            int result = CalculateUtils.judgeYourCards(getCards(PUBLIC_CARDS), getCards(YOUR_CARDS));
            if (result == INVALID) {
                Toast.makeText(getActivity(), "result is valid!", Toast.LENGTH_SHORT).show();
                return;
            }
            RivalResult rivalResult = CalculateUtils.findBiggerRivals(getCards(PUBLIC_CARDS), getCards(YOUR_CARDS));
            OutsResult outsResult = CalculateUtils.calculateOuts(getCards(PUBLIC_CARDS), getCards(YOUR_CARDS));
            mViewModel.getRivalLiveData().setValue(rivalResult);
            mViewModel.getOutsLiveData().setValue(outsResult);
            mViewModel.getChangeSelectedLoc().setValue(INDEX_RIVAL_PAGE);
            mDataBinding.showResult.setVisibility(View.GONE);
        });

        mDataBinding.clearAll.setOnClickListener( v -> {
            for (int i = 0; i < TOTAL_LENGTH; i ++) {
                Util.refreshLayout(mAllItemDataBinding[i]);
                mAllItemCards[i] = new Card(-1, -1);
            }
            mViewModel.getHomePageLiveData().setValue(mAllItemCards);
        });

        if (mViewModel.getHomePageLiveData().getValue() != null) {
            mAllItemCards = mViewModel.getHomePageLiveData().getValue();
            for (int i = 0; i < mAllItemCards.length; i ++) {
                Log.i(TAG, "reload data: i=" + i + ", color=" + mAllItemCards[i].color + ", val=" + mAllItemCards[i].value);
                if (mAllItemCards[i].color != -1) {
                    Util.refreshCardItem(mAllItemDataBinding[i], mAllItemCards[i]);
                }
            }
        } else {
            for (int i = 0; i < TOTAL_LENGTH; i ++) {
                mAllItemCards[i] = new Card(-1, -1);
            }
        }
    }

    private void calculateIndex(View v) {
        if (v == mDataBinding.common1.getRoot()) {
            curSelectIndex = 0;
        } else if (v == mDataBinding.common2.getRoot()) {
            curSelectIndex = 1;
        } else if (v == mDataBinding.common3.getRoot()) {
            curSelectIndex = 2;
        } else if (v == mDataBinding.common4.getRoot()) {
            curSelectIndex = 3;
        } else if (v == mDataBinding.common5.getRoot()) {
            curSelectIndex = 4;
        } else if (v == mDataBinding.mine1.getRoot()) {
            curSelectIndex = 5;
        } else if (v == mDataBinding.mine2.getRoot()) {
            curSelectIndex = 6;
        } else if (v == mDataBinding.rival1.getRoot()) {
            curSelectIndex = 7;
        } else if (v == mDataBinding.rival2.getRoot()) {
            curSelectIndex = 8;
        }
    }

    private List<Card> getCards(int flag) {
        List<Card> result = new ArrayList<>();
        if (flag == PUBLIC_CARDS) {
            result.addAll(Arrays.asList(mAllItemCards).subList(0, 5));
        } else if (flag == YOUR_CARDS) {
            result.addAll(Arrays.asList(mAllItemCards).subList(5, 7));
        } else if (flag == RIVAL_CARDS) {
            result.addAll(Arrays.asList(mAllItemCards).subList(7, 9));
        }
        return result;
    }
}
