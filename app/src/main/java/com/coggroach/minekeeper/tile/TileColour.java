package com.coggroach.minekeeper.tile;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TileColour
{
    public float R, G, B, A;

    public TileColour(float i, float j, float k, float l)
    {
        this.R = i;
        this.G = j;
        this.B = k;
        this.A = l;
    }

    public float[] toFloatArray()
    {
        return new float[] {R, G, B, A};
    }


}
