package jp.ac.gifu_u.z3033020.mahjong;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Player{
    ArrayList<Tile> tehai = new ArrayList<>();
    static HashMap<Integer, Integer> shantenHash;
    static HashMap<Integer, int[]> korituHash = new HashMap<>();
    static{
        korituHash.put(1, new int[]{2, 3});
        korituHash.put(2, new int[]{1, 3, 4});
        korituHash.put(3, new int[]{1, 2, 4, 5});
        korituHash.put(4, new int[]{2, 3, 5, 6});
        korituHash.put(5, new int[]{3, 4, 6, 7});
        korituHash.put(6, new int[]{4, 5, 7, 8});
        korituHash.put(7, new int[]{5, 6, 8, 9});
        korituHash.put(8, new int[]{6, 7, 9});
        korituHash.put(9, new int[]{7, 8});
    }
    static{
        shantenHash = MainActivity.shantenHash;
//        try{
//            File file = new File("D:\\Java_AndroidStudio\\Mahjong\\app\\src\\main\\assets\\ShantenTable.txt");
//            FileReader filereader = new FileReader(file);
//            int c = filereader.read();
//            while(c != -1){
//                if(c == '\n'){
//                    c = filereader.read();
//                    continue;
//                }
//                int key = 0;
//                int value = 0;
//                while((char)c != '	'){
//                    // System.out.println("key:"+c);
//                    key = key * 10 + Character.getNumericValue(c);
//                    c = filereader.read();
//                }
//                c = filereader.read();
//                while(c != '\n'){
//                    // System.out.println("value:"+c);
//                    value = value * 10 + Character.getNumericValue(c);
//                    c = filereader.read();
//                }
//                // System.out.println(key+":"+value);
//                shantenHash.put(key, value);
//                c = filereader.read();
//            }
//            filereader.close();
//        }catch(FileNotFoundException e){
//            System.out.println(e);
//        }catch(IOException e){
//            System.out.println(e);
//        }
    }
    public void set(ArrayList<Tile> tehai) {
        this.tehai = tehai;
    }

    public void sort(){
        sort(tehai);
    }
    public void sort(ArrayList<Tile> tiles){
        Collections.sort(tiles, new TehaiComparator());
    }
    public ArrayList<Tile> getTiles(ArrayList<Tile> tehai, Class c){
        ArrayList<Tile> tiles = new ArrayList<>();
        for(int i = 0; i < tehai.size(); i++){
            if(tehai.get(i).getClass() == c){
                tiles.add(tehai.get(i));
            }
        }
        return tiles;
    }
    public void add(Tile tile) {
        tehai.add(tile);
    }
    public void remove(Tile tile) {
        tehai.remove(tile);
    }
    public void remove(int index) {
        tehai.remove(index);
    }
    public int size() {
        return tehai.size();
    }
    public Tile get(int index) {
        return tehai.get(index);
    }

    public boolean isAgari(Tile tumo){
        ArrayList<Tile> tehai_tmp = cloneTiles(tehai);
        tehai_tmp.add(tumo);
        sort(tehai_tmp);
        int shanten = getShanten(tehai_tmp);
        if(shanten == -1){
            return true;
        }else{
            return false;
        }
    }

    public Yuukou getYuukou(){
        return getYuukou(tehai, getShanten());
    }

    public Yuukou getYuukou(ArrayList<Tile> tehai, int shanten){
        ArrayList<Tile> yuukou = new ArrayList<>();
        ArrayList<Tile> manzus = getTiles(tehai, Manzu.class);
        ArrayList<Tile> souzus = getTiles(tehai, Souzu.class);
        ArrayList<Tile> pinzus = getTiles(tehai, Pinzu.class);
        ArrayList<Tile> jihai = getTiles(tehai, Jihai.class);
        ArrayList<Integer> manzus_kantu = getKantu(cloneTiles(manzus));
        manzus_kantu.remove(manzus_kantu.size() - 1);
        for(int i = 1; i <= 9; i++){
            ArrayList<Tile> tehai_tmp = cloneTiles(tehai);
            tehai_tmp.add(new Manzu(i));
            sort(tehai_tmp);
            //槓子を除く
            if(!manzus_kantu.contains(i) && getShanten(tehai_tmp) < shanten){
                yuukou.add(new Manzu(i));
            }
        }
        ArrayList<Integer> souzus_kantu = getKantu(cloneTiles(souzus));
        souzus_kantu.remove(souzus_kantu.size() - 1);
        for(int i = 1; i <= 9; i++){
            ArrayList<Tile> tehai_tmp = cloneTiles(tehai);
            tehai_tmp.add(new Souzu(i));
            sort(tehai_tmp);
            //槓子を除く
            if(!souzus_kantu.contains(i) && getShanten(tehai_tmp) < shanten){
                yuukou.add(new Souzu(i));
            }
        }
        ArrayList<Integer> pinzus_kantu = getKantu(cloneTiles(pinzus));
        pinzus_kantu.remove(pinzus_kantu.size() - 1);
        for(int i = 1; i <= 9; i++){
            ArrayList<Tile> tehai_tmp = cloneTiles(tehai);
            tehai_tmp.add(new Pinzu(i));
            sort(tehai_tmp);
            //槓子を除く
            if(!pinzus_kantu.contains(i) && getShanten(tehai_tmp) < shanten){
                yuukou.add(new Pinzu(i));
            }
        }
        ArrayList<Integer> jihai_kantu = getKantu(cloneTiles(jihai));
        jihai_kantu.remove(jihai_kantu.size() - 1);
        for(int i = 1; i <= 7; i++){
            ArrayList<Tile> tehai_tmp = cloneTiles(tehai);
            tehai_tmp.add(new Jihai(i));
            sort(tehai_tmp);
            //槓子を除く
            if(!jihai_kantu.contains(i) && getShanten(tehai_tmp) < shanten){
                yuukou.add(new Jihai(i));
            }
        }

        //有効牌の数を数える
        ArrayList<Tile> tehai_tmp = cloneTiles(tehai);
        int yuukou_num = yuukou.size() * 4;
        for(Tile tile : yuukou){
            int i = 0;
            while(i < tehai_tmp.size()){
                if(tehai_tmp.get(i).getClass() == tile.getClass() && tehai_tmp.get(i).number == tile.number){
                    yuukou_num--;
                    tehai_tmp.remove(i);
                }else{
                    i++;
                }
            }
        }

        return new Yuukou(yuukou, yuukou.size(), yuukou_num);
    }

    public int getShanten(){
        return getShanten(tehai);
    }

    public int getShanten (ArrayList<Tile> tiles){
        int shanten = 8;
        int shantenMentu = getShantenMentu(cloneTiles(tiles));
        int shantenTiitoitu = getShantenTiitoitu(cloneTiles(tiles));
        int shantenKokusi = getShantenKokusi(cloneTiles(tiles));
        if(shantenMentu < shanten){
            shanten = shantenMentu;
        }
        if(shantenTiitoitu < shanten){
            shanten = shantenTiitoitu;
        }
        if(shantenKokusi < shanten){
            shanten = shantenKokusi;
        }
        System.out.print("面子手:"+shantenMentu+"\t"+"七対子:"+shantenTiitoitu+"\t"+"国士無双:"+shantenKokusi+"\t");
        return shanten;
    }

    public int getShantenMentu(ArrayList<Tile> tiles){
        int shanten = 8;
        ArrayList<Tile> manzus = getTiles(tiles, Manzu.class);
        ArrayList<Tile> souzus = getTiles(tiles, Souzu.class);
        ArrayList<Tile> pinzus = getTiles(tiles, Pinzu.class);
        ArrayList<Tile> jihais = getTiles(tiles, Jihai.class);
        sort(manzus);
        sort(souzus);
        sort(pinzus);
        sort(jihais);
        //雀頭ごとにループする
        for(int i = 0; i < manzus.size() - 1; i++){
            if(manzus.get(i).number == manzus.get(i + 1).number){
                //雀頭ならば
                ArrayList<Tile> manzus_tmp = cloneTiles(manzus);
                ArrayList<Tile> souzus_tmp = cloneTiles(souzus);
                ArrayList<Tile> pinzus_tmp = cloneTiles(pinzus);
                ArrayList<Tile> jihais_tmp = cloneTiles(jihais);
                //雀頭を除く
                manzus_tmp.remove(i);
                manzus_tmp.remove(i);
                int shanten_tmp = calcShanten(manzus_tmp, souzus_tmp, pinzus_tmp, jihais_tmp, true) - 1;
                if(shanten_tmp < shanten){
                    shanten = shanten_tmp;
                }
            }
        }
        for(int i = 0; i < souzus.size() - 1; i++){
            if(souzus.get(i).number == souzus.get(i + 1).number){
                //雀頭ならば
                ArrayList<Tile> manzus_tmp = cloneTiles(manzus);
                ArrayList<Tile> souzus_tmp = cloneTiles(souzus);
                ArrayList<Tile> pinzus_tmp = cloneTiles(pinzus);
                ArrayList<Tile> jihais_tmp = cloneTiles(jihais);
                //雀頭を除く
                souzus_tmp.remove(i);
                souzus_tmp.remove(i);
                int shanten_tmp = calcShanten(manzus_tmp, souzus_tmp, pinzus_tmp, jihais_tmp, true) - 1;
                if(shanten_tmp < shanten){
                    shanten = shanten_tmp;
                }
            }
        }
        for(int i = 0; i < pinzus.size() - 1; i++){
            if(pinzus.get(i).number == pinzus.get(i + 1).number){
                //雀頭ならば
                ArrayList<Tile> manzus_tmp = cloneTiles(manzus);
                ArrayList<Tile> souzus_tmp = cloneTiles(souzus);
                ArrayList<Tile> pinzus_tmp = cloneTiles(pinzus);
                ArrayList<Tile> jihais_tmp = cloneTiles(jihais);
                //雀頭を除く
                pinzus_tmp.remove(i);
                pinzus_tmp.remove(i);
                int shanten_tmp = calcShanten(manzus_tmp, souzus_tmp, pinzus_tmp, jihais_tmp, true) - 1;
                if(shanten_tmp < shanten){
                    shanten = shanten_tmp;
                }
            }
        }
        for(int i = 0; i < jihais.size() - 1; i++){
            if(jihais.get(i).number == jihais.get(i + 1).number){
                //雀頭ならば
                ArrayList<Tile> manzus_tmp = cloneTiles(manzus);
                ArrayList<Tile> souzus_tmp = cloneTiles(souzus);
                ArrayList<Tile> pinzus_tmp = cloneTiles(pinzus);
                ArrayList<Tile> jihais_tmp = cloneTiles(jihais);
                //雀頭を除く
                jihais_tmp.remove(i);
                jihais_tmp.remove(i);
                int shanten_tmp = calcShanten(manzus_tmp, souzus_tmp, pinzus_tmp, jihais_tmp, true) - 1;
                if(shanten_tmp < shanten){
                    shanten = shanten_tmp;
                }
            }
        }
        //雀頭がない場合
        int shanten_tmp = calcShanten(cloneTiles(manzus), cloneTiles(souzus), cloneTiles(pinzus), cloneTiles(jihais), false);
        if(shanten_tmp < shanten){
            shanten = shanten_tmp;
        }
        return shanten;
    }

    public int getShantenTiitoitu(ArrayList<Tile> tiles){
        int shanten = 6;
        ArrayList<Tile> manzus = getTiles(tiles, Manzu.class);
        ArrayList<Tile> souzus = getTiles(tiles, Souzu.class);
        ArrayList<Tile> pinzus = getTiles(tiles, Pinzu.class);
        ArrayList<Tile> jihais = getTiles(tiles, Jihai.class);
        sort(manzus);
        sort(souzus);
        sort(pinzus);
        //七対子の場合
        int toitu = 0;
        int i = 0;
        ArrayList<Integer> array = getKantu(manzus);
        int kantuNum = array.get(array.size() - 1);
        array = getKantu(souzus);
        kantuNum += array.get(array.size() - 1);
        array = getKantu(pinzus);
        kantuNum += array.get(array.size() - 1);
        array = getKantu(jihais);
        kantuNum += array.get(array.size() - 1);
        Tile prev = null;
        while(i < manzus.size() - 1){
            if(manzus.get(i).number == manzus.get(i + 1).number && (prev == null || prev.number != manzus.get(i).number)){
                prev = manzus.get(i);
                toitu++;
                // System.out.println("manzu:" + manzus.get(i).number);
                i += 2;
            }else{
                i++;
            }
        }
        prev = null;
        i = 0;
        while(i < souzus.size() - 1){
            if(souzus.get(i).number == souzus.get(i + 1).number && (prev == null || prev.number != souzus.get(i).number)){
                prev = souzus.get(i);
                toitu++;
                // System.out.println("souzu:" + souzus.get(i).number);
                i += 2;
            }else{
                i++;
            }
        }
        prev = null;
        i = 0;
        while(i < pinzus.size() - 1){
            if(pinzus.get(i).number == pinzus.get(i + 1).number && (prev == null || prev.number != pinzus.get(i).number)){
                prev = pinzus.get(i);
                toitu++;
                // System.out.println("pinzu:" + pinzus.get(i).number);
                i += 2;
            }else{
                i++;
            }
        }
        prev = null;
        i = 0;
        while(i < jihais.size() - 1){
            if(jihais.get(i).number == jihais.get(i + 1).number && (prev == null || prev.number != jihais.get(i).number)){
                prev = jihais.get(i);
                toitu++;
                // System.out.println("jihai:" + jihais.get(i).number);
                i += 2;
            }else{
                i++;
            }
        }
        if(6 - toitu + kantuNum < shanten){
            shanten = 6 - toitu + kantuNum;
        }
        return shanten;
    }

    public int getShantenKokusi(ArrayList<Tile> tiles){
        int shanten = 13;
        int[] count = new int[10];
        boolean duplicate = false;
        ArrayList<Tile> manzus = getTiles(tiles, Manzu.class);
        ArrayList<Tile> souzus = getTiles(tiles, Souzu.class);
        ArrayList<Tile> pinzus = getTiles(tiles, Pinzu.class);
        ArrayList<Tile> jihais = getTiles(tiles, Jihai.class);
        for(int i = 0; i < manzus.size(); i++){
            if((manzus.get(i).number == 1 || manzus.get(i).number == 9) && count[manzus.get(i).number] < 2){
                if(count[manzus.get(i).number] == 0){
                    count[manzus.get(i).number]++;
                    shanten--;
                }else if(count[manzus.get(i).number] == 1 && !duplicate){
                    count[manzus.get(i).number]++;
                    shanten--;
                    duplicate = true;
                }
            }
        }
        count = new int[10];
        for(int i = 0; i < souzus.size(); i++){
            if((souzus.get(i).number == 1 || souzus.get(i).number == 9) && count[souzus.get(i).number] < 2){
                if(count[souzus.get(i).number] == 0){
                    count[souzus.get(i).number]++;
                    shanten--;
                }else if(count[souzus.get(i).number] == 1 && !duplicate){
                    count[souzus.get(i).number]++;
                    shanten--;
                    duplicate = true;
                }
            }
        }
        count = new int[10];
        for(int i = 0; i < pinzus.size(); i++){
            if((pinzus.get(i).number == 1 || pinzus.get(i).number == 9) && count[pinzus.get(i).number] < 2){
                if(count[pinzus.get(i).number] == 0){
                    count[pinzus.get(i).number]++;
                    shanten--;
                }else if(count[pinzus.get(i).number] == 1 && !duplicate){
                    count[pinzus.get(i).number]++;
                    shanten--;
                    duplicate = true;
                }
            }
        }
        count = new int[10];
        for(int i = 0; i < jihais.size(); i++){
            if(count[jihais.get(i).number] < 2){
                if(count[jihais.get(i).number] == 0){
                    count[jihais.get(i).number]++;
                    shanten--;
                }else if(count[jihais.get(i).number] == 1 && !duplicate){
                    count[jihais.get(i).number]++;
                    shanten--;
                    duplicate = true;
                }
            }
        }
        return shanten;
    }

    public int calcShanten(ArrayList<Tile> manzus, ArrayList<Tile> souzus, ArrayList<Tile> pinzus, ArrayList<Tile> jihais, boolean head){
        int manzus_format = getRemovedIsolated_format(convertFormat(manzus));
        int souzus_format = getRemovedIsolated_format(convertFormat(souzus));
        int pinzus_format = getRemovedIsolated_format(convertFormat(pinzus));
        //[0:面子*10+ターツ 1:面子*2+ターツ][0:まんず 1:そうず 2:ぴんず][0:面子数 1:ターツ数]
        int[][][] hash = getHash(manzus_format, souzus_format, pinzus_format);
        int jihaiKoutuNum = 0;
        while(true){
            int[] koutu = getKoutu(jihais);
            if(koutu[0] == 0){
                break;
            }
            jihais.remove(koutu[1]);
            jihais.remove(koutu[1]);
            jihais.remove(koutu[1]);
            jihaiKoutuNum++;
        }
        int jihaiKoutuNum_tmp = jihaiKoutuNum;
        int jihaiHeadNum = getHead(jihais)[0];
        int jihaiHeadNum_tmp = jihaiHeadNum;
        int shanten = 8;

        //[][]
        int[][] hash_10 = hash[0];
        int shanten_tmp = 8;
        int block = 5;
        int headNum = 0;
        if(head){
            block = 4;
        }
        // System.out.println("面子*10+ターツが最大");
        //面子*10+ターツが最大
        while(block > 0){
            if(hash_10[0][0] > 0){
                hash_10[0][0]--;
                shanten_tmp-=2;
            }else if(hash_10[1][0] > 0){
                hash_10[1][0]--;
                shanten_tmp-=2;
            }else if(hash_10[2][0] > 0){
                hash_10[2][0]--;
                shanten_tmp-=2;
            }else if(jihaiKoutuNum_tmp > 0){
                jihaiKoutuNum_tmp--;
                shanten_tmp-=2;
            }else if(hash_10[0][1] > 0){
                hash_10[0][1]--;
                shanten_tmp--;
                if(getHead(cloneTiles(manzus))[0] > 0){
                    headNum++;
                }
            }else if(hash_10[1][1] > 0){
                hash_10[1][1]--;
                shanten_tmp--;
                if(getHead(cloneTiles(souzus))[0] > 0){
                    headNum++;
                }
            }else if(hash_10[2][1] > 0){
                hash_10[2][1]--;
                if(getHead(cloneTiles(pinzus))[0] > 0){
                    headNum++;
                }
                shanten_tmp--;
            }else if(jihaiHeadNum_tmp > 0){
                jihaiHeadNum_tmp--;
                shanten_tmp--;
                headNum++;
            }else{
                break;
            }
            block--;
        }
        //5ブロックのヘッドレスはシャンテン数が1増える
        if(block == 0 && !head){
            shanten_tmp++;
            // System.out.println("ヘッドレス1:" + shanten_tmp);
        }
        if(shanten_tmp < shanten){
            shanten = shanten_tmp;
        }

        int[][] hash_2 = hash[1];
        shanten_tmp = 8;
        block = 5;
        headNum = 0;
        jihaiKoutuNum_tmp = jihaiKoutuNum;
        jihaiHeadNum_tmp = jihaiHeadNum;
        if(head){
            block = 4;
        }
        //面子*2+ターツが最大
        // System.out.println("面子*2+ターツが最大");
        while(block > 0){
            if(hash_2[0][0] > 0){
                hash_2[0][0]--;
                shanten_tmp-=2;
            }else if(hash_2[1][0] > 0){
                hash_2[1][0]--;
                shanten_tmp-=2;
            }else if(hash_2[2][0] > 0){
                hash_2[2][0]--;
                shanten_tmp-=2;
            }else if(jihaiKoutuNum_tmp > 0){
                jihaiKoutuNum_tmp--;
                shanten_tmp-=2;
            }else if(hash_2[0][1] > 0){
                hash_2[0][1]--;
                if(getHead(cloneTiles(manzus))[0] > 0){
                    headNum++;
                }
                shanten_tmp--;
            }else if(hash_2[1][1] > 0){
                hash_2[1][1]--;
                if(getHead(cloneTiles(souzus))[0] > 0){
                    headNum++;
                }
                shanten_tmp--;
            }else if(hash_2[2][1] > 0){
                hash_2[2][1]--;
                if(getHead(cloneTiles(pinzus))[0] > 0){
                    headNum++;
                }
                shanten_tmp--;
            }else if(jihaiHeadNum_tmp > 0){
                jihaiHeadNum_tmp--;
                shanten_tmp--;
            }else{
                break;
            }
            block--;
        }
        //5ブロックのヘッドレスはシャンテン数が1増える
        if(block == 0 && !head){
            shanten_tmp++;
            // System.out.println("ヘッドレス2:" + shanten_tmp);
        }
        if(shanten_tmp < shanten){
            shanten = shanten_tmp;
        }
        return shanten;
    }

    public int[][][] getHash(int manzus_format, int souzus_format, int pinzus_format){
        //[面子*10+ターツ, 面子*2+ターツ][まんず、そうず、ぴんず][面子数、ターツ数]
        int[][][] hash = new int[2][3][2];
        int manzu_hash = 0;
        int souzu_hash = 0;
        int pinzu_hash = 0;
        if(manzus_format != 0){
            manzu_hash = shantenHash.get(manzus_format);
        }
        if(souzus_format != 0){
            souzu_hash = shantenHash.get(souzus_format);
        }
        if(pinzus_format != 0){
            pinzu_hash = shantenHash.get(pinzus_format);
        }
        hash[0][0][0] = manzu_hash / 1000;
        manzu_hash %= 1000;
        hash[0][0][1] = manzu_hash / 100;
        manzu_hash %= 100;
        hash[1][0][0] = manzu_hash / 10;
        hash[1][0][1] = manzu_hash % 10;

        hash[0][1][0] = souzu_hash / 1000;
        souzu_hash %= 1000;
        hash[0][1][1] = souzu_hash / 100;
        souzu_hash %= 100;
        hash[1][1][0] = souzu_hash / 10;
        hash[1][1][1] = souzu_hash % 10;

        hash[0][2][0] = pinzu_hash / 1000;
        pinzu_hash %= 1000;
        hash[0][2][1] = pinzu_hash / 100;
        pinzu_hash %= 100;
        hash[1][2][0] = pinzu_hash / 10;
        hash[1][2][1] = pinzu_hash % 10;

        return hash;
    }

    public int getRemovedIsolated_format(int original_format) {
        int format_tmp = original_format;
        int format = 0;
        int digitNum = String.valueOf(original_format).length();
        for(int i = 0; i < digitNum; i++){
            if(original_format % 10 == 1){
                int[] isolatedCheck = Player.korituHash.get(9 - i);
                boolean isolatedFlag = true;
                for(int j = 0; j < isolatedCheck.length; j++){
                    int num = (int) (format_tmp / Math.pow(10, 9 - isolatedCheck[j])) % 10;
                    if(num > 0){
                        isolatedFlag = false;
                        break;
                    }
                }
                if(!isolatedFlag){
                    format += (original_format % 10) * Math.pow(10, i);
                }
            }else{
                format += (original_format % 10) * Math.pow(10, i);
            }
            original_format /= 10;
        }
        return format;
    }

    public int convertFormat(ArrayList<Tile> tiles){
        int format = 0;
        for(int i = 0; i < tiles.size(); i++){
            format += Math.pow(10, 9 - tiles.get(i).number);
        }
        return format;
    }

    public int[] getKoutu(ArrayList<Tile> tiles){
        int koutuNum = 0;
        int koutuIndex = -1;
        int i = 0;
        while(i < tiles.size() - 2){
            if(tiles.get(i).number == tiles.get(i+1).number && tiles.get(i).number == tiles.get(i+2).number){
                if(koutuIndex == -1){
                    koutuIndex = i;
                }
                koutuNum++;
                i+=2;
            }
            i++;
        }
        return new int[]{koutuNum, koutuIndex};
    }

    public ArrayList<Integer> getKantu(ArrayList<Tile> tiles){
        int kantuNum = 0;
        // int kantuIndex = -1;
        ArrayList<Integer> array = new ArrayList<Integer>();
        int i = 0;
        while(i < tiles.size() - 3){
            if(tiles.get(i).number == tiles.get(i+1).number && tiles.get(i).number == tiles.get(i+2).number && tiles.get(i).number == tiles.get(i+3).number){
                array.add(tiles.get(i).number);
                kantuNum++;
                i+=3;
            }
            i++;
        }
        array.add(kantuNum);
        return array;
    }

    public int[] getShuntu(ArrayList<Tile> tiles){
        ArrayList<Tile> tiles_tmp = cloneTiles(tiles);
        int shuntuNum = 0;
        int shuntuIndex1 = -1;
        int shuntuIndex2 = -1;
        int shuntuIndex3 = -1;
        int i = 0;
        int j = i+1;//オフセット
        int k = j+1;

        while(i < tiles_tmp.size() - 2){//1枚目
            // System.out.println("1");
            // for(Tile tile : tiles_tmp){
            //     System.out.print(tile.icon);
            // }
            // System.out.println("");
            // System.out.println("i : " + i);
            // System.out.println("j : " + j);
            // System.out.println("k : " + k);
            if(tiles_tmp.get(i).number + 1 == tiles_tmp.get(j).number){
                // System.out.println("階段");
                while(j < tiles_tmp.size() - 1){//2枚目
                    // System.out.println("2");
                    if(tiles_tmp.get(j).number + 1 == tiles_tmp.get(k).number){
                        shuntuNum++;
                        tiles_tmp.remove(k);
                        tiles_tmp.remove(j);
                        tiles_tmp.remove(i);
                        if(shuntuIndex1 == -1){
                            shuntuIndex1 = i;
                            shuntuIndex2 = j;
                            shuntuIndex3 = k;
                        }
                        i = i + 1;
                        j=i+1;
                        k=j+1;
                        break;
                    }else if(tiles_tmp.get(j).number == tiles_tmp.get(k).number){
                        //３つめをスライドさせる
                        k = k+1;
                        if(k >= tiles_tmp.size()){
                            break;
                        }
                    }else{
                        break;
                    }
                }
                i=i+1;
                j=i+1;
                k=j+1;
            }else if(tiles_tmp.get(i).number == tiles_tmp.get(j).number){
                //２つめをスライドさせる
                // System.out.println("イコール");
                j = j+1;
                k = k+1;
                //kが左端に到達したとき
                if(k >= tiles_tmp.size()){
                    i=i+1;
                    j = i+1;
                    k = j+1;
                }
            }else{
                // System.out.println("それ以外");
                i=i+1;
                j = i+1;
                k = j+1;
            }
            // if(tiles_tmp.get(i).number + 1 == tiles_tmp.get(i+1+j).number && tiles_tmp.get(i).number + 2 == tiles_tmp.get(i+2+j).number){
            //     if(shuntuIndex == -1){
            //         shuntuIndex = i;
            //     }
            //     shuntuNum++;
            //     i+=2;
            // }
            // if(tiles_tmp.get(i).number == tiles_tmp.get(i+j+1).number){
            //     j++;
            // }else{
            //     j = 0;
            //     i++;
            // }
        }
        return new int[]{shuntuNum, shuntuIndex1, shuntuIndex2, shuntuIndex3};
    }

    public int[] getHead(ArrayList<Tile> tiles){
        int headNum = 0;
        int headIndex = -1;
        int i = 0;
        while(i < tiles.size() - 1){
            if(tiles.get(i).number == tiles.get(i+1).number){
                if(headIndex == -1){
                    headIndex = i;
                }
                headNum++;
                i++;
            }
            i++;
        }
        return new int[]{headNum, headIndex};
    }

    public int[] getTaatu(ArrayList<Tile> tiles){
        int taatuNum = 0;
        int taatuIndex = -1;
        int i = 0;
        while(i < tiles.size() - 1){
            if((tiles.get(i).number + 1 == tiles.get(i+1).number) || (tiles.get(i).number + 2 == tiles.get(i+1).number)){
                if(taatuIndex == -1){
                    taatuIndex = i;
                }
                taatuNum++;
                i++;
            }
            i++;
        }
        return new int[]{taatuNum, taatuIndex};
    }

    public ArrayList<Tile> cloneTiles(ArrayList<Tile> tiles){
        ArrayList<Tile> tiles_tmp = new ArrayList<>();
        for(int i = 0; i < tiles.size(); i++){
            tiles_tmp.add(tiles.get(i).clone());
        }
        return tiles_tmp;
    }
}
