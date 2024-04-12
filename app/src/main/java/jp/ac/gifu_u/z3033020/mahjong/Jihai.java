package jp.ac.gifu_u.z3033020.mahjong;

import java.util.HashMap;

public class Jihai extends Tile {
    static HashMap<Integer,String> numberToIcon = new HashMap<>();
    static HashMap<Integer, Integer> numberToImage = new HashMap<>();
    static{
        numberToIcon.put(1, "東");
        numberToIcon.put(2, "南");
        numberToIcon.put(3, "西");
        numberToIcon.put(4, "北");
        numberToIcon.put(5, "白");
        numberToIcon.put(6, "發");
        numberToIcon.put(7, "中");
    }
    static{
        numberToImage.put(1, R.drawable.ji1_ton);
        numberToImage.put(2, R.drawable.ji2_nan);
        numberToImage.put(3, R.drawable.ji3_sha);
        numberToImage.put(4, R.drawable.ji4_pei);
        numberToImage.put(5, R.drawable.ji5_haku);
        numberToImage.put(6, R.drawable.ji6_hatsu);
        numberToImage.put(7, R.drawable.ji7_chun);
    }
    public Jihai(int number) {
        super(number, numberToIcon.get(number), numberToImage.get(number));
    }
}
