package com.coggroach.minekeeper;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.coggroach.minekeeper.graphics.TileRenderer;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class GameActivity extends Activity
{
    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);

        mGLView.setRenderer(new TileRenderer(this));

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
