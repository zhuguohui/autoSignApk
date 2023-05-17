package org.example.cmd;

import org.example.ui.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtil {

    public static boolean executive(String stmt, Log log) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();  //获取Runtime实例
        //执行命令

        String[] command = {"cmd", "/c", stmt};
        Process process = runtime.exec(command);
        // 标准输入流（必须写在 waitFor 之前）
        String inStr = consumeInputStream(process.getInputStream(), log, false);
        // 标准错误流（必须写在 waitFor 之前）
        String errStr = consumeInputStream(process.getErrorStream(), log, true); //若有错误信息则输出
        int proc = process.waitFor();
        if (proc == 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 27      * 消费inputstream，并返回
     * 28
     */
    private static String consumeInputStream(InputStream is, Log log, boolean error) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            if (error) {
                log.e(s);
            } else {
                log.i(s);
            }

            sb.append(s);
        }
        return sb.toString();
    }
}
