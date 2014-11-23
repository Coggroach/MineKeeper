package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TestGame extends Game
{
    public static TileColour red = new TileColour(1.0F, 0.0F, 0.0F, 1.0F);
    public static TileColour cyan = new TileColour(0.0F, 1.0F, 1.0F, 1.0F);

    public TestGame()
    {
        this(Options.SETTING_DIFFICULTY.getWidth(), Options.SETTING_DIFFICULTY.getHeight());
        this.tiles[1].setColour(cyan);
    }

    public TestGame(int w, int h)
    {
        this.height = w;
        this.width = h;
        this.start(this.width, this.height);
    }

    public void start(int w, int h)
    {
        this.tiles = new Tile[w * h];
        for(int i = 0; i < tiles.length; i++)
        {
            tiles[i] = new Tile(i, red, false, false);
        }
    }

    @Override
    public void restart()
    {
        this.start(Options.SETTING_DIFFICULTY.getWidth(), Options.SETTING_DIFFICULTY.getHeight());
    }
}
