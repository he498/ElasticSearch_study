package com.ming.utils;

import com.ming.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ClassNmae: HtmlParseUtil
 * @Description:
 * @Author: Ming on 2022/9/6
 */

@Component
public class HtmlParseUtil {
//    public static void main(String[] args) throws Exception {
//        new HtmlParseUtil().parseJD("java").forEach(System.out::println);
//    }

    public List<Content> parseJD(String keyword) throws Exception {
        //获取请求  https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword="+keyword;
        //解析网页  document对象就是浏览器document对象
        Document document = Jsoup.parse(new URL(url),30000);
        Element element = document.getElementById("J_goodsList");
        //获取所有的li元素
        Elements elements = element.getElementsByTag("li");
        //获取元素中的内容
        ArrayList<Content> goodsList = new ArrayList<>();


        for (Element el : elements) {
            //source-data-lazy-img
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            Content content = new Content();
            content.setTitle(title);
            content.setPrice(price);
            content.setImg(img);
            goodsList.add(content);
        }
        return goodsList;
    }
}
