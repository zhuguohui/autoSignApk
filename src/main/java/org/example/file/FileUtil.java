package org.example.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUtil {

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static String getOutPath(String apkPath, String signSuffix) {
        int index = apkPath.lastIndexOf(".");
        return apkPath.substring(0, index) + "_" + signSuffix + ".apk";
    }


    public static String getOutPathInTemp(String apkPath, String signSuffix) {
        int index = apkPath.lastIndexOf(".");
        // 获取系统默认的临时文件路径

        Path tempDirPath = Paths.get(System.getProperty("java.io.tmpdir"));
        String str= apkPath.substring(0, index) + "_" + signSuffix + ".apk";
        str= str.substring(str.lastIndexOf("\\"));
        String outPath= tempDirPath + str;
        File file=new File(outPath);
        if(file.exists()){
            file.delete();
        }
        return outPath;
    }



    public static void doDeleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            //删除临时的对齐文件
            boolean delete = file.delete();
            if (!delete) {
                //可能文件被占用，隔断时间再去删除
                executor.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    doDeleteFile(path);
                });
            }
        }
    }
}
