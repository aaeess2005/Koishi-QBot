package io.github.aaeess2005.koishiqbot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static Logger logger= LoggerFactory.getLogger(FileUtil.class);
    public static File getFile(String path) {
        File file=new File(path);
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("Cannot create file: "+file.getAbsolutePath());
                throw new RuntimeException(e);
            }
        }
        return file;
    }
}
