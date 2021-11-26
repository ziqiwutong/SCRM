package com.scrm.service.util;

import java.io.*;

public class FileUtil {

    /**
     * 根据文件路径读取文件，byte[]
     */
    public static byte[] readFileByBytes(String filePath, boolean delete) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            short bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len1;
            while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len1);
            }
            byte[] out = bos.toByteArray();
            in.close();
            bos.close();
            if (delete && file.delete()) {
                return out;
            }
            return out;
        }
    }
}
