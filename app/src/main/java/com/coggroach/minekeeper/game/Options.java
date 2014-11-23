package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.R;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class Options
{
    public static Difficulty SETTING_DIFFICULTY = Difficulty.MEDIUM;
    public static int TEXTURE_ID = R.drawable.metal_texture_square;

    public static final int MAX_WIDTH = 16;
    public static final int MAX_HEIGHT = 16;

    public static void changeDifficulty(Difficulty d)
    {
        SETTING_DIFFICULTY = d;
    }
}
