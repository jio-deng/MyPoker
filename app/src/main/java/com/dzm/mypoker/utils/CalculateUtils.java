package com.dzm.mypoker.utils;

import android.util.Log;

import com.dzm.mypoker.MyApplication;
import com.dzm.mypoker.R;
import com.dzm.mypoker.bean.Card;
import com.dzm.mypoker.bean.OutsResult;
import com.dzm.mypoker.bean.RivalResult;
import com.dzm.mypoker.bean.WinRateResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.dzm.mypoker.bean.Card.COLOR_INVALID;

public class CalculateUtils {
    private static final String TAG = "CalculateUtils";

    public static final int INVALID = -2;
    public static final int VALID = -1;
    public static final int HIGH_CARD = 0;
    public static final int ONE_PAIR = 1;
    public static final int TWO_PAIRS = 2;
    public static final int THREE_OF_KIND = 3;
    public static final int STRAIGHT = 4;
    public static final int FLUSH = 5;
    public static final int FULL_HOUSE = 6;
    public static final int FOUR_OF_A_KIND = 7;
    public static final int STRAIGHT_FLUSH = 8;
    public static final int ROYAL_FLUSH = 9;

    public static String calculateResultText(int result) {
        if (result == ROYAL_FLUSH) {
            return MyApplication.getApp().getString(R.string.result_cars_val_royal_flush);
        } else if (result == STRAIGHT_FLUSH) {
            return MyApplication.getApp().getString(R.string.result_cars_val_straight_flush);
        } else if (result == FOUR_OF_A_KIND) {
            return MyApplication.getApp().getString(R.string.result_cars_val_four_of_a_kind);
        } else if (result == FULL_HOUSE) {
            return MyApplication.getApp().getString(R.string.result_cars_val_full_house);
        } else if (result == FLUSH) {
            return MyApplication.getApp().getString(R.string.result_cars_val_flush);
        } else if (result == STRAIGHT) {
            return MyApplication.getApp().getString(R.string.result_cars_val_straight);
        } else if (result == THREE_OF_KIND) {
            return MyApplication.getApp().getString(R.string.result_cars_val_three_of_a_kind);
        } else if (result == TWO_PAIRS) {
            return MyApplication.getApp().getString(R.string.result_cars_val_two_pairs);
        } else if (result == ONE_PAIR) {
            return MyApplication.getApp().getString(R.string.result_cars_val_one_pair);
        } else if (result == HIGH_CARD) {
            return MyApplication.getApp().getString(R.string.result_cars_val_high_card);
        } else {
            return MyApplication.getApp().getString(R.string.result_cars_val_invalid);
        }
    }

    public static int judgeYourCards(List<Card> publicCards, List<Card> yourCards) {
        if (publicCards == null) {
            return INVALID;
        }
        for (int i = publicCards.size() - 1; i >= 0; i --) {
            if (publicCards.get(i).color == COLOR_INVALID) {
                publicCards.remove(i);
            }
        }
        if (publicCards.size() < 3 || publicCards.size() > 5) {
            return INVALID;
        }
        int res = judgeYourCards2(yourCards);
        if (res == INVALID) {
            return INVALID;
        }
        List<Card> list = new ArrayList<>();
        list.addAll(publicCards);
        list.addAll(yourCards);
        return judgeYourCardsCore(list);
    }

    public static int judgeYourCards2(List<Card> yourCards) {
        if (yourCards == null) {
            return INVALID;
        }
        for (int i = yourCards.size() - 1; i >= 0; i --) {
            if (yourCards.get(i).color == COLOR_INVALID) {
                yourCards.remove(i);
            }
        }
        if (yourCards.size() != 2) {
            return INVALID;
        }
        return VALID;
    }

