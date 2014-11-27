package com.coggroach.minekeeper.game;

import android.content.Context;

import com.coggroach.minekeeper.R;
import com.coggroach.minekeeper.common.ResourceReader;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class Options
{
    public static Difficulty SETTING_DIFFICULTY = Difficulty.MEDIUM;
    public static String TEXTURE;// = R.drawable.metal_texture_square;
    public static int GAMEMODE;
    public static boolean SOUND;
    public static boolean MUSIC;
    public static String STYLE;
    public static String ANIMATION;

    public Options(Context c)
    {
        String[] lines = ResourceReader.getString(c, R.raw.options).split("\n");

        for(int i = 0; i < lines.length; i++)
        {
            if(lines[i].contains("TEXTURE"))
                TEXTURE = getStringValue(lines[i]);
            if(lines[i].contains("GAMEMODE"))
                GAMEMODE = getIntegerValue(lines[i]);
            if(lines[i].contains("SOUND"))
                SOUND = getBooleanValue(lines[i]);
            if(lines[i].contains("MUSIC"))
                MUSIC = getBooleanValue(lines[i]);
            if(lines[i].contains("STYLE"))
                STYLE = getStringValue(lines[i]);
            if(lines[i].contains("ANIMATION"))
                ANIMATION = getStringValue(lines[i]);
        }
    }

    public static String getStringValue(String s)
    {
        if(s.contains("S:"))
        {
            return s.substring( s.indexOf("=") + 1,  s.indexOf(";"));
        }
        return null;
    }

    public static boolean getBooleanValue(String s)
    {
        if(s.contains("B:"))
        {
            String sub = s.substring( s.indexOf("=") + 1,  s.indexOf(";"));
            boolean b = Boolean.valueOf(sub);
            return b;
        }
        return false;
    }

    public static int getIntegerValue(String s)
    {
        if(s.contains("I:"))
        {
            String sub = s.substring( s.indexOf("=") + 1,  s.indexOf(";"));
            int i = Integer.valueOf(sub);
            return i;
        }
        return Integer.MIN_VALUE;
    }

}
