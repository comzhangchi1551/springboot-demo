package com.miya;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.excel.EasyExcel;
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
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class HttpTest2 {

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.INFO);
        });
    }



    public static void main(String[] args) throws Exception {
        List<ExcelModel> excelModelList = extracted(
                "/Users/zhangchi/Desktop/仲裁材料/工作交付/24-25工作内容/picture-5",
                "/Users/zhangchi/Desktop/仲裁材料/工作交付/24-25工作内容/文本结果/解析结果5.txt",
                "JSESSIONID=C9976C84C25FBBDA58FA2F69FDB73B4B"
        );
        simpleWrite("/Users/zhangchi/Desktop/仲裁材料/工作交付/24-25工作内容/excel结果/excel5.xlsx", excelModelList);
    }


    public static void simpleWrite(String excelFileName, List<ExcelModel> data) {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = excelFileName;

        EasyExcel.write(fileName, ExcelModel.class).useDefaultStyle(false).sheet("模板").doWrite(data);

    }




    private static List<ExcelModel> extracted(String pathName, String jsonFileName, String cookie) throws IOException, InterruptedException {
        List<ExcelModel> result = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        ArrayList<File> fileList = new ArrayList<>();
        getAllFile(new File(pathName), fileList);

        fileList.sort((a,b)->a.getName().compareTo(b.getName()));


        File json2 = new File(jsonFileName);
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if (!file.exists() || !file.getName().endsWith("jpg")) {
                continue;
            }
            String fileName = file.getName();

            MultipartFile multipartFile = getMultipartFile(file);

            HttpModel httpModel = null;
            try {
                AtomicReference<String> doPost = new AtomicReference<>("error");
                CountDownLatch countDownLatch = new CountDownLatch(1);
                executorService.execute(() -> {
                    HttpTest2 test = new HttpTest2();
                    try {
                        doPost.set(test.doPost("https://pd-test.leqee.com/hostile/live/picture/recognize/ocr", cookie, multipartFile));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    countDownLatch.countDown();
                });

                countDownLatch.await(30, TimeUnit.SECONDS);
                String doPostResult = doPost.get();

                httpModel = JSON.parseObject(doPostResult, HttpModel.class);

                String string = httpModel == null ? "" : httpModel.toString();


                string = fileName + ": \r\n" + string + "\r\n"  + "\r\n";

                writeToFile(string, json2);
                System.out.println(fileName + "---> 调用成功！");
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            } finally {
                result.add(HttpModel.toExcelModel(httpModel, fileName));
            }

            TimeUnit.SECONDS.sleep(1);
        }

        executorService.shutdown();

        return result;

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
    public  String doPost(String url, String cookie, MultipartFile file) throws Exception {
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
            httpPost.setHeader("cookie", cookie);


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
