package com.dzm.mypoker.select.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dzm.mypoker.R;
import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.databinding.DialogSelectCardBinding;
import com.dzm.mypoker.select.SelectCardAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SelectCardDialogFragment extends DialogFragment implements SelectCardAdapter.ItemSelected {

    private DialogSelectCardBinding mDataBinding;
    private MutableLiveData<Card> mCardLiveData;

    private SelectCardAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                R.layout.dialog_select_card, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.DialogFullScreen);
        initViews();
        dialog.setContentView(mDataBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null) {
            // 软键盘弹出时不会影响布局
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            // 设置进场/出场动画
            window.setWindowAnimations(R.style.DialogBottomAnim);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置高度
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void initViews() {
        mAdapter = new SelectCardAdapter();
        mAdapter.setLiveData(mCardLiveData, this);
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
    }

    public void setLiveData(MutableLiveData<Card> liveData) {
        mCardLiveData = liveData;
        if (mAdapter != null) {
            mAdapter.setLiveData(liveData, this);
        }
    }

    public static void showSelectCardDialog(FragmentManager fragmentManager, MutableLiveData<Card> cardLiveData) {
        if (fragmentManager == null) {
            return;
        }
        SelectCardDialogFragment dialogFragment = (SelectCardDialogFragment) fragmentManager.findFragmentByTag("select_card");
        if (dialogFragment == null) {
            dialogFragment = new SelectCardDialogFragment();
            dialogFragment.show(fragmentManager, "select_card");
        }
        dialogFragment.setLiveData(cardLiveData);
    }

    @Override
    public void onItemSelected() {
        dismiss();
    }
}
