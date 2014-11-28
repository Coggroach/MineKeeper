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
import com.coggroach.minekeeper.game.MultiGoesGame;
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
    private Game game;
    private GLSurfaceView mGLView;
    private TileRenderer mGLRender;
    private boolean updateRenderer;

    private View.OnTouchListener listener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            game.onTouch(v, event);
            return true;
        }
    };

    protected Game getGameFromId(int i)
    {
        switch (i)
        {
            case 0:
                return new RainbowGame();
            case 1:
                return new MultiGoesGame();
            default:
                return  new RainbowGame();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGLView = new GLSurfaceView(this);
        game = getGameFromId(Options.GAMEMODE_ID);
        mGLRender = new TileRenderer(this);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(mGLRender);
        mGLView.setOnTouchListener(listener);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        game.generate();
        game.initUIElements(this);

        this.setContentView(mGLView);
        this.addContentView(game.getUILayout(), params);
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

    public Game getGame()
    {
        return game;
    }
}
