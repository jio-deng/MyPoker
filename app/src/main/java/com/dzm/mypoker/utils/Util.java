package com.dzm.mypoker.utils;

import com.dzm.mypoker.R;
import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.databinding.ItemSelectCardBinding;

import static com.dzm.mypoker.bean.Card.COLOR_INVALID;

public class Util {
    public static void refreshCardItem(ItemSelectCardBinding dataBinding, Card card) {
        if (dataBinding == null || card == null) return;
        if (card.color == COLOR_INVALID) {
            refreshLayout(dataBinding);
            return;
        }
        dataBinding.tvValue.setText(Util.getValue(card));
        dataBinding.tvValue.setTextColor(Util.getTextColor(card));
        dataBinding.ivColor.setBackgroundResource(Util.getColorImageResource(card));
    }

    public static void refreshLayout(ItemSelectCardBinding dataBinding) {
        dataBinding.tvValue.setText("");
        dataBinding.ivColor.setBackground(null);
    }

    public static String getValue(Card card) {
        int val = card.value;
        if (val < 0) return "???";
        if (val <= 8) {
            return String.valueOf(val + 2);
        }
        return val == 9 ? "J" : val == 10 ? "Q" : val == 11 ? "K" : "A";
    }

    public static String getColor(Card card) {
        int color = card.color;
        if (color < 0) return "???";
        if (color == 0) {
            return "SPADE";
        } else if (color == 1) {
            return "HEART";
        } else if (color == 2) {
            return "CLUB";
        } else {
            return "DIAMOND";
        }
    }

    public static int getTextColor(Card card) {
        return card.color == Card.COLOR_DIAMOND || card.color == Card.COLOR_HEART ? 0xFFFF0000 : 0xFF000000;
    }

    public static int getColorImageResource(Card card) {
        return card.color == Card.COLOR_CLUB ? R.drawable.club :
                card.color == Card.COLOR_DIAMOND ? R.drawable.diamond :
                        card.color == Card.COLOR_HEART ? R.drawable.heart :
                                R.drawable.spade;
    }
}
