package com.coggroach.minekeeper.game;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coggroach.minekeeper.graphics.TileRenderer;
import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TARDIS on 23/11/2014.
 */
public class RainbowGame extends Game
{
    private boolean isGenerated;
    private boolean canRestart;
    private boolean isGameOn;
    private boolean isRendering = false;
    private int score;
    private TileColour defaultColour = TileColour.white;
    private View.OnClickListener endGameListener;

    public int getScore()
    {
        return score;
    }

    public void incScore()
    {
        this.score++;
    }

    public RainbowGame()
    {
        this(Options.SETTING_DIFFICULTY.getWidth(), Options.SETTING_DIFFICULTY.getHeight());
    }

    protected RainbowGame(int w, int h)
    {
        this.start(w, h);
    }

    @Override
    public boolean isRendering()
    {
        return isRendering;
    }

    @Override
    public void initUIElements(Context c)
    {
        this.UIElements = new ArrayList<View>();
        this.UILayout = new LinearLayout(c);

        TextView score = new TextView(c);
        TextView status = new TextView(c);

        endGameListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!(isGameOn()))
                {
                    updateStatus("New Game");
                    restart();
                    generate();
                }
            }
        };

        status.setOnClickListener(endGameListener);

        ((LinearLayout) UILayout).addView(score);
        ((LinearLayout) UILayout).addView(status);

        UIElements.add(score);
        UIElements.add(status);

        updateScore();
        updateStatus("New Game");
    }

    private void updateScore()
    {
        ((TextView) UIElements.get(0)).setText("Score: " + this.score);
    }

    private void updateStatus(String s)
    {
        ((TextView) UIElements.get(1)).setText(s);
    }

    @Override
    public void start(int w, int h)
    {
        this.isGenerated = false;
        this.canRestart = true;
        this.isRendering = true;
        this.isGameOn = true;
        this.score = 0;

        this.height = w;
        this.width = h;
        this.tiles = new Tile[w * h];
        for(int i = 0; i < tiles.length; i++)
        {
            tiles[i] = new Tile(i, defaultColour);
        }
    }

    @Override
    public void restart()
    {
        if(canRestart)
        {
            this.isGenerated = false;
            this.score = 0;
            this.isGameOn = true;
            for(int i = 0; i < tiles.length; i++)
            {
                tiles[i] = new Tile(i, defaultColour);
            }
        }
    }

    @Override
    public void generate()
    {
        if(!isGenerated)
        {
            Random rand = new Random();
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            this.getTile(x, y).getStats().setMine(true);
            this.generateGrid(x, y);
        }
    }

    @Override
    public boolean isGameOn()
    {
        return isGameOn;
    }

    @Override
    public void setGameOn(boolean b)
    {
        this.isGameOn = b;
    }

    @Override
    public void onTouch(View v, MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if((this.isGameOn()))
            {
                float[] worldPos = TileRenderer.getWorldPosFromProjection(event.getX(), event.getY(), v.getWidth(), v.getHeight());

                int iTile = this.getTileIndexFromWorld(worldPos[0], worldPos[1]);

                if (iTile != Integer.MIN_VALUE)
                {
                    if (!this.getTile(iTile).getStats().isPressed())
                    {
                        this.incScore();
                        this.getTile(iTile).getStats().setPressed(true);
                        this.updateScore();
                    }
                    if(this.getTile(iTile).getStats().isMine())
                    {
                        this.setGameOn(false);
                        this.updateStatus("Congratz, Click me to Continue!");
                    }
                }
            }
        }
    }

    protected void generateGrid(int x, int y)
    {
        for(int i = 0; i < 8; i++)//height
        {
            this.drawLine( 1, -1, x, y + i, i, i);
            this.drawLine(-1, -1, x, y + i, i, i);
            this.drawLine(-1, 1, x, y - i, i, i);
            this.drawLine( 1, 1, x, y - i, i, i);
        }
    }

    protected void drawLine(int m, int n, int x, int y, int r, int c)
    {
        for(int i = 0; i <= r; i++)
        {
            this.setColourWithinBounds(x + i*m, y + i*n, c);
        }
    }

    protected boolean setColourWithinBounds(int x, int y, int i)
    {
        if(x >= 0 && x < width && y >= 0 && y < height)
        {
            this.getTile(x, y).setColour(this.getIndexedTileColour(i));
            return true;
        }
        return false;
    }

    public int getTileIndexFromWorld(float xWorld, float yWorld)
    {
        if(Math.abs(yWorld) > 1.0F)
            return Integer.MIN_VALUE;

        float tileWidth = 2.0F/getWidth();
        float tileHeight = 2.0F/getHeight();

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

    public Tile getTileFromWorld(float xWorld, float yWorld)
    {
        return this.getTile(getTileIndexFromWorld(xWorld, yWorld));
    }

    public TileColour getIndexedTileColour(int i)
    {
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
            default:
                return TileColour.white;
        }
    }

    public boolean isGenerated()
    {
        return isGenerated;
    }
}
