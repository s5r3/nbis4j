# NBIS4j

NBIS4j is Java library targeted to wrap functionalities implemented in the NIST Biometric Image Software (NBIS) distribution which is developed by the National Institute of Standards and Technology (NIST) for the Federal Bureau of Investigation (FBI) and Department of Homeland Security (DHS). Please see https://www.nist.gov/services-resources/software/nist-biometric-image-software-nbis for details.

## Build

### Prerequisite
- NBIS installed, installation directory path must be exported as `NBIS_ROOT` in OS environment variables.
- GCC >= 4.8
- CMake >= 3.10
- Java >= 1.8
- Gradle >= 6.0

### How to build
```
$ gradle clean cmakeBuild build
$ gradle test -DdataDir=/path/to/fingerprints
```

## Usage
### NFIQ
```java
import io.github.nbis4j.ImageFormat;
import io.github.nbis4j.NFIQ;

int nfiqOfWSQ(byte[] data) {
    return NFIQ.compute(byte[] data, ImageFormat.WSQ);
}

int nfiqOfBMP(byte[] data) {
    return NFIQ.compute(byte[] data, ImageFormat.BMP);
}
```

### WSQCodec
```java
import io.github.nbis4j.Bitmap;
import io.github.nbis4j.WSQCodec;

byte[] encodeBitmap(byte[] pixels, int width, int height) {
    return WSQCodec.encode(new Bitmap(pixels, width, height));
}

Bitmap decodeWSQ(byte[] wsq) {
    return WSQCodec.decode(wsq);
}
```

## To do
- AN2K
- JP2
- NFSEG

## License
This project is licensed under the terms of the MIT license.
