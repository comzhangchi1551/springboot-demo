package com.miya;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class OmioCrawler {
    public static void main(String[] args) {
        // 创建 WebClient 对象
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            // 设置 WebClient 选项
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            // 访问目标网页
            HtmlPage page = webClient.getPage("https://www.omio.co.uk/train-stations");

            // 等待 JavaScript 和 AJAX 执行完成
            webClient.waitForBackgroundJavaScript(10 * 1000); // 最多等待 10 秒

            // 查找包含意大利站点的链接
            List<HtmlAnchor> links = page.getByXPath("//a[contains(@href, 'www.omio.co.uk/train-stations/')]");

            // 打印所有找到的链接
            for (HtmlAnchor link : links) {
                System.out.println("URL: " + link.getHrefAttribute());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static List<String> t3 (Integer skip, Integer limit) {
        String urlListStr = FileUtil.readUtf8String("urlList.json");
        List<String> urlList = JSONUtil.toList(urlListStr, String.class);

        urlList = urlList.stream().skip(skip).limit(limit).collect(Collectors.toList());

        List<String> result = new ArrayList<>();
        for (String url : urlList) {
            try {
                String html = getHtml(url);
                writeHtml(html, url);
            } catch (Exception e) {
                log.error("url = [{}]", url, e);
                result.add(url);
            }
        }

        System.out.println("JSONUtil.toJsonStr(result) = " + JSONUtil.toJsonStr(result));

        List<String> result2 = new ArrayList<>();
        for (String url : result) {
            try {
                String html = getHtml(url);
                writeHtml(html, url);
            } catch (Exception e) {
                log.error("url = [{}]", url, e);
                result2.add(url);
            }
        }

        System.out.println("JSONUtil.toJsonStr(result2) = " + JSONUtil.toJsonStr(result2));

        List<String> result3 = new ArrayList<>();
        for (String url : result2) {
            try {
                String html = getHtml(url);
                writeHtml(html, url);
            } catch (Exception e) {
                log.error("url = [{}]", url, e);
                result3.add(url);
            }
        }

        System.out.println("JSONUtil.toJsonStr(result3) = " + JSONUtil.toJsonStr(result3));
        List<String> result4 = new ArrayList<>();
        for (String url : result3) {
            try {
                String html = getHtml(url);
                writeHtml(html, url);
            } catch (Exception e) {
                log.error("url = [{}]", url, e);
                result4.add(url);
            }
        }

        System.out.println("JSONUtil.toJsonStr(result4) = " + JSONUtil.toJsonStr(result4));

        return result;

    }

    private static void writeHtml(String html, String url) {
        String name = getUniqueName(url);
        File file = new File("html/" + name + ".html");
        FileUtil.writeUtf8String(html, file);
    }

    private static String getUniqueName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    @SneakyThrows
    public static String getHtml(String url) {

        //设置驱动
        System.setProperty("webdriver.edge.driver","/Users/zhangchi/Downloads/edgedriver_mac64_m1/msedgedriver");
        //创建驱动
        EdgeOptions options = new EdgeOptions();
        options.setCapability("headless", true);
        EdgeDriver driver = new EdgeDriver(options);

        try {
            //与将要爬取的网站建立连接
            driver.get(url);

            //滚动前先睡眠一会
//            TimeUnit.SECONDS.sleep(3);
            //设置滚动条移动到最下面
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
//            TimeUnit.SECONDS.sleep(1);

            String pageSource = driver.getPageSource();
            return pageSource;
        } finally {
            driver.close();
        }
    }



    public static void t1(String memberKey) {

        // 连接到Redis服务器，这里假设Redis运行在本地机器上，端口为6379
        Jedis jedis = new Jedis("192.168.202.103", 6379);

        String redisKey = "trn:geo:cache:zset:searchByKeyword";

        Double zscore = jedis.zscore(redisKey, memberKey);
        if (zscore == null) {
            jedis.zadd(redisKey, 1l, memberKey);
        } else {
            jedis.zincrby(redisKey,  1, memberKey);
            jedis.zremrangeByRank(redisKey, 10, -1);
        }
    }
}
