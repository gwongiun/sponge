package com.ly.add.sponge.tcel.hdfs;

import com.google.common.collect.Lists;
import com.ly.add.sponge.common.utils.ExceptionUtil;
import com.ly.add.sponge.tcel.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author : qqy48861
 * date : 2018/5/4.
 */
public class HdfsClient {

    private Configuration config;

    private FileSystem fileSystem;

    public HdfsClient(Configuration config) {
        this.config = config;
        this.config.addResource("core-site.xml");
        this.config.addResource("hdfs-site.xml");
    }

    /**
     * 用map初始化config
     *
     * @param config
     */
    public HdfsClient(Map<String, String> config) {
        this.config = new Configuration();
        System.setProperty("HADOOP_USER_NAME", "bidcadmin");
        this.config.addResource("core-site.xml");
        this.config.addResource("hdfs-site.xml");

        for (Map.Entry<String, String> e : config.entrySet()) {
            this.config.set(e.getKey(), e.getValue());
        }

    }

    /**
     * 读取相对path下的文件或目录数量
     *
     * @param path hdfspath相对目录
     * @return
     */
    public int fileCounts(String path) {
        return fileCounts(path, false, null);
    }

    /**
     * 读取相对path下的文件或目录数量
     *
     * @param path       hdfspath相对目录
     * @param onlyFile   是否只要统计文件，排除目录
     * @param fileSuffix 文件后缀
     * @return
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    public int fileCounts(String path, boolean onlyFile, String fileSuffix) {
        int count = 0;
        try {
            this.fileSystem = FileSystem.get(config);
            RemoteIterator<LocatedFileStatus> it = fileSystem.listFiles(new Path(path), false);
            while (it.hasNext()) {
                LocatedFileStatus status = it.next();
                if (onlyFile && status.isDirectory()) {
                    continue;
                }
                if (StringUtils.isNotEmpty(fileSuffix) && !status.getPath().toString().endsWith(fileSuffix)) {
                    continue;
                }
                count++;
            }
        } catch (Exception e) {
            LogFactory.start().withMarker("HdfsClient", "fileCounts").withFilter(path, null)
                    .withMessage(
                            "path=" + path + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                    .withException(e).error();
            throw new RuntimeException(e);
        } finally {
            try {
                fileSystem.close();
            } catch (IOException e) {
                LogFactory.start().withMarker("HdfsClient", "fileCounts()[fileSystem.close()]").withFilter(path, null)
                        .withMessage(
                                "path=" + path + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                        .withException(e).error();
                throw new RuntimeException(e);
            }
        }
        return count;
    }

    /**
     * 把path下的所有文件下载到临时目录 并返回本地文件列表
     *
     * @param path
     * @param fileSuffix
     * @param isCompress
     */
    public List<String> download(String path, String fileSuffix, boolean isCompress) {
        List<String> pathList = Lists.newArrayList();
        try {
            this.fileSystem = FileSystem.get(config);
            RemoteIterator<LocatedFileStatus> it = fileSystem.listFiles(new Path(path), false);
            while (it.hasNext()) {
                LocatedFileStatus status = it.next();
                if (status.isDirectory()) {
                    continue;
                }
                if (StringUtils.isNotEmpty(fileSuffix) && !status.getPath().toString().endsWith(fileSuffix)) {
                    continue;
                }
                // download file to temp path
                String tmpPath = "";
                if (isCompress) {
                    tmpPath = downloadFileAndCompress(status.getPath().toString());
                } else {
                    tmpPath = downloadFile(status.getPath().toString());
                }
                pathList.add(tmpPath);
                LogFactory.start().withMarker("HdfsClient", "download").withFilter(path, status.getPath().toString())
                        .withMessage("下载完成一个文件：src=" + status.getPath().toString() + ",dest=" + tmpPath).info();
            }
        } catch (Exception e) {
            LogFactory.start().withMarker("HdfsClient", "download").withFilter(path, null)
                    .withMessage(
                            "path=" + path + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                    .withException(e).error();
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (IOException e) {
                LogFactory.start().withMarker("HdfsClient", "fileCounts()[fileSystem.close()]").withFilter(path, null)
                        .withMessage(
                                "path=" + path + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                        .withException(e).error();
                throw new RuntimeException(e);
            }
        }
        return pathList;
    }

    /**
     * 下载单个文件，返回文件临时存放本地路径
     *
     * @param src
     * @return
     */
    private String downloadFile(String src) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = fileSystem.open(new Path(src));
            String fileName = src.substring(src.lastIndexOf("/") + 1);
            String preffix = "";
            String suffix = "";
            if (fileName.indexOf(".") == -1) {// 无后缀 保存为txt文件
                suffix = ".txt";
                preffix = fileName;
            } else {
                preffix = fileName.split("\\.")[0];
                suffix = "." + fileName.split("\\.")[1];
            }
            File tmpFile = File.createTempFile(preffix, suffix);
            out = new FileOutputStream(tmpFile);
            IOUtils.copyBytes(in, out, 4096, false);
            return tmpFile.getPath();
        } catch (Exception e) {
            LogFactory.start().withMarker("HdfsClient", "downloadFile").withFilter(src, null)
                    .withMessage("src=" + src + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                    .withException(e).error();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogFactory.start().withMarker("HdfsClient", "downloadFile()[io.close()]").withFilter(src, null)
                        .withMessage(
                                "src=" + src + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                        .withException(e).error();
            }
        }
        return null;
    }

    /**
     * 下载文件临时存放本地路径 并打成zip包
     *
     * @param src
     * @return
     */
    private String downloadFileAndCompress(String src) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = fileSystem.open(new Path(src));
            String fileName = src.substring(src.lastIndexOf("/") + 1);
            String preffix = "";
            String suffix = "";
            if (fileName.indexOf(".") == -1) {// 无后缀 保存为txt文件
                suffix = "";
                preffix = fileName;
            } else {
                preffix = fileName.split("\\.")[0];
                suffix = "." + fileName.split("\\.")[1];
            }
            File tmpFile = File.createTempFile(preffix, suffix);
            out = new FileOutputStream(tmpFile);
            IOUtils.copyBytes(in, out, 4096, false);
            List<File> srcFile = Lists.newArrayListWithExpectedSize(1);
            srcFile.add(tmpFile);
            String zipPath = tmpFile.getPath() + ".zip";
//                ZipUtil.zipFile(zipPath, srcFile);
            LogFactory.start().withMarker("HdfsClient", "downloadFileAndCompress").withFilter(src, null)
                    .withMessage("压缩一个文件完成：src=" + src + ",config=" + config + ",srcsize=" + tmpFile.length()).info();
            tmpFile.delete();// 删除原始文件
            return zipPath;
        } catch (Exception e) {
            LogFactory.start().withMarker("HdfsClient", "downloadFileAndCompress").withFilter(src, null)
                    .withMessage("src=" + src + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                    .withException(e).error();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogFactory.start().withMarker("HdfsClient", "downloadFile()[io.close()]").withFilter(src, null)
                        .withMessage(
                                "src=" + src + ",config=" + config + "," + ExceptionUtil.getErrorInfoFromException(e))
                        .withException(e).error();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.set("fs.defaultFS", "hdfs://hadoopcluster");
        config.set("dfs.nameservices", "hadoopcluster");
        config.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        config.set("dfs.ha.namenodes.hadoopcluster", "nn1, nn2");
        config.set("dfs.namenode.rpc-address.hadoopcluster.nn1", "spmaster.bigdata.ly:8020");
        config.set("dfs.namenode.rpc-address.hadoopcluster.nn2", "spslave1.bigdata.ly:8020");
        config.set("dfs.namenode.http-address.hadoopcluster.nn1", "spmaster.bigdata.ly:50070");
        config.set("dfs.namenode.http-address.hadoopcluster.nn2", "spslave1.bigdata.ly:50070");
        config.set("dfs.client.failover.proxy.provider.hadoopcluster",
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        HdfsClient client = new HdfsClient(config);
        List<String> list = client.download("/tmp", ".zip", true);
        for (String s : list) {
            System.out.println(s);
        }

    }


}