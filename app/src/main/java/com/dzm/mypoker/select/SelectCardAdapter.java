package com.dzm.mypoker.select;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.mypoker.R;
import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.databinding.ItemSelectCardBinding;
import com.dzm.mypoker.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class SelectCardAdapter extends RecyclerView.Adapter<SelectCardAdapter.SelectCardViewHolder> {
    private final List<Card> mData = new ArrayList<>();
    private MutableLiveData<Card> mCardLiveData;
    private ItemSelected mCallback;

    public SelectCardAdapter() {
        createData();
    }

    public void setLiveData(MutableLiveData<Card> liveData, ItemSelected callback) {
        mCardLiveData = liveData;
        mCallback = callback;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSelectCardBinding dataBinding = DataBindingUtil.inflate(inflater,
                R.layout.item_select_card, parent, false);
        return new SelectCardViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectCardViewHolder holder, int position) {
        Card card = mData.get(position);
        holder.onBind(card);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void createData() {
        for (int i = 0; i < 4; i ++) {
            for (int j = 0; j < 13; j ++) {
                Card card = new Card(i, j);
                mData.add(card);
            }
            mData.add(new Card(-1, -1));
        }
    }

    class SelectCardViewHolder extends RecyclerView.ViewHolder {
        ItemSelectCardBinding mDataBinding;

        public SelectCardViewHolder(ItemSelectCardBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding = dataBinding;
        }

        public void onBind(Card card) {
            if (card.color == Card.COLOR_INVALID) {
                Util.refreshLayout(mDataBinding);
            } else {
                Util.refreshCardItem(mDataBinding, card);
            }
            mDataBinding.getRoot().setOnClickListener(v -> {
                if (mCardLiveData != null) mCardLiveData.postValue(card);
                if (mCallback != null) mCallback.onItemSelected();
            });
        }
    }

    public interface ItemSelected {
        void onItemSelected();
    }
}
