package jp.ac.gifu_u.z3033020.mahjong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Tile> yama = new ArrayList<>();
    ArrayList<Tile> yama_tmp = new ArrayList<>();
    Tile tumo;
    Player player = new Player();
    Random random = new Random();
    ImageButton tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13, tileTumo;
    ImageButton[] imageButtons = new ImageButton[13];
    Button btnReset;
    TextView tvShanten, tvYuukou, tvYama;
    LinearLayout linearLayout1, linearLayout2;
    static HashMap<Integer, Integer> shantenHash = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout1 = findViewById(R.id.insert_layout1);
        linearLayout2 = findViewById(R.id.insert_layout2);

        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                // assetsフォルダ内の txt をオープンする
                is = this.getAssets().open("ShantenTable.txt");
                br = new BufferedReader(new InputStreamReader(is));

                // １行ずつ読み込み、改行を付加する
                text = br.readLine();
                while (text != null) {
                    String key = text.split("\t")[0];
                    String value = text.split("\t")[1];
//                    Log.d("key", key);
//                    Log.d("value", value);
                    shantenHash.put(Integer.valueOf(key), Integer.valueOf(value));
                    text = br.readLine();
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            // エラー発生時の処理
            Log.d("ファイル読み込み", "");
        }

        tile1 = (ImageButton) findViewById(R.id.tile1);
        tile2 = (ImageButton)findViewById(R.id.tile2);
        tile3 = (ImageButton)findViewById(R.id.tile3);
        tile4 = (ImageButton)findViewById(R.id.tile4);
        tile5 = (ImageButton)findViewById(R.id.tile5);
        tile6 = (ImageButton)findViewById(R.id.tile6);
        tile7 = (ImageButton)findViewById(R.id.tile7);
        tile8 = (ImageButton)findViewById(R.id.tile8);
        tile9 = (ImageButton)findViewById(R.id.tile9);
        tile10 = (ImageButton)findViewById(R.id.tile10);
        tile11 = (ImageButton)findViewById(R.id.tile11);
        tile12 = (ImageButton)findViewById(R.id.tile12);
        tile13 = (ImageButton)findViewById(R.id.tile13);
        tileTumo = (ImageButton) findViewById(R.id.tumo);
        tvShanten = (TextView) findViewById(R.id.tvShanten);
        tvYuukou = (TextView) findViewById(R.id.yuukou);
        tvYama = (TextView) findViewById(R.id.yama);

        imageButtons[0] = tile1;
        imageButtons[1] = tile2;
        imageButtons[2] = tile3;
        imageButtons[3] = tile4;
        imageButtons[4] = tile5;
        imageButtons[5] = tile6;
        imageButtons[6] = tile7;
        imageButtons[7] = tile8;
        imageButtons[8] = tile9;
        imageButtons[9] = tile10;
        imageButtons[10] = tile11;
        imageButtons[11] = tile12;
        imageButtons[12] = tile13;

        btnReset = findViewById(R.id.reset);
        btnReset.setOnClickListener(this);

        for(ImageButton imagebutton : imageButtons){
            imagebutton.setOnClickListener(this);
        }
        tileTumo.setOnClickListener(this);

        reset();
    }

    public void setImage(ArrayList<Tile> tehai){
        try {
            for (int i = 0; i < tehai.size(); i++) {
                imageButtons[i].setImageResource(tehai.get(i).image);
            }
        }catch (Exception e){
            Log.d("error", String.valueOf(imageButtons.length));
            Log.d("tehai length", String.valueOf(tehai.size()));
        }
    }

    public Tile getTumo(){
        Tile tile = yama.get(yama.size() - 1);
        yama.remove(yama.size() - 1);
        return tile;
    }

    public void reset(){
        player = new Player();
        yama.clear();
        yama_tmp.clear();
        for (int i = 1; i <= 9; i++) {
            for(int j = 0; j < 4; j++){
                yama_tmp.add(new Souzu(i));
                yama_tmp.add(new Manzu(i));
                yama_tmp.add(new Pinzu(i));
            }
        }
        for (int i = 1; i <= 7; i++) {
            for(int j = 0; j < 4; j++){
                yama_tmp.add(new Jihai(i));
            }
        }

        while(yama_tmp.size() > 0){
            int index = random.nextInt(yama_tmp.size());
            yama.add(yama_tmp.get(index));
            yama_tmp.remove(index);
        }

        for(int i = 0; i < 13; i++){
            int index = random.nextInt(yama.size());
            player.tehai.add(yama.get(index));
            yama.remove(index);
        }

        player.sort();
        setImage(player.tehai);
        int shanten = player.getShanten();
        if(shanten > 0){
            tvShanten.setText(String.valueOf(shanten) + "シャンテン");
        }else{
            tvShanten.setText("テンパイ");
        }

        tileTumo.setVisibility(View.VISIBLE);
        tumo = getTumo();
        tileTumo.setImageResource(tumo.image);
        tvYama.setText("山 : " + String.valueOf(yama.size()) + "枚");

        deleteYuukou();
        showYuukou(player.getYuukou());
    }

    private void showYuukou(Yuukou yuukou) {
        Log.d("yuukou", String.valueOf(yuukou.tiles.size()));
        tvYuukou.setText(String.format("有効牌%d種%d牌", yuukou.shurui, yuukou.num));

        if(yuukou.tiles.size() <= 20) {
            for (Tile tile : yuukou.tiles) {
                ImageView image = new ImageView(linearLayout1.getContext());
//            ImageView image_tmp = findViewById(R.id.tile1);
//            ViewGroup.LayoutParams param = linearLayout.getLayoutParams();
//            Log.d("サイズ",String.format("width%d:height%d", image_tmp.getWidth(), image_tmp.getHeight()));
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(image_tmp.getWidth(), image_tmp.getHeight());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(83, 113);
                image.setLayoutParams(param);
                image.setImageResource(tile.image);
                linearLayout1.addView(image);
            }
        }else{
            for (int i = 0; i < 20; i++) {
                ImageView image = new ImageView(linearLayout1.getContext());
//            ImageView image_tmp = findViewById(R.id.tile1);
//            ViewGroup.LayoutParams param = linearLayout.getLayoutParams();
//            Log.d("サイズ",String.format("width%d:height%d", image_tmp.getWidth(), image_tmp.getHeight()));
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(image_tmp.getWidth(), image_tmp.getHeight());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(83, 113);
                image.setLayoutParams(param);
                image.setImageResource(yuukou.tiles.get(i).image);
                linearLayout1.addView(image);
            }
            for(int i = 20; i < yuukou.tiles.size(); i++){
                ImageView image = new ImageView(linearLayout2.getContext());
//            ImageView image_tmp = findViewById(R.id.tile1);
//            ViewGroup.LayoutParams param = linearLayout.getLayoutParams();
//            Log.d("サイズ",String.format("width%d:height%d", image_tmp.getWidth(), image_tmp.getHeight()));
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(image_tmp.getWidth(), image_tmp.getHeight());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(83, 113);
                image.setLayoutParams(param);
                image.setImageResource(yuukou.tiles.get(i).image);
                linearLayout2.addView(image);
                Log.d("回数", String.valueOf(yuukou.tiles.size()));
            }
        }
    }

    private void deleteYuukou(){
        linearLayout1.removeAllViews();
        linearLayout2.removeAllViews();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.reset){
            reset();
            deleteYuukou();
            showYuukou(player.getYuukou());
            return;
        }
        if(view.getId() != R.id.tumo){
            int index = 0;
            switch (view.getId()){
                case R.id.tile1:
                    index = 0;
                    break;
                case R.id.tile2:
                    index = 1;
                    break;
                case R.id.tile3:
                    index = 2;
                    break;
                case R.id.tile4:
                    index = 3;
                    break;
                case R.id.tile5:
                    index = 4;
                    break;
                case R.id.tile6:
                    index = 5;
                    break;
                case R.id.tile7:
                    index = 6;
                    break;
                case R.id.tile8:
                    index = 7;
                    break;
                case R.id.tile9:
                    index = 8;
                    break;
                case R.id.tile10:
                    index = 9;
                    break;
                case R.id.tile11:
                    index = 10;
                    break;
                case R.id.tile12:
                    index = 11;
                    break;
                case R.id.tile13:
                    index = 12;
                    break;
            }
            player.remove(index);
            player.add(tumo);
        }
        player.sort();
        setImage(player.tehai);
        int shanten = player.getShanten();
        if(shanten > 0){
            tvShanten.setText(String.valueOf(shanten) + "シャンテン");
        }else{
            tvShanten.setText("テンパイ");
        }
        tumo = getTumo();
        tileTumo.setImageResource(tumo.image);
        tvYama.setText("山 : " + String.valueOf(yama.size())+ "枚");
        deleteYuukou();
        showYuukou(player.getYuukou());
        if(player.isAgari(tumo)){
            tvShanten.setText("あがり");
        }
    }
}