package jp.ac.gifu_u.z3033020.mahjong;

import java.util.HashMap;

public class Souzu extends Tile {
    static HashMap<Integer, String> numberToIcon = new HashMap<>();
    static HashMap<Integer, Integer> numberToImage = new HashMap<>();
    static {
        numberToIcon.put(1, " 1");
        numberToIcon.put(2, " 2");
        numberToIcon.put(3, " 3");
        numberToIcon.put(4, " 4");
        numberToIcon.put(5, " 5");
        numberToIcon.put(6, " 6");
        numberToIcon.put(7, " 7");
        numberToIcon.put(8, " 8");
        numberToIcon.put(9, " 9");
    }
    static{
        numberToImage.put(1, R.drawable.sou1);
        numberToImage.put(2, R.drawable.sou2);
        numberToImage.put(3, R.drawable.sou3);
        numberToImage.put(4, R.drawable.sou4);
        numberToImage.put(5, R.drawable.sou5);
        numberToImage.put(6, R.drawable.sou6);
        numberToImage.put(7, R.drawable.sou7);
        numberToImage.put(8, R.drawable.sou8);
        numberToImage.put(9, R.drawable.sou9);
    }

    public Souzu(int number) {
        super(number, numberToIcon.get(number), numberToImage.get(number));
    }
//    public Souzu(int number, String icon) {
//        super(number, icon);
//    }
}

