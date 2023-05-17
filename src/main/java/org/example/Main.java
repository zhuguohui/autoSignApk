package org.example;

import net.dongliu.apk.parser.bean.ApkMeta;
import org.example.cmd.CmdUtil;
import org.example.config.SignConfigFile;
import org.example.config.SignConfigsBean;
import org.example.ui.FilePathFrame;
import org.example.ui.Log;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        FilePathFrame jFrame=new FilePathFrame();
        //从args中读取config;
        jFrame.setVisible(true);
        Log log=jFrame;
        String configPath=getConfigPathFromArgs(args);
        if(isEmpty(configPath)){
            log.e("没有配置 签名配置文件，无法使用");
            return;
        }
        SignConfigFile signConfigFile=null;
        try {
             signConfigFile=SignConfigFile.create(configPath);
        } catch (Exception e) {
            String action=   "读取配置文件失败:位置:"+configPath;
            showError(log,action,e);
        }
        if(signConfigFile==null){
            return;
        }
        log.i("拖入apk文件 自动签名");

        SignConfigFile finalSignConfigFile = signConfigFile;
        jFrame.setFilePathGetFunction(str->{
            doSign(log,str, finalSignConfigFile);
            return null;
        });

    }

    private static void doSign(Log log, String apkPath, SignConfigFile configFile) {
        ApkMeta apkInfo=null;
        try {
           apkInfo= ApkUtil.getPackInfo(apkPath);
        } catch (IOException e) {
           showError(log,"解析apk出错,位置:"+apkPath,e);
        }
        if(apkInfo==null){
            return;
        }
        log.i("应用名称:"+apkInfo.getName());
        log.i("应用包名:"+apkInfo.getPackageName());
        SignConfigsBean configsBean= configFile.getConfigBeanByPackageName(apkInfo.getPackageName());
        if(configsBean==null){
            log.e("没有对应包名的配置信息");
            return;
        }

        SignConfigsBean.CmdInfo cmdInfo = null;
        try{
            cmdInfo=configsBean.buildSignCommand(apkPath, "signed");
        }catch (Exception e){
            showError(log,"生成签名命令失败",e);
            return;
        }

        boolean executeSuccess=false;
        try {
            executeSuccess= CmdUtil.executive(cmdInfo.getCmd(),log);
        } catch (Exception e) {
            showError(log,"执行命令失败,命令为:"+cmdInfo.getCmd(),e);
        }
        if(executeSuccess){
            log.i("签名成功");
            log.i("文件保存在:"+cmdInfo.getOutPath());
        }


    }

    private static void showError(Log log,String action,Exception e){
        log.e(action);
        log.e("错误详情:");
        log.e(e.getMessage());
        e.printStackTrace();
    }

    private static boolean isEmpty(String info){
        return info==null||info.length()==0;
    }

    private static final String CONFIG_KEY="-configFilePath";
    private static String getConfigPathFromArgs(String[] args) {
        if(args==null||args.length==0){
            return null;
        }
        String configFilePath=null;
        for(int i=0;i<args.length;i++){
            if(CONFIG_KEY.equals(args[i])&&i<(args.length-1)){
                configFilePath=args[i+1];
            }
        }

        return configFilePath;
    }


}