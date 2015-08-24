package com.anniemal.jsoup;

import android.util.Log;

import com.anniemal.model.CosImageInfo;
import com.anniemal.model.ImageInfo;
import com.anniemal.model.JokeInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Andy on 2015/6/12
 */
public class JsoupTool {
    private static JsoupTool instance = null;
    public static final String MAIN_CLASS = "list_title";
    public static final String DATESTRING_ID = "text110";
    public static final String IMAGEURL = "text110";
    public static String TITLE[] = null;
    public static String NUM[] = null;
    public static String TIME[] = null;
    public static String LINK[] = null;
    public static String TEXT[] = null;
    public static String CTITLE[] = null;
    public static String CUPTIME[] = null;
    public static String CIMG[] = null;
    public static String CPATH[] = null;
    Map<String, String[]> map;

    private JsoupTool() {
        trustEveryone();
    }

    public static JsoupTool getInstance() {
        if (instance == null) {
            synchronized (JsoupTool.class) {
                instance = new JsoupTool();
            }
        }

        return instance;
    }

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ImageInfo> getAllImages(String pageUrl) {
        try {
            Document doc = Jsoup.connect(pageUrl)
                    .timeout(10000)
                    .post();

            String title = doc.title();
            System.out.println(title);

            Elements urls = doc.select("img[src$=.jpg]");
            List<ImageInfo> imgList = new ArrayList<ImageInfo>();
            ImageInfo imageInfo;
            for (Element url : urls) {
                imageInfo = new ImageInfo();
                imageInfo.setImgTitle(url.attr("title"));
                imageInfo.setImgUrl(url.attr("src"));
                imgList.add(imageInfo);
            }

            return imgList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CosImageInfo> getCoses(String iurl){

        try {
            Document doc = Jsoup.connect(iurl)
                    .timeout(10000)
                    .post();

            Element contents = doc.getElementsByClass("i-list").first();
            Elements licontents = doc.select("li[box]");
            Elements imgs = doc.select("img[height]");
            Elements ems = contents.getElementsByTag("em");
            Elements eas = ems.select("a");


            CTITLE = new String[ems.size()];
            CUPTIME = new String[ems.size()];
            CIMG = new String[ems.size()];
            CPATH = new String[ems.size()];

            for(int i=0;i<ems.size();i++){
                CTITLE[i] = eas.get(i).text();
                CPATH[i] = eas.attr("abs:href");
                CUPTIME[i] = ems.get(i).text();
                CIMG[i] = imgs.get(i).attr("src");
                Log.e("cos",CTITLE[i]+CPATH[i]+CUPTIME[i]+CIMG[i]);
            }

            List<CosImageInfo> cosimgList = new ArrayList<CosImageInfo>();
            CosImageInfo cosImageInfo;
            for (int i=0;i<CTITLE.length;i++){
                cosImageInfo = new CosImageInfo();
                cosImageInfo.setCosTitle(CTITLE[i]);
                cosImageInfo.setCosImage(CIMG[i]);
                cosImageInfo.setImagePath(CPATH[i]);
                cosImageInfo.setUpdateTime(CUPTIME[i]);
                cosimgList.add(cosImageInfo);
            }

            return cosimgList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<JokeInfo> getAllJokes(String jurl){

        try {

            Document doc = Jsoup.connect(jurl)
                    .timeout(10000)
                    .get();

            Element contents = doc.getElementsByClass(MAIN_CLASS).first();

            Elements links = contents.getElementsByTag("a");

            Elements browses = contents.getElementsByTag("span");
            Elements times = contents.getElementsByTag("i");
            TITLE = new String[browses.toArray().length];
            TIME = new String[browses.toArray().length];
            NUM = new String[browses.toArray().length];
            LINK = new String[browses.toArray().length];
            TEXT = new String[browses.toArray().length];


            for (int i = 0; i < browses.toArray().length; i++) {

                TITLE[i] = links.get(i).text();
                LINK[i] = links.get(i).attr("abs:href");
                TIME[i] = times.get(i).text();
                NUM[i] = browses.get(i).text();


//                Log.e("LINK",LINK[i]+".....i="+i);
//                Document doc1 = Jsoup.connect(LINK[i]).timeout(4000).get();
//
//                Element contents1 = doc1.getElementById(DATESTRING_ID);
//                textString = contents1 + "";
//                StringInfo = textString;
//                TEXT[i] = StringInfo;
            }

            List<JokeInfo> jokeList = new ArrayList<JokeInfo>();
            JokeInfo jokeInfo;
            for (int i=0;i<TITLE.length;i++){
                jokeInfo = new JokeInfo();
                jokeInfo.setTitle(TITLE[i]);
                jokeInfo.setNumber(NUM[i]);
                jokeInfo.setTime(TIME[i]);
                //jokeInfo.setText(TEXT[i]);
                jokeInfo.setGetUrl(LINK[i]);
                jokeList.add(jokeInfo);
            }
            return jokeList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getText(String turl){

        try {
            Document doc1 = Jsoup.connect(turl).timeout(4000).get();
            Element contents1 = doc1.getElementById(DATESTRING_ID);
            String textString = null;
            String StringInfo = null;
            textString = contents1 + "";
            StringInfo = textString.substring(19, textString.length() - 7);
            return StringInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
