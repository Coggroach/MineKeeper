package com.coggroach.minekeeper.game;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.coggroach.minekeeper.tile.Tile;

import java.util.ArrayList;

/**
 * Created by TARDIS on 20/11/2014.
 */
public abstract class Game
{
    protected Tile[] tiles;
    protected int width, height;
    protected ArrayList<View> UIElements;
    protected View UILayout;

    public abstract boolean isRendering();
    public abstract void initUIElements(Context c);
    public abstract void start(int i, int j);
    public abstract void restart();
    public abstract void generate();
    public abstract boolean isGameOn();
    public abstract void setGameOn(boolean b);
    public abstract void onTouch(View v, MotionEvent event);
    public abstract void updateView(boolean b);
    public abstract boolean getUpdateView();


    public Tile getTile(int x, int y)
    {
        return (x + y*height < tiles.length) ? tiles[x + y*height] : null;
    }

    public boolean isInBound(int x, int y)
    {
        return (float) width > (float) (x/height + y); //Divide Both sides by Height; Less Calculations;
    }

    public Tile[] getTiles()
    {
        return tiles;
    }

    public Tile getTile(int i)
    {
        return (tiles.length > i) ? tiles[i] : null;
    }

    public void setTile(Tile t, int i)
    {
        this.tiles[i] = t;
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

    public ArrayList<View> getUIElements()
    {
        return UIElements;
    }

    public View getUILayout()
    {
        return UILayout;
    }

}
