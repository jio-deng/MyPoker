package com.dzm.mypoker.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RivalResult {
    public Map<Integer, List<List<Card>>> map;
    public int totalCombination;
    public int winCombination;
    public int tieCombination;

    public RivalResult() {
        map = new HashMap<>();
    }
}
