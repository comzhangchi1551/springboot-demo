package com.miya;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.miya.common.utils.CJsonUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OmioCrawler {
    public static void main(String[] args) {

        List<String> urls = new ArrayList<>();

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

            // 使用 Jsoup 解析页面内容
            String pageAsXml = page.asXml();
            Document doc = Jsoup.parse(pageAsXml);

            // 提取标题为 "Train Stations in Europe" 和 "All Train Stations" 的部分
            Elements sections = doc.select("section");
            for (Element section : sections) {
                Element title = section.selectFirst("h2");
                if (title != null && (title.text().equals("Train Stations in Europe") || title.text().equals("All Train Stations"))) {
                    Elements links = section.select("a[href]");
                    for (Element link : links) {
                        String href = link.attr("href");
                        if (href.contains("/train-stations/italy")) { // 根据实际链接特征进行判断
                            urls.add(href);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("urls: " + CJsonUtils.toJson(urls));


    }
}
