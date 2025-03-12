package xin.pwdkeeper.wechat.toolutil;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import xin.pwdkeeper.wechat.service.RedisService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @projectName:openapi
 * @author:
 * @createTime: 2019/04/24 14:55
 * @description:
 */
public class HttpUtils {

    public static String doGet(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
//        URI uri = null;
//        try {
//            URL url = new URL(url1);
////            uri = new URI("https",url.getUserInfo(),url.getHost(),url.getPort(),url.getPath(),url.getQuery(),null);
//            uri = new URI(url1);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String doGetTbOrderDetails(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        URI uri = null;
        try {
            URL urls = new URL(url);
            uri = new URI("https",urls.getUserInfo(),urls.getHost(),urls.getPort(),urls.getPath(),urls.getQuery(),null);
//            uri = new URI(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static String sendGet(String getUrl, Map<String, Object> paraMap) {
        if (paraMap == null) {
            paraMap = new HashMap<String, Object>();
        }
        paraMap = new TreeMap<String, Object>(paraMap);
        StringBuilder sb = new StringBuilder();
        paraMap.entrySet().stream().forEach(entry -> {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        });
        getUrl = getUrl.contains("?") ? getUrl : getUrl + "?";
        return doGet(getUrl + sb.toString());
    }
    public static String sendGetTbOrderDetails(String getUrl, Map<String, Object> paraMap) {
        if (paraMap == null) {
            paraMap = new HashMap<String, Object>();
        }
        paraMap = new TreeMap<String, Object>(paraMap);
        StringBuilder sb = new StringBuilder();
        paraMap.entrySet().stream().forEach(entry -> {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        });
        getUrl = getUrl.contains("?") ? getUrl : getUrl + "?";
        return doGetTbOrderDetails(getUrl + sb.toString());
    }

    /**
     * url编码
     * @param str
     * @return
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * url解码
     * @param str
     * @return
     */
    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
