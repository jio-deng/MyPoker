package com.dzm.mypoker.select.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * base类
 *
 * 1.重写onActivityCreated，防止内存泄漏
 *
 * @author dzm
 */
public class BaseDialogFragment extends DialogFragment {
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";

    private Dialog mDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mDialog = getDialog();
        boolean isShow = this.getShowsDialog();
        this.setShowsDialog(false);
        super.onActivityCreated(savedInstanceState);
        this.setShowsDialog(isShow);

        View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            mDialog.setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null) {
            mDialog.setOwnerActivity(activity);
        }

        DetachableDialogDismissListener dismissListener = DetachableDialogDismissListener.wrap(this);
        mDialog.setOnDismissListener(dismissListener);

        if (savedInstanceState != null) {
            Bundle dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG);
            if (dialogState != null) {
                mDialog.onRestoreInstanceState(dialogState);
            }
        }

        dismissListener.clearOnDetach(mDialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.setOnShowListener(null);
            mDialog.setOnCancelListener(null);
            mDialog.setOnDismissListener(null);
        }
    }
}
