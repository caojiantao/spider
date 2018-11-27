package cn.caojiantao.api.tencent;

import cn.caojiantao.api.AssetQuery;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.caojiantao.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class QQMusicServiceImpl implements IQQMusicService {

    private static Pattern compile = Pattern.compile("^callback\\((.+?)\\)$");

    private final Tencent tencent;

    @Autowired
    public QQMusicServiceImpl(Tencent tencent) {
        this.tencent = tencent;
    }

    @Override
    public JSONObject getSongs(AssetQuery query) {
        try {
            String text = Jsoup.connect(tencent.getSongSearch())
                    .ignoreContentType(true)
                    .data("w", query.getKeyword())
                    .data("p", query.getPage().toString())
                    .data("n", query.getPagesize().toString())
                    .get()
                    .text();
            Matcher matcher = compile.matcher(text);
            if (matcher.find()) {
                return JSON.parseObject(matcher.group(1));
            }
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public JSONObject getSongPlay(String mid) {
        double guid = Math.round(2147483647 * Math.random()) * System.currentTimeMillis() % 1e10;
        JSONObject req0 = new JSONObject();
        req0.put("module", "vkey.GetVkeyServer");
        req0.put("method", "CgiGetVkey");
        JSONObject param = new JSONObject();
        param.put("guid", String.valueOf(guid));
        param.put("songmid", Collections.singletonList(mid));
        param.put("uin", "0");
        req0.put("param", param);

        JSONObject data = new JSONObject();
        data.put("req_0", req0);

        try {
            String text = Jsoup.connect(tencent.getSongPlay())
                    .ignoreContentType(true)
                    .data("data", data.toJSONString())
                    .get()
                    .text();
            return JSON.parseObject(text);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
