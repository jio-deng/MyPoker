package com.dzm.mypoker.bean;

public class Card {
    public static final int COLOR_SPADE = 0;
    public static final int COLOR_HEART = 1;
    public static final int COLOR_CLUB = 2;
    public static final int COLOR_DIAMOND = 3;
    public static final int COLOR_INVALID = -1;

    public int color;
    public int value;

    public Card(int color, int value) {
        this.color = color;
        this.value = value;
    }
}
