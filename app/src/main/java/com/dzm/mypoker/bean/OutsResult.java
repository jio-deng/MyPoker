package com.dzm.mypoker.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutsResult {
    public Map<Integer, List<List<Card>>> map;
    public int totalCombination;

    public OutsResult() {
        map = new HashMap<>();
    }
}
