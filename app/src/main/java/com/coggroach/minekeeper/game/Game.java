package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.tile.Tile;

/**
 * Created by TARDIS on 20/11/2014.
 */
public abstract class Game
{
    private Tile[] tiles;

    public Tile[] getTiles()
    {
        return tiles;
    }

    public void setTiles(Tile[] tiles)
    {
        this.tiles = tiles;
    }
}
