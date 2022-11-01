package com.miya.common.utils.tracer;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 
/**
 * @description: 压缩工具类
 * @author: wujie
 * @create: 2020-08-28 09:49
 **/
@Slf4j
public class ZipUtils {
 
    /**
     * 文件夹压缩成zip
     *
     * @param zipFilePath
     * @param filePath
     */
    public static void zipFileChannel(String zipFilePath, String filePath) {
        File zipFile = new File(zipFilePath);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            File path = new File(filePath);
            File[] files = path.listFiles();
            FileChannel fileChannel;
            for (File file : files) {
                fileChannel = new FileInputStream(file).getChannel();
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                fileChannel.transferTo(0, file.length(), writableByteChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        zipFileChannel("D:\\Downloads\\tempFile\\a.zip", "D:\\Downloads\\tempFile\\test");
    }
}