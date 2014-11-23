package com.coggroach.minekeeper.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coggroach.minekeeper.game.Difficulty;
import com.coggroach.minekeeper.game.Game;
import com.coggroach.minekeeper.game.Options;
import com.coggroach.minekeeper.game.RainbowGame;
import com.coggroach.minekeeper.graphics.TileRenderer;
import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

import java.util.Random;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class GameActivity extends Activity
{
    public Game game;
    private GLSurfaceView mGLView;
    private TileRenderer mGLRender;
    private TextView score;
    private TextView status;

    private void setScoreText()
    {
        score.setText("Score: " + ((RainbowGame) game).getScore() + " ");
    }

    private void setStatusText(String s)
    {
        status.setText(s);
    }

    private View.OnTouchListener listener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
            {
                if(!((RainbowGame) game).isEnded())
                {
                    float[] worldPos = mGLRender.getWorldPosFromProjection(event.getX(), event.getY());

                    int iTile = ((RainbowGame) game).getTileIndexFromWorld(worldPos[0], worldPos[1]);

                    if (iTile != Integer.MIN_VALUE)
                    {
                        if (!game.getTile(iTile).getStats().isPressed())
                        {
                            ((RainbowGame) game).incScore();
                            game.getTile(iTile).getStats().setPressed(true);
                        }
                        if (game.getTile(iTile).getStats().isMine())
                        {
                            setStatusText("Congrats, Click me to Play Again!");
                            ((RainbowGame) game).setEnded();

                        }
                        setScoreText();
                    }
                }
            }
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGLView = new GLSurfaceView(this);
        game = new RainbowGame();
        mGLRender = new TileRenderer(this);

        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(mGLRender);
        mGLView.setOnTouchListener(listener);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((RainbowGame) game).generateMine();

        this.setContentView(mGLView);

        //Screen Guis
        score = new TextView(this);
        status = new TextView(this);
        LinearLayout layout = new LinearLayout(this);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setScoreText();
        setStatusText("New Game");

        status.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(((RainbowGame) game).isEnded())
                {
                    setStatusText("New Game");
                    game.restart();
                    ((RainbowGame) game).generateMine();
                }
            }
        });

        layout.addView(score);
        layout.addView(status);
        addContentView(layout, params);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGLView.onResume();
    }
}
