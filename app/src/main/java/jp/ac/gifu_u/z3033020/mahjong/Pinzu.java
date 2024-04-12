package jp.ac.gifu_u.z3033020.mahjong;

import java.util.HashMap;

public class Pinzu extends Tile {
    static HashMap<Integer, String> numberToIcon = new HashMap<>();
    static HashMap<Integer, Integer> numberToImage = new HashMap<>();
    static {
        numberToIcon.put(1, " ①");
        numberToIcon.put(2, " ②");
        numberToIcon.put(3, " ③");
        numberToIcon.put(4, " ④");
        numberToIcon.put(5, " ⑤");
        numberToIcon.put(6, " ⑥");
        numberToIcon.put(7, " ⑦");
        numberToIcon.put(8, " ⑧");
        numberToIcon.put(9, " ⑨");
    }
    static{
        numberToImage.put(1, R.drawable.pin1);
        numberToImage.put(2, R.drawable.pin2);
        numberToImage.put(3, R.drawable.pin3);
        numberToImage.put(4, R.drawable.pin4);
        numberToImage.put(5, R.drawable.pin5);
        numberToImage.put(6, R.drawable.pin6);
        numberToImage.put(7, R.drawable.pin7);
        numberToImage.put(8, R.drawable.pin8);
        numberToImage.put(9, R.drawable.pin9);
    }
    Pinzu(int number) {
        super(number, numberToIcon.get(number), numberToImage.get(number));
    }
//    Pinzu(int number, String icon) {
//        super(number, icon);
//    }
}
