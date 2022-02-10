package io.github.nbis4j;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.io.IOException;

public class WSQCodec {
    private static boolean initialized;

    private static void initialize() {
        synchronized (WSQCodec.class) {
            if (initialized) {
                return;
            }

            try {
                Utils.extractNativeLibrary("wsqc");
                initialized = true;
            } catch (IOException e) {
                throw new RuntimeException("Error loading native wsqc library", e);
            }
        }
    }

    public static byte[] encode(Bitmap bitmap) {
        if (!initialized) {
            initialize();
        }

        PointerByReference wsqReference = new PointerByReference();
        IntByReference length = new IntByReference();
        int status = CLibrary.INSTANCE.encode_bitmap(bitmap.pixels, bitmap.width, bitmap.height, wsqReference, length);
        if (status != 0) {
            throw new NBISException("Error in native layer", status);
        }
        byte[] wsq = new byte[length.getValue()];
        System.arraycopy(wsqReference.getValue().getByteArray(0, length.getValue()), 0, wsq, 0, length.getValue());
        Native.free(Pointer.nativeValue(wsqReference.getValue()));
        return wsq;
    }

    public static Bitmap decode(byte[] wsq) {
        if (!initialized) {
            initialize();
        }

        PointerByReference bitmapReference = new PointerByReference();
        IntByReference width = new IntByReference();
        IntByReference height = new IntByReference();
        int status = CLibrary.INSTANCE.decode_wsq(wsq, wsq.length, bitmapReference, width, height);
        if (status != 0) {
            throw new NBISException("Error in native layer", status);
        }
        int size = width.getValue() * height.getValue();
        byte[] pixels = new byte[size];
        System.arraycopy(bitmapReference.getValue().getByteArray(0, size), 0, pixels, 0, size);
        Native.free(Pointer.nativeValue(bitmapReference.getValue()));
        return new Bitmap(pixels, width.getValue(), height.getValue());
    }

    private interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.synchronizedLibrary(Native.load("wsqc", CLibrary.class));

        int encode_bitmap(byte[] bitmap, int width, int height, PointerByReference wsq, IntByReference length);

        int decode_wsq(byte[] wsq, int length, PointerByReference bitmap, IntByReference width, IntByReference height);
    }
}
