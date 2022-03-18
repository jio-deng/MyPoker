package com.dzm.mypoker.utils;

import android.util.Log;

import com.dzm.mypoker.bean.Card;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    private static final String TAG = "TestUtil";

    public static void test1() {
        Card card1 = new Card(1, 2);
        Card card2 = new Card(2, 4);
        Card card3 = new Card(3, 4);
        Card card4 = new Card(1, 12);
        Card card5 = new Card(2, 1);
        Card card6 = new Card(3, 11);
        Card card7 = new Card(1, 1);
        List<Card> commonCards = new ArrayList<>();
        commonCards.add(card1);
        commonCards.add(card2);
        commonCards.add(card3);
        List<Card> myCards = new ArrayList<>();
        myCards.add(card4);
        myCards.add(card5);
        List<Card> rivalCards = new ArrayList<>();
        rivalCards.add(card6);
        rivalCards.add(card7);
        int res = CalculateUtils.compareCards(commonCards, myCards, rivalCards);
        Log.i(TAG, "test1: res=" + res);
    }
}
