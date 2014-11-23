package com.coggroach.minekeeper.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.coggroach.minekeeper.R;
import com.coggroach.minekeeper.common.ResourceReader;
import com.coggroach.minekeeper.game.TestGame;
import com.coggroach.minekeeper.tile.Tile;
import com.coggroach.minekeeper.tile.TileColour;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TileRenderer extends AbstractGLRenderer
{
    public TestGame game;

    private final Context context;
    private int width, height;

    private float[] mModelMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];

    private final FloatBuffer mModelPositions;
    private final FloatBuffer mModelNormals;
    private final FloatBuffer mModelColors;
    private final FloatBuffer mModelTextureCoordinates;

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

    public float[] XYPos = new float[2];
    public boolean STOP = false;

    private final int FRAMES_PER_SECOND = 60;
    private final int COLOUR_PER_SECOND = 1;
    private final float PERIOD_TIME = 1000 / FRAMES_PER_SECOND;
    private final float COLOUR_PERIOD_TIME = 1000 / COLOUR_PER_SECOND;
    private long LAST_TIME;

    public TileRenderer(Context context)
    {
        this.context = context;
        LAST_TIME = System.currentTimeMillis();
        game = new TestGame();

        final float[] cubePositionData =
                {
                        // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                        // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                        // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                        // usually represent the backside of an object and aren't visible anyways.

                        // Front face
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,

                        // Right face
                        1.0f, 1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, -1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Back face
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,

                        // Left face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, 1.0f, 1.0f,

                        // Top face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Bottom face
                        1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                };

        // X, Y, Z
        // The normal is used in light calculations and is a vector which points
        // orthogonal to the plane of the surface. For a cube model, the normals
        // should be orthogonal to the points of each face.
        final float[] cubeNormalData =
                {
                        // Front face
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,

                        // Right face
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,

                        // Back face
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,

                        // Left face
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,

                        // Top face
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,

                        // Bottom face
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f
                };
        // S, T (or X, Y)
        // Texture coordinate data.
        // Because images have a Y axis pointing downward (values increase as you move down the image) while
        // OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
        // What's more is that the texture coordinates are the same for every face.
        final float[] cubeTextureCoordinateData =
                {
                        // Front face
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,

                        // Right face
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,

                        // Back face
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,

                        // Left face
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,

                        // Top face
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,

                        // Bottom face
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f
                };

        int colourLength = 4 * 6 * 6;

        mModelPositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mModelNormals = ByteBuffer.allocateDirect(cubeNormalData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mModelColors = ByteBuffer.allocateDirect(colourLength * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mModelTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();

        mModelPositions.put(cubePositionData).position(0);
        mModelNormals.put(cubeNormalData).position(0);
        mModelColors.put(getFloatArrayAs(colourLength, 1.0F)).position(0);
        mModelTextureCoordinates.put(cubeTextureCoordinateData).position(0);

    }

    public float[] getFloatArrayAs(int length, float value)
    {
        float[] array = new float[length];
        for(int i = 0; i < array.length; i++)
            array[i] = value;
        return array;
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
    public void setViewMatrix() {
        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = (float)-1.75*game.getWidth() + 4.25F; //y = -1.75x + 4.25 near = 1.0f, far = 30.0f

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = 0.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    @Override
    public void setProjectionMatrix(int i, int j) {
        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) i / j;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0F;
        final float top = 1.0F;
        final float near = 1.0f;
        final float far = 30.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        super.onSurfaceCreated(glUnused, config);

        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        this.setViewMatrix();

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

        mTextureDataHandle = ResourceReader.loadTexture(context, R.drawable.bordered_empty);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        super.onSurfaceChanged(glUnused, width, height);
        this.setProjectionMatrix(width, height);
        this.width = width;
        this.height = height;
    }

    float eyeZ = 0.0F;
    int size = 2;

    public void handleLocations()
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
        eyeZ = eyeZ > 100.0F ? 0.0F : eyeZ + 1.0F;

        int h = game.getHeight();
        int w = game.getWidth();

        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
            {
                Matrix.setIdentityM(mModelMatrix, 0);
                float x = w-1;
                float y = h-1;

                x -= 2.0F * i;
                y -= 2.0F * j;

                Matrix.translateM(mModelMatrix, 0, x, y, 5.0f);
                drawTile(game.getTile(i, j), mModelMatrix);
            }

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0, 0, -eyeZ);
        //Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        //Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
        drawLight();

        LAST_TIME = System.currentTimeMillis();
    }


    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        handleLocations();
        onDrawFrame();
    }

    public void drawTile(Tile tile, float[] mModelMatrix)
    {
        mModelPositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mModelPositions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        mModelNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 0, mModelNormals);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        mModelTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, mModelTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        GLES20.glUniform4fv(mUniformColorHandle, 1, tile.getColour().toFloatArray(), 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    public void drawLight()
    {
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

        Matrix.orthoM(matrix, 0, 0, width, height, 0, 0, 1);
        Matrix.multiplyMV(normPoint, 0, matrix, 0, normPoint, 0);

        return normPoint;
    }

    public Tile getTileFromWorld(float xWorld, float yWorld)
    {
        Tile tile = null;

            int x = (int) (game.getWidth() * (xWorld + 0.5));
            int y = (int) (game.getHeight() * -1 * (yWorld - 0.5));

            Log.i("Converted Point1:", String.valueOf(x) + " " + String.valueOf(y));

            Log.i("Converted Point2:", String.valueOf(x) + " " + String.valueOf(y));

            if((x >= 0 && y >= 0) && x < game.getWidth() && y < game.getHeight())
                tile = game.getTile(x, y);

        return tile;
    }
}
