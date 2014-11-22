package com.coggroach.minekeeper;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.coggroach.minekeeper.game.TestGame;
import com.coggroach.minekeeper.graphics.TileRenderer;
import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

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
            mGLRender.XYPos[0] = event.getX();
            mGLRender.XYPos[1] = event.getY();
            if(event.getAction() == MotionEvent.ACTION_DOWN)
                mGLRender.STOP = true;

            //float[] worldPos = mGLRender.getWorldCoordinatesFromProjection(mGLRender.XYPos);

            //Tile tile = mGLRender.getTileFromWorld(worldPos);

            //if (tile != null)
            //{
             //   tile.setColour(TestGame.red);
           // }
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

        this.setContentView(mGLView);
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
