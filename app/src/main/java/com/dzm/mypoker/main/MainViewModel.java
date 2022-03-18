package com.dzm.mypoker.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.bean.OutsResult;
import com.dzm.mypoker.bean.RivalResult;
import com.dzm.mypoker.bean.WinRateResult;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> mChangeSelectedLoc;
    private MutableLiveData<Card[]> mHomePageLiveData;
    private MutableLiveData<RivalResult> mRivalLiveData;
    private MutableLiveData<OutsResult> mOutsLiveData;
    private MutableLiveData<WinRateResult> mWinRateData;

    public MutableLiveData<Integer> getChangeSelectedLoc() {
        if (mChangeSelectedLoc == null) {
            mChangeSelectedLoc = new MutableLiveData<>();
        }
        return mChangeSelectedLoc;
    }

    public MutableLiveData<Card[]> getHomePageLiveData() {
        if (mHomePageLiveData == null) {
            mHomePageLiveData = new MutableLiveData<>();
        }
        return mHomePageLiveData;
    }

    public MutableLiveData<RivalResult> getRivalLiveData() {
        if (mRivalLiveData == null) {
            mRivalLiveData = new MutableLiveData<>();
        }
        return mRivalLiveData;
    }

    public MutableLiveData<OutsResult> getOutsLiveData() {
        if (mOutsLiveData == null) {
            mOutsLiveData = new MutableLiveData<>();
        }
        return mOutsLiveData;
    }

    public MutableLiveData<WinRateResult> getWinRateResult() {
        if (mWinRateData == null) {
            mWinRateData = new MutableLiveData<>();
        }
        return mWinRateData;
    }
}
