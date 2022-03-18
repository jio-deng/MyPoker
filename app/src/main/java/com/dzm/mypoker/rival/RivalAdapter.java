package com.dzm.mypoker.rival;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.mypoker.R;
import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.bean.RivalResult;
import com.dzm.mypoker.databinding.ItemRivalCardBinding;
import com.dzm.mypoker.databinding.ItemRivalHeaderBinding;
import com.dzm.mypoker.utils.CalculateUtils;
import com.dzm.mypoker.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class RivalAdapter extends RecyclerView.Adapter<RivalAdapter.RivalViewHolder> {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CARD = 1;

    private List<Object> mData = new ArrayList<>();

    public void setData(RivalResult data) {
        if (data == null) return;
        mData = new ArrayList<>();
        for (int i = 9; i >= 0; i --) {
            if (data.map.containsKey(i) && data.map.get(i).size() > 0) {
                mData.add(CalculateUtils.calculateResultText(i));
                mData.addAll(data.map.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RivalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_TITLE) {
            ItemRivalHeaderBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_rival_header, null, false);
            return new RivalViewHolder(dataBinding);
        } else {
            ItemRivalCardBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_rival_card, null, false);
            return new RivalViewHolder(dataBinding);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = mData.get(position);
        return obj instanceof String ? TYPE_TITLE : TYPE_CARD;
    }

    @Override
    public void onBindViewHolder(@NonNull RivalViewHolder holder, int position) {
        int type = getItemViewType(position);
        Object obj = mData.get(position);
        if (type == TYPE_CARD) {
            holder.bind1((List<Card>) obj);
        } else {
            holder.bind2((String) obj);
        }
    }

    class RivalViewHolder extends RecyclerView.ViewHolder {
        private ItemRivalCardBinding mDataBinding1;
        private ItemRivalHeaderBinding mDataBinding2;

        public RivalViewHolder(ItemRivalCardBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding1 = dataBinding;
        }

        public RivalViewHolder(ItemRivalHeaderBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding2 = dataBinding;
        }

        public void bind1(List<Card> cards) {
            Util.refreshCardItem(mDataBinding1.card1, cards.get(0));
            Util.refreshCardItem(mDataBinding1.card2, cards.get(1));
        }

        public void bind2(String title) {
            mDataBinding2.text.setText(title);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_TITLE ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
}
