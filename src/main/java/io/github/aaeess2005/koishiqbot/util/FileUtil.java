package io.github.aaeess2005.koishiqbot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static File getFile(String path,boolean isDir) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if(!isDir) {
                    file.createNewFile();
                }else {
                    file.mkdir();
                }
            } catch (IOException e) {
                logger.error("Cannot create file/directory: " + file.getAbsolutePath());
                throw new RuntimeException(e);
            }
        }
        return file;
    }
}
