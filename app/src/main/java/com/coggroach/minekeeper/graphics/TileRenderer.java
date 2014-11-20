package com.coggroach.minekeeper.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TileRenderer extends AbstractGLRenderer
{
    @Override
    public String getVertexShader() {
        return null;
    }

    @Override
    public String getFragmentShader() {
        return null;
    }

    @Override
    public String getVertexShader(int resId) {
        return null;
    }

    @Override
    public String getFragmentShader(int resId) {
        return null;
    }

    @Override
    public void setViewMatrix() {

    }

    @Override
    public void setProjectionMatrix(int i, int j) {

    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        super.onSurfaceCreated(glUnused, config);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        super.onSurfaceChanged(glUnused, width, height);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {

    }
}
