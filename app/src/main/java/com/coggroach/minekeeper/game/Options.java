package com.coggroach.minekeeper.game;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class Options
{
    public static Difficulty SETTING_DIFFICULTY = Difficulty.HARD;

    public static final int MAX_WIDTH = 16;
    public static final int MAX_HEIGHT = 16;

    public static void changeDifficulty(Difficulty d)
    {
        SETTING_DIFFICULTY = d;
    }
}
