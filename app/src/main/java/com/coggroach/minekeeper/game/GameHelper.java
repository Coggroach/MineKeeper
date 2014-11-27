package com.coggroach.minekeeper.game;

import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

/**
 * Created by TARDIS on 27/11/2014.
 */
public class GameHelper
{
    public static int getTileIndexFromWorld(Game game, float xWorld, float yWorld)
    {
        if(Math.abs(yWorld) > 1.0F)
            return Integer.MIN_VALUE;

        float tileWidth = 2.0F/game.getWidth();
        float tileHeight = 2.0F/game.getHeight();

        int index = 0;
        for(float j = 1.0F; j >= -1.0F; j = j - tileHeight)
            for(float i = 0.999999F ; i >= -1.0F; i = i - tileWidth)
            {
                boolean lessThanI = xWorld < i;
                boolean lessThanJ = yWorld < j;
                boolean greaterThanIMinus = xWorld > i - tileWidth;
                boolean greaterThanJMinus = yWorld > j - tileHeight;

                if(lessThanI && greaterThanIMinus && lessThanJ && greaterThanJMinus)
                {
                    return index;
                }
                index++;
            }

        return index;
    }

    public static Tile getTileFromWorld(Game game, float xWorld, float yWorld)
    {
        return game.getTile(getTileIndexFromWorld(game, xWorld, yWorld));
    }

    protected static void generateGrid(Game game, int x, int y, int r)
    {
        for(int i = 0; i <= r; i++)//height
        {
            drawLine(game,  1, -1, x, y + i, i, i);
            drawLine(game, -1, -1, x, y + i, i, i);
            drawLine(game, -1, 1, x, y - i, i, i);
            drawLine(game, 1, 1, x, y - i, i, i);
        }
    }

    protected static void drawLine(Game game, int m, int n, int x, int y, int r, int c)
    {
        for(int i = 0; i <= r; i++)
        {
            setColourWithinBounds(game, x + i * m, y + i * n, c);
        }
    }

    protected static boolean setColourWithinBounds(Game game, int x, int y, int i)
    {
        if(x >= 0 && x < game.getWidth() && y >= 0 && y < game.getHeight())
        {
            game.getTile(x, y).setColour(getIndexedTileColour(i));
            return true;
        }
        return false;
    }

    public static TileColour getIndexedTileColour(int i)
    {
        if(i > 8)
        {
            i = 8;
        }
        switch (i)
        {
            case 0:
                return TileColour.grey;
            case 1:
                return TileColour.red;
            case 2:
                return TileColour.orange;
            case 3:
                return TileColour.yellow;
            case 4:
                return TileColour.green;
            case 5:
                return TileColour.blue;
            case 6:
                return TileColour.purple;
            case 7:
                return TileColour.pink;
            case 8:
                return TileColour.black;
            default:
                return TileColour.white;
        }
    }
}
