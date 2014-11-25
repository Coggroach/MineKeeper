package com.coggroach.minekeeper.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.coggroach.minekeeper.R;
import com.coggroach.minekeeper.activities.GameActivity;
import com.coggroach.minekeeper.common.ResourceReader;
import com.coggroach.minekeeper.game.Options;
import com.coggroach.minekeeper.tile.Tile;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TileRenderer extends AbstractGLRenderer
{
    private final Context context;
    private int width, height;

    private float[] mModelMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];

    private int mLightPosHandle;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;
    private int mUniformColorHandle;

    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];

    private int mProgramHandle;
    private int mPointProgramHandle;
    private int mTextureDataHandle;

    public TileRenderer(Context context)
    {
        this.context = context;
    }

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
        return ResourceReader.getString(context, resId);
    }

    @Override
    public String getFragmentShader(int resId) {
        return ResourceReader.getString(context, resId);
    }

    @Override
    public void setViewMatrix()
    {
        final float eyeZ = (float) (Options.SETTING_DIFFICULTY.getWidth()/((double) width/height )) + RenderSettings.OBJECT_POSITION_Z + RenderSettings.NEAR_Z;
        //Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        Matrix.setLookAtM(mViewMatrix, 0, 0.0F, 0.0F, eyeZ, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public void setProjectionMatrix(int i, int j)
    {
        final float ratio = (float) i / j;
        final float FAR_Z = Options.SETTING_DIFFICULTY.getWidth()/ratio + RenderSettings.OBJECT_LENGTH_Z;
        //Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0F, 1.0F, RenderSettings.NEAR_Z, FAR_Z);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        super.onSurfaceCreated(glUnused, config);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final String vertexShader = getVertexShader(R.raw.per_pixel_vertex_shader);
        final String fragmentShader = getFragmentShader(R.raw.per_pixel_fragment_shader);

        final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate", "u_Color"});

        final String pointVertexShader = getVertexShader(R.raw.point_vertex_shader);
        final String pointFragmentShader = getFragmentShader(R.raw.point_fragment_shader);

        final int pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
        final int pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);
        mPointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle, new String[] {"a_Position"});

        mTextureDataHandle = ResourceReader.loadTexture(context, Options.TEXTURE_ID);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        super.onSurfaceChanged(glUnused, width, height);
        this.width = width;
        this.height = height;

        this.setProjectionMatrix(width, height);
        this.setViewMatrix();
    }

    public void storeHandleLocations()
    {
        GLES20.glUseProgram(mProgramHandle);
        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_LightPos");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
        mUniformColorHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Color");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
    }

    public void onDrawFrame()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if(((GameActivity) context).game.isRendering())
        {
            int h = Options.SETTING_DIFFICULTY.getHeight();
            int w = Options.SETTING_DIFFICULTY.getWidth();

            for (int j = 0; j < h; j++)
            {
                for (int i = 0; i < w; i++)
                {
                    float x = w - 1 - RenderSettings.OBJECT_LENGTH_Z * i;
                    float y = h - 1 - RenderSettings.OBJECT_LENGTH_Z * j;

                    Matrix.setIdentityM(mModelMatrix, 0);
                    Matrix.translateM(mModelMatrix, 0, x, y, RenderSettings.OBJECT_POSITION_Z);
                    drawTile(((GameActivity) context).game.getTile(i, j), mModelMatrix);
                }
            }
            Matrix.setIdentityM(mLightModelMatrix, 0);
            Matrix.translateM(mLightModelMatrix, 0, 0.0F, 0.0F, RenderSettings.OBJECT_POSITION_Z + 5.0F);
            drawLight(mLightModelMatrix);
        }
    }


    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        storeHandleLocations();
        onDrawFrame();
    }

    public void drawTile(Tile tile, float[] mModelMatrix)
    {
        if(tile != null)
        {
            Tile.getModelPositions().position(0);
            GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, Tile.getModelPositions());
            GLES20.glEnableVertexAttribArray(mPositionHandle);

            Tile.getModelNormals().position(0);
            GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 0, Tile.getModelNormals());
            GLES20.glEnableVertexAttribArray(mNormalHandle);

            Tile.getModelTextureCoordinates().position(0);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, Tile.getModelTextureCoordinates());
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

            float[] colour = tile.getDrawingColour();

            GLES20.glUniform4fv(mUniformColorHandle, 1, colour, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
            GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
            GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        }
    }

    public void drawLight(float[] mLightModelMatrix)
    {
        //Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        //Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        GLES20.glUseProgram(mPointProgramHandle);
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);
        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

    public float[] getWorldPosFromProjection(float xPoint, float yPoint)
    {
        float[] normPoint = new float[] {xPoint, yPoint, 1.0F, 1.0F};
        float[] matrix = new float[16];

        float f = (height - width)/2;

        Matrix.orthoM(matrix, 0, 0, width, width + f, f, 0, 1);
        Matrix.multiplyMV(normPoint, 0, matrix, 0, normPoint, 0);

        return normPoint;
    }
}
