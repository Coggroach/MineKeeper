package com.coggroach.minekeeper.tile;

/**
 * Created by TARDIS on 23/11/2014.
 */
public class TileStats
{
    private int id;
    private  boolean isMine;
    private boolean isPressed;

    public TileStats(int i)
    {
        this.id = i;
        this.isMine = false;
        this.isPressed = false;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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
