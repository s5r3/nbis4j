#include "nbis4j_wsqc.h"
#include <wsq.h>

int debug = 0; // Needed by wsq_decode_mem(..)
int grayscale_depth = 8;
float wsq_bitrate = 0.75f;

int encode_bitmap(byte *bitmap, int width, int height, byte **wsq, int *len) {
    int ret = wsq_encode_mem(wsq, len, wsq_bitrate, bitmap, width, height, grayscale_depth, -1, "NBIS v5");
    if (ret != 0) {
        if (*wsq != 0) {
            free(wsq);
        }
        return 9900 + ret;
    }

    return 0;
}

int decode_wsq(byte *wsq, int len, byte **bitmap, int *width, int *height) {
    int depth, ppi, lossy;
    int ret = wsq_decode_mem(bitmap, width, height, &depth, &ppi, &lossy, wsq, len);
    if (ret != 0) {
        if (*bitmap != NULL) {
            free(bitmap);
        }
        return 9900 + ret;
    }

    if (depth != 8) {
        free(bitmap);
        return 9900;
    }

    return 0;
}
