package com.safety51.customer;

import com.safety51.api.dto.obs.ObsBucket;
import com.safety51.api.service.IMinioService;
import com.safety51.customer.utils.Byte2InputStream;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;

/**
 * Created by liwenqiang 2020/5/11.
 */
@RestController
public class TestController{

    @Reference(protocol = "hessian",timeout = 360000)
    private IMinioService minioService;
    
    @RequestMapping("createBucket")
    public String createBucket(String name) {
        String bucket = minioService.createBucket(name);
        return bucket;
    }
    @RequestMapping("getAllBuckets")
    public List<ObsBucket> getAllBuckets() {
        List<ObsBucket> allBuckets = minioService.getAllBuckets();
        return allBuckets;
    }

    @RequestMapping("getBucket")
    public ObsBucket getBucket(String name) {
        ObsBucket bucket = minioService.getBucket(name);
        return bucket;
    }
    @RequestMapping("getObject")
    public void getObject(String bucketName, String objectName) {
        byte[] ins = minioService.getObject(bucketName, objectName);
        InputStream inputStream = Byte2InputStream.byte2InputStream(ins);
        try {
            writeToLocal("C:\\test\\yyy2.jpg",inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param input 输入流
     * @throws IOException IOException
     */
    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();

    }

    /**
     *
     * @param bucketName
     * @param fileName
     * @param objectName
     */
    @RequestMapping("putObject")
    public void putObject(String bucketName, String fileName, String objectName) {
        File file = new File(fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = Byte2InputStream.inputStream2byte(fileInputStream);
            minioService.putObject(bucketName, objectName, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("putObjectSize")
    public void putObjectSize(String bucketName, String fileName, String objectName) {
        try {
            minioService.putObject(bucketName, objectName, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("removeObject")
    public void removeObject(String bucketName, String objectName) {
        try {
            minioService.removeObject(bucketName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("getObjectURL")
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        String objectURL=null;
        try {
            objectURL = minioService.getObjectURL(bucketName, objectName, expires);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectURL;
    }
}
