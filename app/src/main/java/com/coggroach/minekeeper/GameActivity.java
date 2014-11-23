package com.coggroach.minekeeper;

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

import com.coggroach.minekeeper.game.Difficulty;
import com.coggroach.minekeeper.game.Options;
import com.coggroach.minekeeper.game.TestGame;
import com.coggroach.minekeeper.graphics.TileRenderer;
import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

import java.util.Random;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class GameActivity extends Activity
{
    private GLSurfaceView mGLView;
    private TileRenderer mGLRender;

    private View.OnTouchListener listener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
            {
                float[] worldPos = mGLRender.getWorldPosFromProjection(event.getX(), event.getY());

                mGLRender.eyeX = worldPos[0];
                mGLRender.eyeY = worldPos[1];

                Tile tile = mGLRender.getTileFromWorld(worldPos[0], worldPos[1]);


                Log.i("Screen Point", String.valueOf(event.getX()) + " " + String.valueOf(event.getY()));
                Log.i("World Point", String.valueOf(worldPos[0]) + " " + String.valueOf(worldPos[1]));

                if (tile != null)
                {
                    Log.i("Tile Index", String.valueOf(tile.getId()));

                    if(tile.getColour().isEqual(TestGame.red))
                        tile.setColour(TestGame.cyan);
                    else if(tile.getColour().isEqual(TestGame.cyan))
                        tile.setColour(TestGame.blue);
                    else if(tile.getColour().isEqual(TestGame.blue))
                        tile.setColour(TestGame.grey);
                    else if(tile.getColour().isEqual(TestGame.grey))
                        tile.setColour(TestGame.red);

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
        mGLRender = new TileRenderer(this);

        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(mGLRender);
        mGLView.setOnTouchListener(listener);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.setContentView(mGLView);

        /*
        Button options = new Button(this);
        LinearLayout layout = new LinearLayout(this);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        params.gravity = 14;
        options.setText("Change Difficulty");

        layout.addView(options);

        addContentView(layout, params);

        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int i = Difficulty.values().length;
                Options.changeDifficulty(Difficulty.values()[new Random().nextInt(i)]);
                mGLRender.game.restart();
                mGLRender.UPDATE_VIEW = true;
            }
        });*/
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
