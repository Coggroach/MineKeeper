package com.coggroach.minekeeper.tile;

/**
 * Created by TARDIS on 20/11/2014.
 */
public class TileColour
{
    public float R, G, B, A;

    public static TileColour red = new TileColour(1.0F, 0.0F, 0.0F, 1.0F);
    public static TileColour orange = new TileColour(1.0F, 0.5F, 0.0F, 1.0F);
    public static TileColour yellow = new TileColour(1.0F, 1.0F, 0.0F, 1.0F);
    public static TileColour green = new TileColour(0.5F, 1.0F, 0.0F, 1.0F);
    public static TileColour blue = new TileColour(0.0F, 0.0F, 1.0F, 1.0F);
    public static TileColour purple = new TileColour(0.5F, 0.0F, 1.0F, 1.0F);
    public static TileColour pink = new TileColour(1.0F, 0.0F, 1.0F, 1.0F);

    public static TileColour cyan = new TileColour(0.0F, 1.0F, 1.0F, 1.0F);
    public static TileColour white = new TileColour(1.0F, 1.0F, 1.0F, 1.0F);
    public static TileColour grey = new TileColour(0.45F, 0.45F, 0.45F, 1.0F);
    public static TileColour black = new TileColour(0.1F, 0.1F, 0.1F, 0.5F);

    public TileColour(float i, float j, float k, float l)
    {
        this.R = i;
        this.G = j;
        this.B = k;
        this.A = l;
    }

    public boolean isEqual(TileColour c)
    {
        return this.R == c.R && this.G == c.G && this.B == c.B;
    }

    public float[] toFloatArray()
    {
        return new float[] {R, G, B, A};
    }


}
