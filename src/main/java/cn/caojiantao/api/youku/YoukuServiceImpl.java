package cn.caojiantao.api.youku;

import com.alibaba.fastjson.JSONObject;
import com.github.caojiantao.util.ExceptionUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caojiantao
 */
@Slf4j
@Service
public class YoukuServiceImpl implements IYouKuService {

    private final static String CKEY = "DIl58SLFxFNndSV1GFNnMQVYkx1PP5tKe1siZu/86PR1u/Wh1Ptd+WOZsHHWxysSfAOhNJpdVWsdVJNsfJ8Sxd8WKVvNfAS8aS8fAOzYARzPyPc3JvtnPHjTdKfESTdnuTW6ZPvk2pNDh4uFzotgdMEFkzQ5wZVXl2Pf1/Y6hLK0OnCNxBj3+nb0v72gZ6b0td+WOZsHHWxysSo/0y9D2K42SaB8Y/+aD2K42SaB8Y/+ahU+WOZsHcrxysooUeND";

    private final static String APPKEY = "24679788";

    private final static Pattern CNA_REGEX = Pattern.compile("cna=(.+?);");
    private final static Pattern M_H5_TK_REGEX = Pattern.compile("_m_h5_tk=(.+?);");
    private final static Pattern M_H5_TK_ENC_REGEX = Pattern.compile("_m_h5_tk_enc=(.+?);");

    private final static Pattern ID_REGEX = Pattern.compile("id_(.+?).html");

    private String cna;
    private Map<String, String> tkMap;

    /**
     * 初始化连接cookie
     */
    private void initCookie() {
        cna = getCna();
        tkMap = getTks();
    }

    public static void main(String[] args) {
        String url = "http://v.youku.com/v_show/id_XMzkwMzA2MjMzNg==.html?spm=..m_26664.5~5!2~5~5~5~5~5~5~A";
        Matcher matcher = ID_REGEX.matcher(url);
        if (matcher.find()) {
            String vid = matcher.group(1);
            System.out.println(vid);

            YoukuServiceImpl youkuService = new YoukuServiceImpl();
            JSONObject data = youkuService.getJSONDataByVid(vid);
            System.out.println(data);
        }
    }

    @Override
    public JSONObject getJSONDataByVid(String vid) {
        return getJSONDataByVid(vid, 0);
    }

    private JSONObject getJSONDataByVid(String vid, int retry) {
        // 重试超过三次直接返回null
        if (retry > 3) {
            return null;
        }
        if (Strings.isNullOrEmpty(cna) || CollectionUtils.isEmpty(tkMap)) {
            initCookie();
        }
        String ts = String.valueOf(System.currentTimeMillis());
        JSONObject dataObject = getDataParam(vid, ts, cna);
        String sign = getSignStr(ts, tkMap.get("token"), dataObject.toJSONString());

        try {
            String result = Jsoup.connect("http://acs.youku.com/h5/mtop.youku.play.ups.appinfo.get/1.1/")
                    .ignoreContentType(true)
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
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private String getCna() {
        String cna = "";
        try {
            Connection.Response response = Jsoup.connect("http://log.mmstat.com/eg.js")
                    .ignoreContentType(true)
                    .execute();
            String setCookies = response.header("Set-Cookie");
            Matcher matcher = CNA_REGEX.matcher(setCookies);
            if (matcher.find()) {
                cna = matcher.group(1);
            }
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return cna;
    }

    private Map<String, String> getTks() {
        Map<String, String> map = new HashMap<>(3);
        try {
            Connection.Response response = Jsoup.connect("http://acs.youku.com/h5/mtop.youku.play.ups.appinfo.get/1.1/")
                    .ignoreContentType(true)
                    .data("appKey", APPKEY)
                    .execute();
            String setCookies = response.header("Set-Cookie");
            Matcher tkMatcher = M_H5_TK_REGEX.matcher(setCookies);
            if (tkMatcher.find()) {
                map.put("tk", tkMatcher.group(1));
                map.put("token", map.get("tk").substring(0, 32));
            }
            Matcher tkEncMatcher = M_H5_TK_ENC_REGEX.matcher(setCookies);
            if (tkEncMatcher.find()) {
                map.put("tkEnc", tkEncMatcher.group(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
