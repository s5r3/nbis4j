package io.github.nbis4j;

public class Bitmap {
    public final byte[] pixels;
    public final int width;
    public final int height;

    public Bitmap(byte[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }
}
