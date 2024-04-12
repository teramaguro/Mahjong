package jp.ac.gifu_u.z3033020.mahjong;

public class Tile {
    int number;
    String icon;
    int image;
    public Tile(int number, String icon, int image) {
        this.number = number;
        this.icon = icon;
        this.image = image;
    }

    public Tile clone() {
        if(this.getClass() == Manzu.class){
            return new Manzu(this.number);
        } else if(this.getClass() == Souzu.class){
            return new Souzu(this.number);
        } else if(this.getClass() == Pinzu.class){
            return new Pinzu(this.number);
        } else if(this.getClass() == Jihai.class){
            return new Jihai(this.number);
        } else {
            return new Tile(this.number, this.icon, this.image);
        }
    }
}
