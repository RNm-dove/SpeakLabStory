package com.example.ryosuke.speaklabstory;

/**
 * Created by ryosuke on 17/05/16.
 */

public class KANA {

    public KANA() {
    }

    public static String checkSoundOf(String kana){
        String sound;

        switch (kana){
            case"あ":
            case"い":
            case"う":
            case "え":
            case "お":
                sound = "a";
                break;
            case"か":
            case"き":
            case"く":
            case "け":
            case "こ":
                sound = "ka";
                break;
            case"さ":
            case"し":
            case"す":
            case "せ":
            case "そ":
                sound = "sa";
                break;
            case"た":
            case"ち":
            case"つ":
            case "て":
            case "と":
                sound = "ta";
                break;
            case"な":
            case"に":
            case"ぬ":
            case "ね":
            case "の":
                sound = "na";
                break;
            case"は":
            case"ひ":
            case"ふ":
            case "へ":
            case "ほ":
                sound = "ha";
                break;
            case"ま":
            case"み":
            case"む":
            case "め":
            case "も":
                sound = "ma";
                break;
            case"や":
            case"ゆ":
            case"よ":
                sound = "ya";
                break;
            case"ら":
            case"り":
            case"る":
            case "れ":
            case "ろ":
                sound = "ra";
                break;
            case"わ":
            case"を":
            case"ん":

                sound = "wa";
                break;
            default: sound =null;

        }
        return sound;
    }

    public static int checkPositionOf(String kana){
        int position = -1;

        switch (kana){
            case"あ":
            case"か":
            case"さ":
            case "た":
            case "な":
            case"は":
            case"ま":
            case"や":
            case "ら":
            case "わ":
                position = 0;
                break;
            case"い":
            case"き":
            case"し":
            case "ち":
            case "に":
            case"ひ":
            case"み":
            case"り":
                position = 1;
                break;
            case"う":
            case"く":
            case"す":
            case "つ":
            case "ぬ":
            case"ふ":
            case"む":
            case"ゆ":
            case"る" :
            case"を" :
                position = 2;
                break;
            case"え":
            case"け":
            case"せ":
            case "て":
            case "ね":
            case"へ":
            case"め":
            case"れ":
                position = 3;
                break;
            case"お":
            case"こ":
            case"そ":
            case "と":
            case "の":
            case"ほ":
            case"も":
            case"よ":
            case"ろ" :
            case"ん" :
                position = 4;
                break;



        }
        return position;
    }

}
