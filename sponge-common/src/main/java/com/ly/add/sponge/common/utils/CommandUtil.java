package com.ly.add.sponge.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public class CommandUtil {
    public static void excute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                System.out.println(s);
            }
            br.close();
            process.waitFor();
            if (process.exitValue() == 0)
                System.out.println("运行成功！！ ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}