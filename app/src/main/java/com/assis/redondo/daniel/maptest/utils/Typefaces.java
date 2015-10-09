package com.assis.redondo.daniel.maptest.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Hashtable;

public class Typefaces {

    private static final String TAG = LogUtil.getTag(Typefaces.class);

	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public interface Font {
        String getName();
    }

    public static Typeface get(AssetManager assets, Font font) {
        synchronized (cache) {
            if (!cache.containsKey(font.getName())) {
                try {
                    Typeface t = Typeface.createFromAsset(assets, "fonts/" + font.getName());
                    cache.put(font.getName(), t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + font.getName()
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(font.getName());
        }
    }

    public static void setTextFont(AssetManager assets, TextView text, Font font) {
        Typeface tf =  get(assets, font);
        if(tf != null) {
            text.setTypeface(tf);
        }
    }

    public static void setButtonTextFont(AssetManager assets, Button button, Font font) {
        Typeface tf =  get(assets, font);
        if(tf != null) {
            button.setTypeface(tf);
        }
    }

}