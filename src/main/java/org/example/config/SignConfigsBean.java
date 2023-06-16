package org.example.config;


import java.io.File;
import java.io.Serializable;

public class SignConfigsBean implements Serializable {
    /**
     * appId : com.wznews.news.app
     * path : E:\Desktop\apk\keysotres\wzrb.jks
     * storePassword : monsoon
     * keyAlias : myandroidkey
     * aliasPassword : monsoon
     */

    private String appId;
    private String storePath;
    private String storePassword;
    private String keyAlias;
    private String aliasPassword;
    private String signToolsPath;

    public String getAppId() {
        return appId;
    }

    void setSignToolsPath(String signToolsPath) {
        this.signToolsPath = signToolsPath;
    }

    /**
     * D:\Android\SDK\build-tools\33.0.2\apksigner.bat sign --ks E:\Desktop\apk\keysotres\wzrb.jks  --ks-pass  pass:xxx   --ks-key-alias aaa  --key-pass  pass:xxx --out E:\Desktop\apk\wd_signed.apk E:\Desktop\apk\wd.apk
     */

    private static final String CMD_TEMPLATE = "%s sign --v3-signing-enabled false --v4-signing-enabled false --ks %s --ks-pass pass:%s --ks-key-alias %s --key-pass pass:%s --out %s %s";

    /**
     * 生成签名的命令
     *
     * @param apkPath    应用的地址
     * @param signSuffix 签名以后的后缀，比如后缀是xxx。原来的apk名称为。a.apk,签名以后
     *                   在原来的位置生成的apk的名字为a_xxx.apk
     * @return 生成的签名命令
     */
    public CmdInfo buildSignCommand(String apkPath, String signSuffix) {

        String outPath = getOutPath(apkPath, signSuffix);
        String cmd = String.format(CMD_TEMPLATE,
                signToolsPath,
                storePath,
                storePassword,
                keyAlias,
                aliasPassword,
                outPath,
                apkPath);
        return new CmdInfo(cmd, outPath);
    }

    public static class CmdInfo {
        String cmd;
        String outPath;

        public CmdInfo(String cmd, String outPath) {
            this.cmd = cmd;
            this.outPath = outPath;
        }

        public String getCmd() {
            return cmd;
        }

        public String getOutPath() {
            return outPath;
        }
    }

    private static String getOutPath(String apkPath, String signSuffix) {
        int index = apkPath.lastIndexOf(".");
        return apkPath.substring(0, index) + "_" + signSuffix + ".apk";
    }


}
