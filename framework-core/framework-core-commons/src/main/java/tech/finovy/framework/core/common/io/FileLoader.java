package tech.finovy.framework.core.common.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileLoader.class);

    public static File load(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name can't be null");
        }
        String decodedPath = URLDecoder.decode(name, StandardCharsets.UTF_8);

        return getFileFromFileSystem(decodedPath);

    }

    private static File getFileFromFileSystem(String decodedPath) {

        // run with jar file and not package third lib into jar file, this.getClass().getClassLoader() will be null
        URL resourceUrl = FileLoader.class.getClassLoader().getResource("");
        String[] tryPaths;
        if (resourceUrl != null) {
            tryPaths = new String[]{
                    // first: project dir
                    resourceUrl.getPath() + decodedPath,
                    // second: system path
                    decodedPath
            };
        } else {
            tryPaths = new String[]{
                    decodedPath
            };
        }

        for (String tryPath : tryPaths) {
            File targetFile = new File(tryPath);
            if (targetFile.exists()) {
                return targetFile;
            }
        }

        return null;
    }

}
