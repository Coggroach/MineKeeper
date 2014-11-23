package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.tile.Tile;

/**
 * Created by TARDIS on 20/11/2014.
 */
public abstract class Game
{
    protected Tile[] tiles;
    protected int width, height;

    public Tile getTile(int x, int y)
    {
        return (x + y*height < tiles.length) ? tiles[x + y*height] : null;
    }

    public boolean isInBound(int x, int y)
    {
        return (float) width > (float) (x/height + y); //Divide Both sides by Height; Less Calculations;
    }

    public abstract void restart();

    public Tile[] getTiles()
    {
        return tiles;
    }

    public int getTilesLength()
    {
        return tiles.length;
    }

    public void setTiles(Tile[] tiles)
    {
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
