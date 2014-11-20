package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TestGame extends Game
{
    static TileColour red = new TileColour(1.0F, 0.0F, 0.0F, 1.0F);

    public TestGame()
    {
        Difficulty d = Options.SETTING_DIFFICULTY;
        this.height = d.getHeight();
        this.width = d.getWidth();

        this.tiles = new Tile[width * height];
        for(int i = 0; i < tiles.length; i++)
        {
            tiles[i] = new Tile(i, red, false);
        }
    }

}
