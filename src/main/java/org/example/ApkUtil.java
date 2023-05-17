package org.example;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.IOException;

public class ApkUtil {
    public static ApkMeta  getPackInfo(String path) throws IOException {

            File file = new File(path);
            if (file.exists() && file.isFile()) {
                ApkFile apkFile = new ApkFile(file);
                ApkMeta apkMeta = apkFile.getApkMeta();
               /* System.out.println("应用名称 :" + apkMeta.getLabel());
                System.out.println("包名     :" + apkMeta.getPackageName());
                System.out.println("版本号   :" + apkMeta.getVersionName());
                System.out.println("图标     :" + apkMeta.getIcon());
                System.out.println("大小     :" + (double) (file.length() * 100 / 1024 / 1024) / 100 + " MB");*/
                return apkMeta;
                //注释：apk所有信息都在apkMeta类里面。可以输出整个apkMeta来查看跟多详情信息
            }

        return null;
    }
}
