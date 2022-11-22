package com.miya;


import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class HttpTest {



    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ArrayList<File> fileList = new ArrayList<>();
        getAllFile(new File("/Users/zhangchi/Desktop/mz7000-7500-6"), fileList);

        fileList.sort((a,b)->a.getName().compareTo(b.getName()));


        File json2 = new File("/Users/zhangchi/Desktop/解析结果6.txt");
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if (!file.exists() || !file.getName().endsWith("jpg")) {
                continue;
            }
            MultipartFile multipartFile = getMultipartFile(file);

            try {
                AtomicReference<String> doPost = new AtomicReference<>("error");
                CountDownLatch countDownLatch = new CountDownLatch(1);
                executorService.execute(() -> {
                    HttpTest test = new HttpTest();
                    try {
                        doPost.set(test.doPost("https://pd-test.leqee.com/hostile/live/picture/recognize/ocr", multipartFile));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    countDownLatch.countDown();
                });

                countDownLatch.await(30, TimeUnit.SECONDS);
                String doPostResult = doPost.get();
                String string = "error";
                if (!doPostResult.equals("error")){
                    string = JSON.parseObject(doPostResult, HttpModel.class).toString();
                }


                String fileName = file.getName();

                string = fileName + ": \r\n" + string + "\r\n"  + "\r\n";

                writeToFile(string, json2);
                System.out.println(fileName + "---> 调用成功！");
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            TimeUnit.SECONDS.sleep(1);
        }


    }


    private static void writeToFile(String str, File file) {


        FileWriter fw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, true); // true表示追加
            fw.write(str);//向文件中写内容
            fw.write("\r\n");//换行
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    public static void getAllFile(File fileInput, List<File> allFileList) {
        if (!fileInput.exists()) {
            return;
        }

        // 获取文件列表
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                // 递归处理文件夹
                // 如果不想统计子文件夹则可以将下一行注释掉
                getAllFile(file, allFileList);
            } else {
                // 如果是文件则将其加入到文件数组中
                allFileList.add(file);
            }

        }
    }



    /**
     *
     *form表单提交（带附件）
     *
     *
     */
    public  String doPost(String url, MultipartFile file) throws Exception {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
        String resultString ="";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("cookie", "JSESSIONID=3FD50AE31449D0BBA53A1443E62F3F4E");


            //传参
            multipartEntityBuilder.addTextBody("shopId", "2200818155742");

            //流文件入参
            multipartEntityBuilder.addBinaryBody("picture", file.getInputStream(), ContentType.create("multipart/form-data", Consts.UTF_8), file.getOriginalFilename());

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");

        } finally {
            if (response != null) {
                response.close();
            }
        }
        return resultString ;
    }


    // 第二种方式
    public static MultipartFile getMultipartFile(File pdfFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        return multipartFile;
    }


}
