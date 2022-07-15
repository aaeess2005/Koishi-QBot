package io.github.aaeess2005.koishiqbot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ResourceUtil {
    public static InputStream getResourceAsStream(String path) throws FileNotFoundException {
        InputStream is=ResourceUtil.class.getClassLoader().getResourceAsStream(File.separator + path);
        if(is==null){
            throw new FileNotFoundException();
        }
        return is;
    }
    public static byte[] getResourceAsBytes(String path) throws IOException {
        try(InputStream is=getResourceAsStream(path)){
            return is.readAllBytes();
        }
    }
    public static ByteBuffer getResourceAsByteBuffer(String path) throws IOException {
        InputStream is=getResourceAsStream(path);
        ByteBuffer bb=ByteBuffer.allocate(is.available());
        bb.put(is.readAllBytes());
        is.close();
        return bb.flip();
    }
}
