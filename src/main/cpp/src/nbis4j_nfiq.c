#include "nbis4j_nfiq.h"
#include <nfiq.h>
#include <wsq.h>

int debug = 0; // Needed by wsq_decode_mem(..)

int compute_nfiq_from_bitmap(byte *bitmap, int width, int height, int *nfiq) {
    int onfiq;
    float oconf;
    int optflag = 0;
    int ret = comp_nfiq(&onfiq, &oconf,  bitmap, width, height, 8, -1, &optflag);

    if (ret != 0) {
        return 9900 + ret;
    }
    
    *nfiq = onfiq;
    return 0;
}

int compute_nfiq_from_wsq(byte *wsq, int len, int *nfiq) {
    byte *bitmap;
    int width, height, depth, ppi, lossy;
    int ret = wsq_decode_mem(&bitmap, &width, &height, &depth, &ppi, &lossy, wsq, len);
    if (ret != 0) {
        if (bitmap != NULL) {
            free(bitmap);
        }
        return 9900 + ret;
    }
    
    int onfiq;
    float oconf;
    int optflag = 0;
    ret = comp_nfiq(&onfiq, &oconf,  bitmap, width, height, 8, ppi, &optflag);
    free(bitmap);
    
    if (ret != 0) {
        return 9900 + ret;
    }
    
    *nfiq = onfiq;
    return 0;
}

