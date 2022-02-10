#ifndef _NBIS_WSQ_CODEC_H_
#define _NBIS_WSQ_CODEC_H_

typedef unsigned char byte;

#ifdef __cplusplus
extern "C" {
#endif

int encode_bitmap(byte *bitmap, int width, int height, byte **wsq, int *len);

int decode_wsq(byte *wsq, int len, byte **bitmap, int *width, int *height);

#ifdef __cplusplus
}
#endif

#endif
