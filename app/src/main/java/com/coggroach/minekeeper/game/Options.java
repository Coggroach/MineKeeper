package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.R;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class Options
{
    public static int width, height;
    public static int TEXTURE_ID = R.drawable.metal_texture_square;
    public static int GAMEMODE_ID = 1;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
