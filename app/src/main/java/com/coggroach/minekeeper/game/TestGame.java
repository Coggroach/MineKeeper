package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

import java.util.Random;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TestGame extends Game
{
    public static TileColour red = new TileColour(1.0F, 0.0F, 0.0F, 1.0F);
    public static TileColour cyan = new TileColour(0.0F, 1.0F, 1.0F, 1.0F);
    public static TileColour white = new TileColour(1.0F, 1.0F, 1.0F, 1.0F);
    public static TileColour grey = new TileColour(0.45F, 0.45F, 0.45F, 1.0F);
    public static TileColour blue = new TileColour(0.0F, 0.0F, 1.0F, 1.0F);

    public TestGame()
    {
        this(Options.SETTING_DIFFICULTY.getWidth(), Options.SETTING_DIFFICULTY.getHeight());
        //this.tiles[1].setColour(cyan);
    }

    public TestGame(int w, int h)
    {
        this.start(w, h);
    }

    public void generate()
    {
        Random rand = new Random();
        tiles[rand.nextInt(tiles.length)].setMine(true);
    }

    public void start(int w, int h)
    {
        this.height = w;
        this.width = h;
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
