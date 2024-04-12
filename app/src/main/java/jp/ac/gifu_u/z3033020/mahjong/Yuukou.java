package jp.ac.gifu_u.z3033020.mahjong;

import java.util.ArrayList;

public class Yuukou {
    ArrayList<Tile> tiles;
    int shurui;
    int num;
    public Yuukou(ArrayList<Tile> tiles, int shurui, int num){
        this.shurui = shurui;
        this.num = num;
        this.tiles = tiles;
    }
}

