#ifndef _NFIQ_HELPER_H_
#define _NFIQ_HELPER_H_

typedef unsigned char byte;

#ifdef __cplusplus
extern "C" {
#endif

int compute_nfiq_from_bitmap(byte *bitmap, int width, int height, int *nfiq);
int compute_nfiq_from_wsq(byte *wsq, int len, int *nfiq);

#ifdef __cplusplus
}
#endif

#endif
