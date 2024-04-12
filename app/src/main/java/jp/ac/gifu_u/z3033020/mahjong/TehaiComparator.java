package jp.ac.gifu_u.z3033020.mahjong;

import java.util.Comparator;

public class TehaiComparator implements Comparator<Tile>{
    public int compare(Tile t1, Tile t2) {
        Class c1 = t1.getClass();
        Class c2 = t2.getClass();
        if(c1 == c2) {
            return t1.number - t2.number;
        } else {
            return -1 * c1.getName().compareTo(c2.getName());
        }
    }
}
