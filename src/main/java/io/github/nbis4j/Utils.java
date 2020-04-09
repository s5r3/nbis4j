package io.github.nbis4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Utils {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final Path LIBRARY_PATH = Paths.get(System.getProperty("user.home"), ".nbis4j", version());

    static {
        try {
            Files.createDirectories(LIBRARY_PATH);
        } catch (IOException ignored) {
        }
        System.setProperty("jna.library.path", LIBRARY_PATH.toString());
    }

    public static void extractNativeLibrary(String libraryName) throws IOException {
        String libraryFileName = "lib" + libraryName;
        if (isLinux()) {
            libraryFileName += ".so";
        } else if (isWindows()) {
            libraryFileName +=   ".dll";
        } else {
            throw new IllegalStateException("Only Windows and Linux operating systems are supported");
        }

        String sourcePath = "/native/" + libraryFileName;
        try (InputStream is = Utils.class.getResourceAsStream(sourcePath)) {
            Files.copy(is, Paths.get(LIBRARY_PATH.toString(), libraryFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (NullPointerException e) {
            throw new FileNotFoundException("File " + sourcePath + " was not found inside jar");
        }
    }

    public static boolean isWindows() {
        return OS.startsWith("windows");
    }

    public static boolean isLinux() {
        return OS.startsWith("linux");
    }

    public static String version() {
        String version = Utils.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "0.0";
        }
        return version;
    }
}
