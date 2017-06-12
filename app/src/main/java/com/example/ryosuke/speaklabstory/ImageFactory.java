package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

/**
 * Created by ryosuke on 17/05/15.
 */

public class ImageFactory {
    Context context;
    AssetManager assets;

    /*
        このクラスはfile名を渡すことで、自動的にassetからImageViewを返すくメソッドを持ったうクラスです。
     */


    public ImageFactory(Context context){
        this.context = context;
        assets = context.getAssets();
    }

    public ImageView getImageView(String filename){
        InputStream in = null;
        Bitmap bitmap = null;


        try{
            in = assets.open(filename);
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap ==null){
                throw new RuntimeException("Couldn't load bitmap from asset' " + filename + "'");
            }
        } catch (IOException e){
            throw new RuntimeException("Couldn't load bitmap from asset' " + filename + "'");
        } finally {
            if(in != null){
                try {
                    in.close();
                }catch (IOException e){

                }
            }
        }
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);

        return imageView;
    }

    public Bitmap getBitmap(String filename){
        InputStream in = null;
        Bitmap bitmap = null;


        try{
            in = assets.open(filename);
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap ==null){
                throw new RuntimeException("Couldn't load bitmap from asset' " + filename + "'");
            }
        } catch (IOException e){
            throw new RuntimeException("Couldn't load bitmap from asset' " + filename + "'");
        } finally {
            if(in != null){
                try {
                    in.close();
                }catch (IOException e){

                }
            }
        }
        return bitmap;
    }

    public ImageView getImageView(@DrawableRes int drawable){

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(drawable);

        return imageView;
    }

}
