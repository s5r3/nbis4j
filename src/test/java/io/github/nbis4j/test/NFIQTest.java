package io.github.nbis4j.test;

import io.github.nbis4j.Bitmap;
import io.github.nbis4j.ImageFormat;
import io.github.nbis4j.NFIQ;
import io.github.nbis4j.WSQCodec;
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
public class NFIQTest {
    private static final Logger log = LoggerFactory.getLogger(NFIQTest.class);

    @BeforeAll
    private void init() {
        log.info("Tester instance initialized");
    }

    @Test
    @Order(1)
    public void computeNFIQ() throws IOException {
        String nfiqDirPath = System.getProperty("nfiqDir");
        if (nfiqDirPath == null || nfiqDirPath.trim().length() == 0) {
            log.info("Skipping, data source not provided by -DnfiqDir=</path/to/fingerprint/directory>");
            return;
        }
        log.info("Reading fingerprints from {}", nfiqDirPath);
        List<String> filePaths = listFilePaths(nfiqDirPath);
        log.info("Found fingerprints to compute NFIQ {}", filePaths.size());
        int count = 0;
        long t0 = System.currentTimeMillis();
        for (String filePath : filePaths) {
            String formatString = filePath.substring(filePath.length() - 3);
            int nfiq = NFIQ.compute(Files.readAllBytes(Paths.get(filePath)), ImageFormat.valueOf(formatString.toUpperCase()));
            log.info("{}: {}", filePath, nfiq);
            assert nfiq >= 1 && nfiq <= 5;
        }
        log.info("NFIQ computation done, elapsed {} seconds", String.format("%.2f", (System.currentTimeMillis() - t0) * 1f / 1000));
    }

    List<String> listFilePaths(String dirPath) throws IOException {
        List<String> filePaths = new ArrayList<>();
        Files.walk(Paths.get(dirPath))
                .filter(path -> path.toString().endsWith(".bmp") || path.toString().endsWith(".wsq"))
                .forEach(path -> filePaths.add(path.toString()));
        return filePaths;
    }
}
