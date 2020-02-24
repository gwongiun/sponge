package com.ly.add.sponge.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : qqy48861
 * date : 2019/5/23.
 */
public class AutoDeploy {
    public static void main(String[] args) {
        String spongeVersion = "1.6.13";

        String oldMainPath = "D:\\MyConfiguration\\qqy48861\\.m2\\repository\\com\\ly\\add\\";
        String versionPath = "\\" + spongeVersion + "\\";
        String newMainPath = "D:\\";

        String sponge = "sponge";
        String spongeCommon = "sponge-common";
        String spongeTcel = "sponge-tcel";
        String spongeSsm = "sponge-ssm";
        String spongeEs = "sponge-es";

        String jar = "jar";
        String pom = "pom";

        String spongePomFileName = sponge + "-" + spongeVersion + ".pom";
        String spongeCommonPomFileName = spongeCommon + "-" + spongeVersion + ".pom";
        String spongeCommonJarFileName = spongeCommon + "-" + spongeVersion + ".jar";
        String spongeTcelPomFileName = spongeTcel + "-" + spongeVersion + ".pom";
        String spongeTcelJarFileName = spongeTcel + "-" + spongeVersion + ".jar";
        String spongeSsmPomFileName = spongeSsm + "-" + spongeVersion + ".pom";
        String spongeSsmJarFileName = spongeSsm + "-" + spongeVersion + ".jar";
        String spongeEsPomFileName = spongeEs + "-" + spongeVersion + ".pom";
        String spongeEsJarFileName = spongeEs + "-" + spongeVersion + ".jar";

        String newSpongePomFilePath = newMainPath + spongePomFileName;
        String newSpongeCommonPomFilePath = newMainPath + spongeCommonPomFileName;
        String newSpongeCommonJarFilePath = newMainPath + spongeCommonJarFileName;
        String newSpongeTcelPomFilePath = newMainPath + spongeTcelPomFileName;
        String newSpongeTcelJarFilePath = newMainPath + spongeTcelJarFileName;
        String newSpongeSsmPomFilePath = newMainPath + spongeSsmPomFileName;
        String newSpongeSsmJarFilePath = newMainPath + spongeSsmJarFileName;
        String newSpongeEsPomFilePath = newMainPath + spongeEsPomFileName;
        String newSpongeEsJarFilePath = newMainPath + spongeEsJarFileName;

        Map<String, String> map = new HashMap<>();
        map.put(oldMainPath + sponge + versionPath + spongePomFileName, newSpongePomFilePath);
        map.put(oldMainPath + spongeCommon + versionPath + spongeCommonPomFileName, newSpongeCommonPomFilePath);
        map.put(oldMainPath + spongeCommon + versionPath + spongeCommonJarFileName, newSpongeCommonJarFilePath);
        map.put(oldMainPath + spongeTcel + versionPath + spongeTcelPomFileName, newSpongeTcelPomFilePath);
        map.put(oldMainPath + spongeTcel + versionPath + spongeTcelJarFileName, newSpongeTcelJarFilePath);
        map.put(oldMainPath + spongeSsm + versionPath + spongeSsmPomFileName, newSpongeSsmPomFilePath);
        map.put(oldMainPath + spongeSsm + versionPath + spongeSsmJarFileName, newSpongeSsmJarFilePath);
        map.put(oldMainPath + spongeEs + versionPath + spongeEsPomFileName, newSpongeEsPomFilePath);
        map.put(oldMainPath + spongeEs + versionPath + spongeEsJarFileName, newSpongeEsJarFilePath);
        batchCopy(map);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String deploySpongeCommand = commandTemp(sponge, spongeVersion, pom, newSpongePomFilePath, newSpongePomFilePath);
        String deploySpongeCommonCommand = commandTemp(spongeCommon, spongeVersion, jar, newSpongeCommonPomFilePath, newSpongeCommonJarFilePath);
        String deploySpongeTcelCommand = commandTemp(spongeTcel, spongeVersion, jar, newSpongeTcelPomFilePath, newSpongeTcelJarFilePath);
        String deploySpongeSsmCommand = commandTemp(spongeSsm, spongeVersion, jar, newSpongeSsmPomFilePath, newSpongeSsmJarFilePath);
        String deploySpongeEsCommand = commandTemp(spongeEs, spongeVersion, jar, newSpongeEsPomFilePath, newSpongeEsJarFilePath);
        CommandUtil.excute(deploySpongeCommand);
        CommandUtil.excute(deploySpongeCommonCommand);
        CommandUtil.excute(deploySpongeTcelCommand);
        CommandUtil.excute(deploySpongeSsmCommand);
        CommandUtil.excute(deploySpongeEsCommand);
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new File(newSpongePomFilePath).delete();
        new File(newSpongeCommonJarFilePath).delete();
        new File(newSpongeTcelJarFilePath).delete();
        new File(newSpongeSsmJarFilePath).delete();
        new File(newSpongeEsJarFilePath).delete();
        new File(newSpongeCommonPomFilePath).delete();
        new File(newSpongeTcelPomFilePath).delete();
        new File(newSpongeSsmPomFilePath).delete();
        new File(newSpongeEsPomFilePath).delete();
    }

    private static void batchCopy(Map<String, String> map) {
        for (Map.Entry entry : map.entrySet()) {
            File oldf = new File((String) entry.getKey());
            File newf = new File((String) entry.getValue());
            try {
                FileUtils.copyFile(oldf, newf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String commandTemp(String artifactId, String version, String packaging, String pomFilePath, String jarFilePath) {
        return "cmd /c D:\\Program\\Java\\maven-3.3.9\\bin\\mvn deploy:deploy-file -DgroupId=com.ly.add -DartifactId=" + artifactId + " -Dversion=" + version + " -Dpackaging=" + packaging + " -DpomFile=" + pomFilePath + " -Dfile=" + jarFilePath + " -Durl=http://nexus.17usoft.com/repository/mvn-tcwireless-salm-release/ -DrepositoryId=salm";
    }
}