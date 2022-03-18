package com.dzm.mypoker.bean;

public class WinRateResult {
    public int totalCombination;
    public int winCombination;
    public int tieCombination;

    public void merge(int win, int tie) {
        synchronized (this) {
            winCombination += win;
            tieCombination += tie;
        }
    }
}
