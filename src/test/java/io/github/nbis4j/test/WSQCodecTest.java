package io.github.nbis4j.test;

import io.github.nbis4j.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WSQCodecTest {
    private static final Logger log = LoggerFactory.getLogger(WSQCodecTest.class);

    @BeforeAll
    private void init() {
        log.info("Tester instance initialized");
    }

    @Test
    @Order(1)
    public void encodeBitmap() throws IOException {
        String bmpDirPath = System.getProperty("bmpDir");
        if (bmpDirPath == null || bmpDirPath.trim().length() == 0) {
            log.info("Skipping, data source not provided by -DbmpDir=</path/to/fingerprint/directory>");
            return;
        }
        log.info("Reading fingerprints from {}", bmpDirPath);
        List<String> filePaths = listBMPFilePaths(bmpDirPath);
        log.info("Found fingerprints to encode {}", filePaths.size());
        int success = 0;
        int failed = 0;
        long t0 = System.currentTimeMillis();
        for (String filePath : filePaths) {
            success++;
            BufferedImage image = ImageIO.read(new File(filePath));
            if (image == null) {
                log.warn("Could not read image from {}", filePath);
                continue;
            }
            byte[] pixels = ((DataBufferByte) image.getData().getDataBuffer()).getData();
            try {
                byte[] wsq = WSQCodec.encode(new Bitmap(pixels, image.getWidth(), image.getHeight()));
                log.debug("Image {}, WSQ size {}", filePath, wsq.length);
            } catch (NBISException e) {
                failed++;
                log.error("Failed to encode {}, error code {}", filePath, e.errorCode);
            }
        }
        log.info("Bitmap encode done, total/success/failed {}/{}/{}, elapsed {} seconds", filePaths.size(),
                success, failed, String.format("%.2f", (System.currentTimeMillis() - t0) * 1f / 1000));
    }

    @Test
    @Order(2)
    public void decodeWSQ() throws IOException {
        String wsqDirPath = System.getProperty("wsqDir");
        if (wsqDirPath == null || wsqDirPath.trim().length() == 0) {
            log.info("Skipping, data source not provided by -DwsqDir=</path/to/fingerprint/directory>");
            return;
        }
        log.info("Reading fingerprints from {}", wsqDirPath);
        List<String> filePaths = listWSQFilePaths(wsqDirPath);
        log.info("Found fingerprints to encode {}", filePaths.size());
        int success = 0;
        int failed = 0;
        long t0 = System.currentTimeMillis();
        for (String filePath : filePaths) {
            success++;
            try {
                Bitmap bitmap = WSQCodec.decode(Files.readAllBytes(Paths.get(filePath)));
                log.debug("WSQ {}, Bitmap {}/{}/{}", filePath, bitmap.pixels.length, bitmap.width, bitmap.height);
            } catch (NBISException e) {
                failed++;
                log.error("Failed to decode {}, error code {}", filePath, e.errorCode);
            }
        }
        log.info("WSQ decode done, total/success/failed {}/{}/{}, elapsed {} seconds", filePaths.size(),
                success, failed, String.format("%.2f", (System.currentTimeMillis() - t0) * 1f / 1000));
    }

    List<String> listBMPFilePaths(String dirPath) throws IOException {
        List<String> filePaths = new ArrayList<>();
        Files.walk(Paths.get(dirPath))
                .filter(path -> path.toString().endsWith(".bmp"))
                .forEach(path -> filePaths.add(path.toString()));
        return filePaths;
    }

    List<String> listWSQFilePaths(String dirPath) throws IOException {
        List<String> filePaths = new ArrayList<>();
        Files.walk(Paths.get(dirPath))
                .filter(path -> path.toString().endsWith(".wsq"))
                .forEach(path -> filePaths.add(path.toString()));
        return filePaths;
    }
}
