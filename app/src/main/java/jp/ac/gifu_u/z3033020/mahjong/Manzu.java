package jp.ac.gifu_u.z3033020.mahjong;

import java.util.HashMap;

public class Manzu extends Tile {
    static HashMap<Integer, String> numberToIcon = new HashMap<>();
    static HashMap<Integer, Integer> numberToImage = new HashMap<>();
    static {
        numberToIcon.put(1, "一");
        numberToIcon.put(2, "二");
        numberToIcon.put(3, "三");
        numberToIcon.put(4, "四");
        numberToIcon.put(5, "五");
        numberToIcon.put(6, "六");
        numberToIcon.put(7, "七");
        numberToIcon.put(8, "八");
        numberToIcon.put(9, "九");
    }
    static{
        numberToImage.put(1, R.drawable.man1);
        numberToImage.put(2, R.drawable.man2);
        numberToImage.put(3, R.drawable.man3);
        numberToImage.put(4, R.drawable.man4);
        numberToImage.put(5, R.drawable.man5);
        numberToImage.put(6, R.drawable.man6);
        numberToImage.put(7, R.drawable.man7);
        numberToImage.put(8, R.drawable.man8);
        numberToImage.put(9, R.drawable.man9);
    }
    Manzu(int number) {
        super(number, numberToIcon.get(number), numberToImage.get(number));
    }
//    Manzu(int number, String icon) {
//        super(number, icon);
//    }
}
