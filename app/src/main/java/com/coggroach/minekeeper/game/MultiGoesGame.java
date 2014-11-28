package com.coggroach.minekeeper.game;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private boolean hasWon = false;
    private boolean isRendering = false;
    private boolean updateView = false;

    private int score;
    private int lives;
    private TileColour defaultColour = TileColour.white;
    private View.OnClickListener endGameListener;

    public MultiGoesGame()
    {
        this(Options.SETTING_DIFFICULTY.getWidth(), Options.SETTING_DIFFICULTY.getHeight());
    }

    protected MultiGoesGame(int w, int h)
    {
        this.start(w, h);
    }

    public int getLives()
    {
        return lives;
    }

    public void incLives()
    {
        this.lives += 3;
    }

    public void incDifficulty()
    {
        if(Options.SETTING_DIFFICULTY == Difficulty.EASY)
            Options.SETTING_DIFFICULTY = Difficulty.MEDIUM;
        else if(Options.SETTING_DIFFICULTY == Difficulty.MEDIUM)
            Options.SETTING_DIFFICULTY = Difficulty.HARD;
        else if(Options.SETTING_DIFFICULTY == Difficulty.HARD)
            Options.SETTING_DIFFICULTY = Difficulty.EXPERT;
        else if(Options.SETTING_DIFFICULTY == Difficulty.EXPERT)
            Options.SETTING_DIFFICULTY = Difficulty.MASSIVE;
    }

    public void resetDifficulty()
    {
        Options.SETTING_DIFFICULTY = Difficulty.EASY;
    }

    public void decLives()
    {
        this.lives--;
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

        TextView lives = new TextView(c);
        TextView score = new TextView(c);
        TextView status = new TextView(c);

        endGameListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!(isGameOn()))
                {
                    isRendering = false;
                    if(hasWon) {
                        incDifficulty();
                        updateStatus(" ");
                    }
                    else {
                        resetDifficulty();
                        updateStatus("New Game");
                    }
                    updateLives();
                    updateScore();
                    restart();
                    generate();

                }
            }
        };

        status.setOnClickListener(endGameListener);

        LinearLayout line = new LinearLayout(c);

        line.addView(lives);
        line.addView(score);
        line.setOrientation(LinearLayout.HORIZONTAL);

        ((LinearLayout) UILayout).addView(line);
        ((LinearLayout) UILayout).addView(status);
        ((LinearLayout) UILayout).setOrientation(LinearLayout.VERTICAL);

        lives.setTextSize(30);
        lives.setTextColor(Color.GREEN);
        score.setTextSize(30);
        score.setTextColor(Color.CYAN);
        status.setTextSize(20);
        status.setTextColor(Color.WHITE);

        UIElements.add(lives);
        UIElements.add(status);
        UIElements.add(score);

        updateLives();
        updateScore();
        updateStatus("New Game");
    }

    private void updateLives()
    {
        ((TextView) UIElements.get(0)).setText("Lives: " + this.lives + "   ");
    }

    private void updateStatus(String s)
    {
        ((TextView) UIElements.get(1)).setText(s);
    }

    private void updateScore()
    {
        ((TextView) UIElements.get(2)).setText(" Score: " + this.score);
    }

    @Override
    public void start(int w, int h)
    {
        this.isGenerated = false;
        this.canRestart = true;
        this.isRendering = true;
        this.isGameOn = true;
        this.lives = 20;
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
            this.canRestart = true;
            //this.isRendering = true;
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
            this.updateView(true);
            this.isRendering = true;
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
            GameHelper.generateGrid(this, x, y, 35);
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
                            this.decLives();
                        this.getTile(iTile).getStats().setPressed(true);
                        this.updateLives();
                    }
                    if(this.getLives() <= 0)
                    {
                        hasWon = false;
                        this.updateStatus("Hard Luck, Click me to Play Again");
                        this.lives = 20;
                        this.score = 0;
                        this.setGameOn(false);
                    }
                    if(this.getTile(iTile).getStats().isMine())
                    {
                        hasWon = true;
                        this.updateStatus("Well Done! Click me to Keep Going");
                        this.incLives();
                        this.incScore();
                        this.setGameOn(false);
                    }
                }
            }
        }
    }

    @Override
    public void updateView(boolean b)
    {
        this.updateView = b;
    }

    @Override
    public boolean getUpdateView()
    {
        return this.updateView;
    }

    public boolean isGenerated()
    {
        return isGenerated;
    }

    public void incScore() {
        this.score++;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
