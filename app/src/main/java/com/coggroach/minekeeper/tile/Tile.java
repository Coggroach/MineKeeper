package com.coggroach.minekeeper.tile;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class Tile
{
    private int id;
    private TileColour colour;
    private  boolean isMine;
    private boolean isPressed;

    public Tile(int i, TileColour c, boolean b, boolean p)
    {
        this.id = i;
        this.colour = c;
        this.isMine = b;
        this.isPressed = p;
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

    public boolean isPressed()
    {
        return isPressed;
    }

    public void setPressed(boolean isPressed)
    {
        this.isPressed = isPressed;
    }
}
