package com.miya;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

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
            List<HtmlAnchor> links = page.getByXPath("//a[contains(@href, '/train-stations/italy')]");

            // 打印所有找到的链接
            for (HtmlAnchor link : links) {
                System.out.println("URL: " + link.getHrefAttribute());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