    private static int judgeYourCardsCore(List<Card> cards) {
        if (cards == null || cards.size() == 0 || cards.size() > 7) {
            return INVALID;
        }
        if (containsSame(cards)) {
            return INVALID;
        }

        if (isRoyalFlush(cards)) {
            return ROYAL_FLUSH;
        }

        if (isFlushStraight(cards)) {
            return STRAIGHT_FLUSH;
        }

        if (isFourOfKind(cards)) {
            return FOUR_OF_A_KIND;
        }

        boolean isThreeOfKind = isThreeOfKind(cards);
        boolean isTwoPairs = isTwoPair(cards);
        if (isThreeOfKind && isTwoPairs) {
            return FULL_HOUSE;
        }

        if (isFlush(cards)) {
            return FLUSH;
        }

        if (isStraight(cards)) {
            return STRAIGHT;
        }

        if (isThreeOfKind) {
            return THREE_OF_KIND;
        }

        if (isTwoPairs) {
            return TWO_PAIRS;
        }

        if (isPair(cards)) {
            return ONE_PAIR;
        }

        return HIGH_CARD;
    }

    /**
     * calculate win rate
     *
     * @param commonCards public zone
     * @param myCards your cards
     * @param rivalCards rival's cards
     */
    public static WinRateResult calculateWinRateResult(List<Card> commonCards, List<Card> myCards,
                                                       List<Card> rivalCards) {
        long t1 = System.currentTimeMillis();
        Log.i(TAG, "calculateWinRateResult: t1 " + t1);
        WinRateResult winRateResult = new WinRateResult();
        if (commonCards == null || myCards == null || rivalCards == null) {
            return winRateResult;
        }
        for (int i = commonCards.size() - 1; i >= 0; i --) {
            if (commonCards.get(i).color == COLOR_INVALID) {
                commonCards.remove(i);
            }
        }
        for (int i = myCards.size() - 1; i >= 0; i --) {
            if (myCards.get(i).color == COLOR_INVALID) {
                myCards.remove(i);
            }
        }
        for (int i = rivalCards.size() - 1; i >= 0; i --) {
            if (rivalCards.get(i).color == COLOR_INVALID) {
                rivalCards.remove(i);
            }
        }
        if (commonCards.size() > 5 || myCards.size() != 2 || rivalCards.size() != 2) {
            return winRateResult;
        }
        List<Card> used = new ArrayList<>();
        used.addAll(commonCards);
        used.addAll(myCards);
        used.addAll(rivalCards);
        List<List<Card>> cardsList = fillAllCombinations(used, 5 - commonCards.size());
        long t2 = System.currentTimeMillis();
        Log.i(TAG, "calculateWinRateResult: t2 " + t2 + ", dif=" + (t2 - t1));

        // TODO 使用100个子线程去计算，提升30%左右，目前计算时间太长了，有问题
        int count = 100, chunk = cardsList.size() / count, start = 0;
        Thread[] threads = new Thread[count];
//        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i ++) {
            int s = start, e = i == count - 1 ? cardsList.size() : start + chunk;
            start = e;
            threads[i] =  new Thread(() -> {
                int win = 0, tie = 0;
                for (int j = s; j < e; j ++) {
                    List<Card> l = cardsList.get(j);
                    l.addAll(commonCards);
                    int result = compareCards(l, myCards, rivalCards);
                    if (result > 0) {
                        win ++;
                    } else if (result == 0) {
                        tie ++;
                    }
                }
                winRateResult.merge(win, tie);
            });
        }

        for (int i = 0; i < count; i ++) {
            threads[i].start();
        }
        for (int i = 0; i < count; i ++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        winRateResult.totalCombination = cardsList.size();
//        for (List<Card> l : cardsList) {
//            l.addAll(commonCards);
//            int result = compareCards(l, myCards, rivalCards);
//            if (result > 0) {
//                winRateResult.winCombination ++;
//            } else if (result == 0) {
//                winRateResult.tieCombination ++;
//            }
//        }
        long t3 = System.currentTimeMillis();
        Log.i(TAG, "calculateWinRateResult: t3 " + t3 + ", dif=" + (t3 - t2));
        return winRateResult;
    }

