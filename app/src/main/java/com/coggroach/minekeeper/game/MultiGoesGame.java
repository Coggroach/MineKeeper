package com.coggroach.minekeeper.game;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coggroach.minekeeper.graphics.TileRenderer;
import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TARDIS on 27/11/2014.
 */
public class MultiGoesGame extends Game
{
    private boolean isGenerated;
    private boolean canRestart;
    private boolean isGameOn;
    private boolean isRendering = false;
    private int score;
    private TileColour defaultColour = TileColour.white;
    private View.OnClickListener endGameListener;

    public MultiGoesGame()
    {
        this(Options.SETTING_DIFFICULTY.getWidth(), Options.SETTING_DIFFICULTY.getHeight());
    }

    public MultiGoesGame(int w, int h)
    {
        this.start(w, h);
    }

    public int getScore()
    {
        return score;
    }

    public void incScore()
    {
        this.score += 3;
    }

    public void decScore()
    {
        this.score--;
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
                    updateScore();
                    restart();
                    generate();
                }
            }
        };

        status.setOnClickListener(endGameListener);

        ((LinearLayout) UILayout).addView(score);
        ((LinearLayout) UILayout).addView(status);
        ((LinearLayout) UILayout).setOrientation(LinearLayout.VERTICAL);

        score.setTextSize(30);
        score.setTextColor(Color.WHITE);
        status.setTextSize(20);
        status.setTextColor(Color.WHITE);

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
        this.score = 10;

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
            this.canRestart = true;
            this.isRendering = true;
            this.isGameOn = true;


            int w = Options.SETTING_DIFFICULTY.getWidth();
            int h = Options.SETTING_DIFFICULTY.getHeight();

            this.height = w;
            this.width = h;
            this.tiles = new Tile[w * h];
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
            this.isGenerated = true;
            Random rand = new Random();
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            this.getTile(x, y).getStats().setMine(true);
            GameHelper.generateGrid(this, x, y, 20);
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

                int iTile = GameHelper.getTileIndexFromWorld(this, worldPos[0], worldPos[1]);

                if (iTile != Integer.MIN_VALUE)
                {
                    if (!this.getTile(iTile).getStats().isPressed())
                    {
                        if(!this.getTile(iTile).getStats().isMine())
                            this.decScore();
                        this.getTile(iTile).getStats().setPressed(true);
                        this.updateScore();
                    }
                    if(this.getScore() <= 0)
                    {
                        this.updateStatus("Hard Luck, Click me to Play Again");
                        this.score = 10;
                        this.setGameOn(false);
                    }
                    if(this.getTile(iTile).getStats().isMine())
                    {
                        this.updateStatus("Well Done! Click me to Keep Going");
                        this.incScore();
                        this.setGameOn(false);
                    }
                }
            }
        }
    }

    public boolean isGenerated()
    {
        return isGenerated;
    }
}
