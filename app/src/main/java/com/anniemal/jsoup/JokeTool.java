package com.anniemal.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2015/8/17.
 */
public class JokeTool {

    public static String TITLE[] = null;
    public static String NUM[] = null;
    public static String TIME[] = null;
    public static String LINK[] = null;
    public static String TEXT[] = null;
    public static final String PATH_HOT = "http://www.jokeji.cn/list29_1.htm";
    public static final String PATH_BASE = "http://www.jokeji.cn";
    public static final String MAIN_CLASS = "list_title";
    public static final String DATESTRING_ID = "text110";
    Map<String, String[]> map;
    Map<Integer, String> cmap = new HashMap<Integer, String>();

    public Map<String, String[]> GetInfo(String path) {

        try {
            // ��ȡ�����ĵ�
            Document doc = Jsoup.connect(path).timeout(4000).get();
            // ��ȡ���� Class Ϊ list_title html����
            Element contents = doc.getElementsByClass(MAIN_CLASS).first();
            // ��ȡ����a��ǩ�Ĳ���
            Elements links = contents.getElementsByTag("a");
            // System.out.println(links);
            // ��ȡ����span��ǩ�Ĳ��֣��������
            Elements browses = contents.getElementsByTag("span");
            TITLE = new String[browses.toArray().length];
            TIME = new String[browses.toArray().length];
            NUM = new String[browses.toArray().length];
            LINK = new String[browses.toArray().length];
            TEXT = new String[browses.toArray().length];

            // ��ȡ����i��ǩ�Ĳ��֣�����ʱ�䣩
            Elements times = contents.getElementsByTag("i");
            // ������������
            if (contents.select("a").size() > 0) {

                int i = 0;
                for (Element link : links) {

                    String linkHref = link.attr("abs:href");

                    String linkText = link.text();
                    TITLE[i] = linkText;
                    LINK[i] = linkHref;

                    i++;
                }
            }
            // ������������
            for (int i = 0; i < browses.toArray().length; i++) {
                TIME[i] = times.get(i).text();
                NUM[i] = browses.get(i).text();
                String textString = null;
                String StringInfo = null;
                // ��ȡ�����ĵ�

                Document doc1 = Jsoup.connect(LINK[i]).timeout(4000).get();
                // ��ȡ���� Class Ϊ list_title html����
                Element contents1 = doc1.getElementById(DATESTRING_ID);
                textString = contents1 + "";
                StringInfo = textString;
                TEXT[i] = StringInfo;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ������Ҫ���ص�����
        map = new HashMap<String, String[]>();
        map.put("title", TITLE);
        map.put("num", NUM);
        map.put("time", TIME);
        map.put("text", TEXT);

        return map;
    }

    public Map<Integer, String> GetCatagrey(String path){

        try {

            Document doc = Jsoup.connect(path).timeout(6000).get();

           // Element contents = doc.getElementsByClass("joketype l_left").first();
            Element contents = doc.select("div[class=joketype l_left]").first();

            //Elements links = contents.getElementsByTag("a");
            Elements links = contents.select("a");

            // ������������
            if (contents.select("a").size() > 0) {

                int i = 0;
                for (Element link : links) {
                    String linkHref = link.attr("abs:href");
                    String linkText = link.text();
                    cmap.put(i,linkText+"$"+linkHref);
                    i++;
                }
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cmap;
    }

}
