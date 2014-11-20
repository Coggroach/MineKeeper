package com.coggroach.minekeeper.tile;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class Tile
{
    private int id;
    private TileColour colour;
    private  boolean isMine;

    public Tile(int i, TileColour c, boolean b)
    {
        this.id = i;
        this.colour = c;
        this.isMine = b;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public TileColour getColour()
    {
        return colour;
    }

    public void setColour(TileColour colour)
    {
        this.colour = colour;
    }

    public boolean isMine()
    {
        return isMine;
    }

    public void setMine(boolean isMine)
    {
        this.isMine = isMine;
    }
}
