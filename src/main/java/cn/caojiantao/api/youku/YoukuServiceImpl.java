package cn.caojiantao.api.youku;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author caojiantao
 */
@Service
public class YoukuServiceImpl implements IYouKuService {

    private final static String CKEY = "DIl58SLFxFNndSV1GFNnMQVYkx1PP5tKe1siZu/86PR1u/Wh1Ptd+WOZsHHWxysSfAOhNJpdVWsdVJNsfJ8Sxd8WKVvNfAS8aS8fAOzYARzPyPc3JvtnPHjTdKfESTdnuTW6ZPvk2pNDh4uFzotgdMEFkzQ5wZVXl2Pf1/Y6hLK0OnCNxBj3+nb0v72gZ6b0td+WOZsHHWxysSo/0y9D2K42SaB8Y/+aD2K42SaB8Y/+ahU+WOZsHcrxysooUeND";

    private final static String APPKEY = "24679788";

    private String cna;
    private Map<String, String> tkMap;

    /**
     * 初始化连接cookie
     */
    private void initCookie() {
        cna = getCna();
        tkMap = getTks();
    }

    @Override
    public JSONObject getJSONDataByVid(String vid) {
        return getJSONDataByVid(vid, 0);
    }

    private JSONObject getJSONDataByVid(String vid, int retry) {
        // 重试超过三次直接返回null
        if (retry > 3) return null;
        if (Strings.isNullOrEmpty(cna) || CollectionUtils.isEmpty(tkMap)) {
            initCookie();
        }
        String ts = String.valueOf(System.currentTimeMillis());
        JSONObject dataObject = getDataParam(vid, ts, cna);
        String sign = getSignStr(ts, tkMap.get("token"), dataObject.toJSONString());

        try {
            String result = Jsoup.connect("http://acs.youku.com/h5/mtop.youku.play.ups.appinfo.get/1.1/")
                    .data("appKey", APPKEY)
                    .data("t", ts)
                    .data("sign", sign)
                    .data("data", dataObject.toJSONString())
                    .header("Referer", "http://v.youku.com")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                    .cookie("referhost", "http%3A%2F%2Fso.youku.com")
                    .cookie("cna", cna)
                    .cookie("_m_h5_tk", tkMap.get("tk"))
                    .cookie("_m_h5_tk_enc", tkMap.get("tkEnc"))
                    .get()
                    .text();
            JSONObject object = JSONObject.parseObject(result);
            if (object != null && object.getJSONObject("data").getJSONObject("data").getJSONObject("error") != null) {
                initCookie();
                return getJSONDataByVid(vid, ++retry);
            }
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCna() {
        String cna = "";
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> entity = template.getForEntity("http://log.mmstat.com/eg.js", String.class);


//            HttpURLConnection connection = HttpParser.connect("http://log.mmstat.com/eg.js")
//                    .get();
//            connection.connect();
//            List<String> setCookies = WebUtils.getResponseHeaderByName(connection, "Set-Cookie");
//            if (!CollectionUtils.isEmpty(setCookies)) {
//                for (String setCookie : setCookies) {
//                    String[] arr = setCookie.split(";");
//                    if (ArrayUtils.isNotEmpty(arr)) {
//                        for (String anArr : arr) {
//                            String[] temps = anArr.split("=");
//                            if (ArrayUtils.isNotEmpty(temps)) {
//                                String key = temps[0];
//                                String value = temps[1];
//                                if ("cna".equalsIgnoreCase(key)) {
//                                    cna = value;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        return cna;
    }

    private Map<String, String> getTks() {
        Map<String, String> map = new HashMap<>(3);
//        try {
//            HttpURLConnection connection = HttpParser.connect("http://acs.youku.com/h5/mtop.youku.play.ups.appinfo.get/1.1/")
//                    .data("appKey", APPKEY)
//                    .get();
//            connection.connect();
//            List<String> setCookies = WebUtils.getResponseHeaderByName(connection, "Set-Cookie");
//            if (!CollectionUtils.isEmpty(setCookies)) {
//                for (String setCookie : setCookies) {
//                    String[] arr = setCookie.split(";");
//                    if (ArrayUtils.isNotEmpty(arr)) {
//                        for (String anArr : arr) {
//                            String[] temps = anArr.split("=");
//                            if (ArrayUtils.isNotEmpty(temps)) {
//                                String key = temps[0];
//                                String value = temps[1];
//                                if ("_m_h5_tk".equalsIgnoreCase(key)) {
//                                    map.put("tk", value);
//                                    map.put("token", value.substring(0, 32));
//                                } else if ("_m_h5_tk_enc".equalsIgnoreCase(key)) {
//                                    map.put("tkEnc", value);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return map;
    }

    private JSONObject getDataParam(String vid, String ts, String cna) {
        JSONObject data = new JSONObject(true);
        JSONObject stealParams = new JSONObject(true);
        stealParams.put("ccode", "0502");
        stealParams.put("client_ip", "192.168.1.1");
        stealParams.put("utid", cna);
        stealParams.put("client_ts", ts);
        stealParams.put("version", "0.5.46");
        stealParams.put("ckey", CKEY);
        JSONObject bizParams = new JSONObject(true);
        bizParams.put("vid", vid);
        bizParams.put("current_showid", "20542");
        JSONObject adParams = new JSONObject(true);
        adParams.put("site", 1);
        adParams.put("wintype", "interior");
        adParams.put("p", 1);
        adParams.put("fu", 0);
        adParams.put("vs", "1.0");
        adParams.put("rst", "mp4");
        adParams.put("dq", "hd2");
        adParams.put("os", "win");
        adParams.put("osv", "");
        adParams.put("d", 0);
        adParams.put("bt", "pc");
        adParams.put("aw", "w");
        adParams.put("needbf", 1);
        adParams.put("atm", "");
        data.put("steal_params", stealParams.toJSONString());
        data.put("biz_params", bizParams.toJSONString());
        data.put("ad_params", adParams.toJSONString());
        return data;
    }

    private String getSignStr(String ts, String token, String dataStr) {
        String signStr = "";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            engine.eval(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("script/youku.js")));
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                signStr = (String) invocable.invokeFunction("s", token + "&" + ts + "&" + APPKEY + "&" + dataStr);
            }
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return signStr;
    }
}
