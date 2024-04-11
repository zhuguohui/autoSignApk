package org.example.config;

import com.google.gson.Gson;

import java.io.*;
import java.util.List;

public class SignConfigFile implements Serializable {
    static Gson gson=new Gson();
    public static SignConfigFile create(String path) throws IOException{
        File file=new File(path);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        FileInputStream fis=new FileInputStream(file);
        int len=0;
        byte[] buffer=new byte[1024];
        while ((len=fis.read(buffer,0,1024))>=0){
            baos.write(buffer,0,len);
        }
        String str = baos.toString();
        baos.close();
        fis.close();
        SignConfigFile signConfigFile = gson.fromJson(str, SignConfigFile.class);
        signConfigFile.setFilePath(path);
        return signConfigFile;
    }

    String filePath;

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    /**
     * signToolsPath : D:\Android\SDKuild-tools\33.0.2\apksigner.bat
     * signConfigs : [{"appId":"com.wznews.news.app","path":"E:\\Desktop\\apk\\keysotres\\wzrb.jks","storePassword":"monsoon","keyAlias":"myandroidkey","aliasPassword":"monsoon"}]
     */

    private String signToolsPath;

    //对齐工具的路径地址;一般在SDK\build-tools\33.0.2(最新的sdkVersion)\zipalign.exe
    private String alignToolsPath;
    private List<SignConfigsBean> signConfigs;

    private String getSignToolsPath() {
        return signToolsPath;
    }
    private static final String ALIGN_CMD_TEMPLATE = "%s -p -f -v 4 %s %s";

    /**
     * 获取用来压缩的命令
     * @param inputApkPath
     * @return
     */
    public  String getAlignCmdStr(String inputApkPath,String outApkPath){
        if(alignToolsPath==null||alignToolsPath.length()==0){
            throw new RuntimeException("alignToolsPath 没有配置 无法对齐apk");
        }
        return String.format(ALIGN_CMD_TEMPLATE, alignToolsPath, inputApkPath, outApkPath);
    }




    public SignConfigsBean getConfigBeanByPackageName(String packageName) {
        if(packageName==null||packageName.isEmpty()){
            throw new RuntimeException("传入的包名为空");
        }
        if(signConfigs==null||signConfigs.size()==0){
            return null;
        }
        for(SignConfigsBean bean:signConfigs){
            if(packageName.equals(bean.getAppId())){
                bean.setSignToolsPath(getSignToolsPath());
                bean.setConfigFilePath(filePath);
                return bean;
            }
        }
        return null;
    }
}