    /**
     * calculate bigger cards combination
     *
     * @param commonCards public zone
     * @param myCards my cards
     * @return all combination of rival's cards
     */
    public static RivalResult findBiggerRivals(List<Card> commonCards, List<Card> myCards) {
        RivalResult rivalResult = new RivalResult();
        if (commonCards == null || myCards == null) {
            return rivalResult;
        }
        for (int i = commonCards.size() - 1; i >= 0; i --) {
            if (commonCards.get(i).color == COLOR_INVALID) {
                commonCards.remove(i);
            }
        }
        for (int i = myCards.size() - 1; i >= 0; i --) {
            if (myCards.get(i).color == COLOR_INVALID) {
                myCards.remove(i);
            }
        }
        if (commonCards.size() < 3 || commonCards.size() > 5 || myCards.size() != 2) {
            return rivalResult;
        }
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(commonCards);
        allCards.addAll(myCards);
        int totalLevel = judgeYourCardsCore(allCards);
        findBiggerRivalsCore(commonCards, myCards, allCards, totalLevel, rivalResult);
        return rivalResult;
    }

    private static boolean containsSame(List<Card> cards) {
        if (cards == null || cards.size() == 0) {
            return false;
        }
        cards.sort((o1, o2) -> {
            if (o1.value != o2.value) {
                return o2.value - o1.value;
            }
            return o2.color - o1.color;
        });
        for (int i = 1; i < cards.size() - 4; i ++) {
            if (cards.get(i).value == cards.get(i-1).value &&
                    cards.get(i).color == cards.get(i-1).color) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRoyalFlush(List<Card> cards) {
        if (cards == null || cards.size() < 5 || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> {
            if (o1.color != o2.color) {
                return o2.color - o1.color;
            }
            return o2.value - o1.value;
        });
        boolean answer = false;
        for (int i = 0; i < cards.size() - 4; i ++) {
            boolean findFlush = true;
            int cur = cards.get(i).color;
            for (int j = i+1; j < i+5; j ++) {
                if (cards.get(j).color != cur) {
                    findFlush = false;
                    break;
                }
            }
            if (!findFlush) {
                continue;
            }
            boolean findStright = true;
            int val = 12;
            for (int j = i+1; j < i+5; j ++) {
                if (cards.get(j).value != val) {
                    findStright = false;
                    break;
                }
                val --;
            }
            if (findStright) {
                answer = true;
                break;
            }
        }
        return answer;
    }

    private static boolean isFlushStraight(List<Card> cards) {
        if (cards == null || cards.size() < 5 || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> {
            if (o1.color != o2.color) {
                return o2.color - o1.color;
            }
            return o2.value - o1.value;
        });
        boolean answer = false;
        for (int i = 0; i < cards.size() - 4; i ++) {
            boolean find = true;
            int cur = cards.get(i).value;
            int col = cards.get(i).color;
            for (int j = i+1; j < i+5; j ++) {
                if (col != cards.get(j).color || cards.get(j).value != cur-1) {
                    find = false;
                    break;
                }
                cur --;
            }
            if (find) {
                answer = true;
                break;
            }
        }
        return answer;
    }

    private static boolean isStraight(List<Card> cards) {
        if (cards == null || cards.size() < 5 || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> {
            if (o1.value != o2.value) {
                return o2.value - o1.value;
            }
            return o2.color - o1.color;
        });
        boolean answer = false;
        for (int i = 0; i < cards.size() - 4; i ++) {
            boolean find = true;
            int cur = cards.get(i).value;
            for (int j = i+1; j < i+5; j ++) {
                if (cards.get(j).value != cur-1) {
                    find = false;
                    break;
                }
                cur --;
            }
            if (find) {
                answer = true;
                break;
            }
        }
        return answer;
    }

    private static boolean isFlush(List<Card> cards) {
        if (cards == null || cards.size() < 5 || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> o2.color - o1.color);
        boolean answer = false;
        for (int i = 0; i < cards.size() - 4; i ++) {
            boolean find = true;
            int cur = cards.get(i).color;
            for (int j = i+1; j < i+5; j ++) {
                if (cards.get(j).color != cur) {
                    find = false;
                    break;
                }
            }
            if (find) {
                answer = true;
                break;
            }
        }
        return answer;
    }

    private static boolean isFourOfKind(List<Card> cards) {
        if (cards == null || cards.size() < 4 || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> o2.value - o1.value);
        boolean answer = false;
        for (int i = 0; i < cards.size() - 3; i ++) {
            boolean find = true;
            int cur = cards.get(i).value;
            for (int j = i+1; j < i+4; j ++) {
                if (cards.get(j).value != cur) {
                    find = false;
                    break;
                }
            }
            if (find) {
                answer = true;
                break;
            }
        }
        return answer;
    }

    private static boolean isThreeOfKind(List<Card> cards) {
        if (cards == null || cards.size() < 3 || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> o2.value - o1.value);
        boolean answer = false;
        for (int i = 0; i < cards.size() - 2; i ++) {
            boolean find = true;
            int cur = cards.get(i).value;
            for (int j = i+1; j < i+3; j ++) {
                if (cards.get(j).value != cur) {
                    find = false;
                    break;
                }
            }
            if (find) {
                answer = true;
                break;
            }
        }
        return answer;
    }

    private static boolean isTwoPair(List<Card> cards) {
        if (cards == null || cards.size() < 4 || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> o2.value - o1.value);
        int count = 0;
        for (int i = 0; i < cards.size() - 1; i ++) {
            if (cards.get(i).value == cards.get(i+1).value) {
                count ++;
                i ++;
            }
        }
        return count >= 2;
    }

    private static boolean isPair(List<Card> cards) {
        if (cards == null || cards.size() > 7) {
            return false;
        }
        cards.sort((o1, o2) -> o2.value - o1.value);
        for (int i = 0; i < cards.size() - 1; i ++) {
            if (cards.get(i).value == cards.get(i+1).value) {
                return true;
            }
        }
        return false;
    }

    private static void findBiggerRivalsCore(List<Card> commonCards, List<Card> myCards, List<Card> allCards,
            int totalLevel, RivalResult rivalResult) {
        List<List<Card>> allCombinations = findAllCombinations(allCards, 2);
        rivalResult.totalCombination = allCombinations.size();
        for (List<Card> item : allCombinations) {
            int level = judgeYourCards(commonCards, item);
            if (level < totalLevel) continue;

            // compare
            int res = compareCards(commonCards, myCards, item);
            if (res > 0) {
                continue;
            }
            if (res == 0) {
                rivalResult.tieCombination ++;
                continue;
            }
            rivalResult.winCombination ++;

            if (!rivalResult.map.containsKey(level)) {
                rivalResult.map.put(level, new ArrayList<>());
            }
            Objects.requireNonNull(rivalResult.map.get(level)).add(item);
        }
        Log.i(TAG, "findBiggerRivalsCore: total=" + rivalResult.totalCombination
                + ", win=" + rivalResult.winCombination + ", tie=" + rivalResult.tieCombination);
    }

    private static List<List<Card>> fillAllCombinations(List<Card> usedCards, int count) {
        List<List<Card>> res = new ArrayList<>();
        if (count <= 0) return res;
        Set<Card> set = new HashSet<>(usedCards);
        fillAllCombinationsCore(res, new ArrayList<>(), count, set, 0);
        return res;
    }

    private static void fillAllCombinationsCore(List<List<Card>> res, List<Card> list, int count,
                                                Set<Card> set, int curIndex) {
        if (list.size() == count) {
            res.add(new ArrayList<>(list));
            return;
        }
        if (curIndex >= 51) {
            return;
        }
        int color = curIndex / 13;
        int val = curIndex % 13;
        if (!containsCard(set, color, val)) {
            list.add(new Card(color, val));
            fillAllCombinationsCore(res, list, count, set, curIndex+1);
            list.remove(list.size() - 1);
        }
        fillAllCombinationsCore(res, list, count, set, curIndex+1);
    }

    private static List<List<Card>> findAllCombinations(List<Card> allCards, int count) {
        List<List<Card>> ans = new ArrayList<>();
        Set<Card> set = new HashSet<>(allCards);
        for (int i = 0; i < 51; i ++) {
            int color1 = i / 13;
            int val1 = i % 13;
            if (containsCard(set, color1, val1)) {
                continue;
            }
            if (count == 1) {
                List<Card> l = new ArrayList<>();
                l.add(new Card(color1, val1));
                ans.add(l);
            } else {
                for (int j = i+1; j < 52; j ++) {
                    int color2 = j / 13;
                    int val2 = j % 13;
                    if (containsCard(set, color2, val2)) {
                        continue;
                    }
                    List<Card> l = new ArrayList<>();
                    l.add(new Card(color1, val1));
                    l.add(new Card(color2, val2));
                    ans.add(l);
                }
            }
        }
        Log.i(TAG, "findAllCombinations: len=" + ans.size());
        return ans;
    }

    private static boolean containsCard(Set<Card> set, int color, int val) {
        if (set == null || set.size() == 0) {
            return false;
        }
        for (Card card : set) {
            if (card.value == val && card.color == color) {
                return true;
            }
        }
        return false;
    }

    public static int compareCards(List<Card> commonCards, List<Card> myCards, List<Card> rivalCards) {
        if (commonCards == null || myCards == null || rivalCards == null
                || commonCards.size() < 3 || commonCards.size() > 5 || myCards.size() != 2 || rivalCards.size() != 2) {
            Log.e(TAG, "compareCards: error commonCards=" + (commonCards == null ? "null" : commonCards.size())
                    + ", myCards=" + (myCards == null ? "null" : myCards.size())
                    + ", rivalCards=" + (rivalCards == null ? "null" : rivalCards.size()));
            return 0;
        }
        int myLevel = judgeYourCards(commonCards, myCards);
        int rivalLevel = judgeYourCards(commonCards, rivalCards);
        return compareCards(commonCards, myCards, rivalCards, myLevel, rivalLevel);
    }

    private static int compareCards(List<Card> commonCards, List<Card> myCards, List<Card> rivalCards,
                                    int myLevel, int rivalLevel) {
        if (commonCards == null || myCards == null || rivalCards == null
                || commonCards.size() < 3 || commonCards.size() > 5 || myCards.size() != 2 || rivalCards.size() != 2) {
            return 0;
        }
        if (myLevel != rivalLevel) {
            return myLevel > rivalLevel ? 1 : -1;
        }
        List<Card> list1 = new ArrayList<>(commonCards);
        list1.addAll(myCards);
        List<Card> list2 = new ArrayList<>(commonCards);
        list2.addAll(rivalCards);
        return compareCardsCore(list1, list2, myLevel, rivalLevel);
    }

    private static int compareCardsCore(List<Card> list1, List<Card> list2, int level1, int level2) {
        if (level1 != level2) {
            return level1 > level2 ? 1 : -1;
        }
        if (level1 == ROYAL_FLUSH) {
            return 0;
        }
        if (level1 == STRAIGHT_FLUSH) {
            int v1 = findFlushStraightSign(list1);
            int v2 = findFlushStraightSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == FOUR_OF_A_KIND) {
            int v1 = findFourOfAKindSign(list1);
            int v2 = findFourOfAKindSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == FULL_HOUSE) {
            int v1 = findFullHouseSign(list1);
            int v2 = findFullHouseSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == FLUSH) {
            int v1 = findFlushSign(list1);
            int v2 = findFlushSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == STRAIGHT) {
            int v1 = findStraightSign(list1);
            int v2 = findStraightSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == THREE_OF_KIND) {
            int v1 = findThreeOfAKindSign(list1);
            int v2 = findThreeOfAKindSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == TWO_PAIRS) {
            int v1 = findTwoPairsSign(list1);
            int v2 = findTwoPairsSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == ONE_PAIR) {
            int v1 = findOnePairSign(list1);
            int v2 = findOnePairSign(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        if (level1 == HIGH_CARD) {
            int v1 = findHighCard(list1);
            int v2 = findHighCard(list2);
            if (v1 == v2) return 0;
            return v1 > v2 ? 1 : -1;
        }
        return 0;
    }

    private static int findFlushStraightSign(List<Card> allCards) {
        allCards.sort((o1, o2) -> {
            if (o1.color != o2.color) {
                return o2.color - o1.color;
            }
            return o2.value - o1.value;
        });
        for (int i = 0; i < allCards.size() - 4; i ++) {
            boolean find = true;
            int cur = allCards.get(i).value;
            int col = allCards.get(i).color;
            for (int j = i+1; j < i+5; j ++) {
                if (col != allCards.get(j).color || allCards.get(j).value != cur-1) {
                    find = false;
                    break;
                }
                cur --;
            }
            if (find) {
                return cur;
            }
        }
        return 0;
    }

    private static int findFourOfAKindSign(List<Card> allCards) {
        allCards.sort((o1, o2) -> o2.value - o1.value);
        for (int i = 0; i < allCards.size() - 3; i ++) {
            boolean find = true;
            int cur = allCards.get(i).value;
            for (int j = i+1; j < i+4; j ++) {
                if (allCards.get(j).value != cur) {
                    find = false;
                    break;
                }
            }
            if (find) {
                int index = 0;
                if (i == 0) index = 4;
                return allCards.get(index).value;
            }
        }
        return 0;
    }

    private static int findFullHouseSign(List<Card> allCards) {
        List<Integer> l3 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        for (int i = 0; i < 13; i ++) {
            int count = 0;
            for (Card card : allCards) {
                if (card.value == i) count ++;
            }
            if (count >= 3) {
                l3.add(i);
            } else if (count == 2) {
                l2.add(i);
            }
        }
        l3.sort((o1, o2) -> o2 - o1);
        int res = 0;
        if (l3.isEmpty()) return 0;
        res += l3.get(0);
        res *= 100;
        l3.remove(0);
        l3.addAll(l2);
        l3.sort((o1, o2) -> o2 - o1);
        res += l3.get(0);
        return res;
    }

    private static int findFlushSign(List<Card> allCards) {
        if (allCards == null || allCards.size() < 5 || allCards.size() > 7) {
            return 0;
        }
        allCards.sort((o1, o2) -> o2.color - o1.color);
        for (int i = 0; i < allCards.size() - 4; i ++) {
            boolean find = true;
            int cur = allCards.get(i).color;
            for (int j = i+1; j < i+5; j ++) {
                if (allCards.get(j).color != cur) {
                    find = false;
                    break;
                }
            }
            if (find) {
                int res = allCards.get(i).value;
                for (int j = i+1; j < i+5; j ++) {
                    res *= 13;
                    res += allCards.get(j).value;
                }
                return res;
            }
        }
        return 0;
    }

    private static int findStraightSign(List<Card> allCards) {
        if (allCards == null || allCards.size() < 5 || allCards.size() > 7) {
            return 0;
        }
        allCards.sort((o1, o2) -> {
            if (o1.value != o2.value) {
                return o2.value - o1.value;
            }
            return o2.color - o1.color;
        });
        for (int i = 0; i < allCards.size() - 4; i ++) {
            boolean find = true;
            int cur = allCards.get(i).value;
            for (int j = i+1; j < i+5; j ++) {
                if (allCards.get(j).value != cur-1) {
                    find = false;
                    break;
                }
                cur --;
            }
            if (find) {
                return cur;
            }
        }
        return 0;
    }

    private static int findThreeOfAKindSign(List<Card> allCards) {
        List<Integer> l3 = new ArrayList<>();
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 13; i ++) {
            int count = 0;
            for (Card card : allCards) {
                if (card.value == i) count ++;
            }
            if (count >= 3) {
                l3.add(i);
            } else if (count > 0) {
                l.add(i);
            }
        }
        l3.sort((o1, o2) -> o2 - o1);
        int res = 0;
        if (l3.isEmpty()) return 0;
        res += l3.get(0);
        res *= 100;
        l3.remove(0);
        l3.addAll(l);
        l3.sort((o1, o2) -> o2 - o1);
        res += l3.get(0);
        res *= 100;
        res += l3.get(1);
        return res;
    }

    private static int findTwoPairsSign(List<Card> allCards) {
        List<Integer> l2 = new ArrayList<>();
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 13; i ++) {
            int count = 0;
            for (Card card : allCards) {
                if (card.value == i) count ++;
            }
            if (count >= 2) {
                l2.add(i);
            } else if (count > 0) {
                l.add(i);
            }
        }
        l2.sort((o1, o2) -> o2 - o1);
        int res = 0;
        if (l2.isEmpty()) return 0;
        res += l2.get(0);
        res *= 100;
        l2.remove(0);
        res += l2.get(0);
        res *= 100;
        l2.remove(0);
        l2.addAll(l);
        l2.sort((o1, o2) -> o2 - o1);
        res += l2.get(0);
        return res;
    }

    private static int findOnePairSign(List<Card> allCards) {
        List<Integer> l2 = new ArrayList<>();
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 13; i ++) {
            int count = 0;
            for (Card card : allCards) {
                if (card.value == i) count ++;
            }
            if (count >= 2) {
                l2.add(i);
            } else if (count > 0) {
                l.add(i);
            }
        }
        l2.sort((o1, o2) -> o2 - o1);
        int res = 0;
        if (l2.isEmpty()) return 0;
        res += l2.get(0);
        res *= 1000;
        l2.remove(0);
        l2.addAll(l);
        l2.sort((o1, o2) -> o2 - o1);
        res += l2.get(0);
        res *= 100;
        res += l2.get(1);
        res *= 100;
        res += l2.get(2);
        return res;
    }

    private static int findHighCard(List<Card> allCards) {
        List<Card> l = new ArrayList<>(allCards);
        l.sort((o1, o2) -> o2.value - o1.value);
        int ans = 0;
        for (Card c : l) {
            ans *= 100;
            ans += c.value;
        }
        return ans;
    }

    public static OutsResult calculateOuts(List<Card> commonCards, List<Card> myCards) {
        OutsResult outsResult = new OutsResult();
        if (commonCards == null || myCards == null) {
            return outsResult;
        }
        for (int i = commonCards.size() - 1; i >= 0; i --) {
            if (commonCards.get(i).color == COLOR_INVALID) {
                commonCards.remove(i);
            }
        }
        for (int i = myCards.size() - 1; i >= 0; i --) {
            if (myCards.get(i).color == COLOR_INVALID) {
                myCards.remove(i);
            }
        }
        if (commonCards.size() < 3 || commonCards.size() >= 5 || myCards.size() != 2) {
            return outsResult;
        }
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(commonCards);
        allCards.addAll(myCards);
        int totalLevel = judgeYourCardsCore(allCards);
        calculateOutsCore(commonCards, myCards, allCards, totalLevel, outsResult, 5 - commonCards.size());
        return outsResult;
    }

    private static void calculateOutsCore(List<Card> commonCards, List<Card> myCards,
            List<Card> allCards, int totalLevel, OutsResult outsResult, int count) {
        List<List<Card>> allCombinations = findAllCombinations(allCards, count);
        outsResult.totalCombination = allCombinations.size();
        for (List<Card> item : allCombinations) {
            List<Card> temp = new ArrayList<>(commonCards);
            temp.addAll(item);
            int level = judgeYourCards(temp, myCards);
            if (level < totalLevel) continue;

            // compare
            int res = compareCardsCore(temp, allCards, level, totalLevel);
            if (res <= 0) {
                continue;
            }

            if (!outsResult.map.containsKey(level)) {
                outsResult.map.put(level, new ArrayList<>());
            }
            Objects.requireNonNull(outsResult.map.get(level)).add(item);
        }
    }
}
