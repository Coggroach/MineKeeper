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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);
        mGLRender = new TileRenderer(this);
        mGLView.setEGLContextClientVersion(2);

        mGLView.setRenderer(mGLRender);

        mGLView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                mGLRender.XYPos[0] = motionEvent.getX();
                mGLRender.XYPos[1] = motionEvent.getY();

                float[] worldPos = mGLRender.getWorldCoordinatesFromProjection(mGLRender.XYPos);

                Tile tile = mGLRender.getTileFromWorld(worldPos);

                if(tile != null)
                {
                    tile.setColour(TestGame.red);
                }

                return false;
            }
        });

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
