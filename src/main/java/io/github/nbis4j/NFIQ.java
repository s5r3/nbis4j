package io.github.nbis4j;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

import javax.imageio.ImageIO;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class NFIQ {
    private static boolean initialized;

    private static void initialize() {
        synchronized (NFIQ.class) {
            if (initialized) {
                return;
            }

            try {
                Utils.extractNativeLibrary("nfiq");
                initialized = true;
            } catch (IOException e) {
                throw new RuntimeException("Error loading native nfiq library", e);
            }
        }
    }

    public static int compute(byte[] bytes, ImageFormat format) {
        if (!initialized) {
            initialize();
        }

        switch (format) {
            case BMP:
                Raster raster;
                try {
                    raster = ImageIO.read(new ByteArrayInputStream(bytes)).getData();
                    byte[] bitmap = ((DataBufferByte) raster.getDataBuffer()).getData();
                    IntByReference nfiq = new IntByReference();
                    int status = CLibrary.INSTANCE.compute_nfiq_from_bitmap(bitmap, raster.getWidth(), raster.getHeight(), nfiq);
                    if (status != 0) {
                        throw new NBISException("Error in native layer", status);
                    }
                    return nfiq.getValue();
                } catch (IOException e) {
                    throw new NBISException("Error reading BMP", e, 1000);
                }
            case WSQ:
                IntByReference nfiq = new IntByReference();
                int status = CLibrary.SYNC_INSTANCE.compute_nfiq_from_wsq(bytes, bytes.length, nfiq);
                if (status != 0) {
                    throw new NBISException("Error in native layer", status);
                }
                return nfiq.getValue();
            default:
                throw new NBISException("Unsupported image format", 1000);
        }
    }

    private interface CLibrary extends Library {
        CLibrary INSTANCE = Native.load("nfiq", CLibrary.class);
        CLibrary SYNC_INSTANCE = (CLibrary) Native.synchronizedLibrary(INSTANCE);

        int compute_nfiq_from_bitmap(byte[] bitmap, int width, int height, IntByReference nfiq);
        int compute_nfiq_from_wsq(byte[] wsq, int len, IntByReference nfiq);
    }
}
