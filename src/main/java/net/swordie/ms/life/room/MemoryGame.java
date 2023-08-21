package net.swordie.ms.life.room;

import net.swordie.ms.client.character.Char;

import java.util.*;

public class MemoryGame {
    private final Map<Char, Integer> score = new HashMap<>();
    private final List<Integer> cards;
    public MemoryGame(int kind) {
        this.cards = initCards(kind);
    }

    public List<Integer> getCards() {
        return cards;
    }

    public MemoryGameResult checkCards(int card1, int card2, Char chr) {
        // 1-indexed numbers are used in the client
        if (cards.get(card1) == cards.get(card2)) {
            score.put(chr, score.getOrDefault(chr, 0) + 2);
            if (score.values().stream().mapToInt(Integer::intValue).sum() >= cards.size()) {
                int half = cards.size() / 2;
                if (score.get(chr) > half) {
                    return MemoryGameResult.WIN;
                } else if (score.get(chr) == half) {
                    return MemoryGameResult.TIE;
                } else {
                    return MemoryGameResult.LOSE;
                }
            } else {
                return MemoryGameResult.MATCH;
            }
        } else {
            return MemoryGameResult.NO_MATCH;
        }
    }

    private static List<Integer> initCards(int kind) {
        int size;
        if (kind == 0) {
            size = 4 * 3;
        } else if (kind == 1) {
            size = 5 * 4;
        } else {
            size = 6 * 5;
        }
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < size / 2; i++) {
            cards.add(i);
            cards.add(i);
        }
        Collections.shuffle(cards);
        return cards;
    }



    public enum MemoryGameResult {
        NO_MATCH(0),
        MATCH(1),
        WIN(2),
        TIE(3),
        LOSE(4);

        private final int val;

        MemoryGameResult(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }
}
