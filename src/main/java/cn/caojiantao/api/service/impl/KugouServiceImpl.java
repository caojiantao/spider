package cn.caojiantao.api.service.impl;

import cn.caojiantao.api.configuration.Kugou;
import cn.caojiantao.api.service.IKugouService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caojiantao.common.http.HttpParser;
import com.caojiantao.common.util.JsonUtils;
import com.caojiantao.common.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author caojiantao
 * @since 2018-01-12 21:24:41
 */
@Slf4j
@Service
public class KugouServiceImpl implements IKugouService {

    private final Kugou kugou;

    @Autowired
    public KugouServiceImpl(Kugou kugou) {
        this.kugou = kugou;
    }

    @Override
    public JSONObject getSongs(String keyword, int page, int pagesize) {
        return listData(keyword, page, pagesize, kugou.getSongSearch());
    }

    @Override
    public JSONObject getSongPlay(String fileHash, String albumId) {
        JSONObject play = null;
        try (InputStream is = HttpParser.connect(kugou.getSongPlay())
                .data("r", "play/getdata")
                .data("hash", fileHash)
                .data("album_id", albumId)
                .get()
                .getInputStream()) {
            play = JSONObject.parseObject(StreamUtils.getStrFromStream(is), JSONObject.class);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return play;
    }

    @Override
    public JSONObject getMvs(String keyword, int page, int pagesize) {
        return listData(keyword, page, pagesize, kugou.getMvSearch());
    }

    @Override
    public JSONObject getMvPlay(String hash) {
        JSONObject play = null;
        String key = DigestUtils.md5DigestAsHex((hash + kugou.getSalt()).getBytes());
        try (InputStream is = HttpParser.connect(kugou.getMvPlay() + "/cmd=100&hash=" + hash + "&key=" + key + "&ext=mp4")
                .get()
                .getInputStream()) {
            play = JSONObject.parseObject(StreamUtils.getStrFromStream(is), JSONObject.class);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return play;
    }

    @Override
    public JSONObject getRankByType(String type) throws IOException {
        Document doc = Jsoup.connect("http://www.kugou.com/yy/rank/home/1-8888.html").get();
        Element div = doc.getElementById("rankWrap").getElementsByClass("pc_temp_songlist").first();
        Element ul = div.getElementsByTag("ul").first();
        Elements lis = ul.getElementsByTag("li");
        JSONArray result = new JSONArray();
        for (int i = 0; i < 20; i++) {
            Element li = lis.get(i);
            JSONObject object = new JSONObject();
            String href = li.getElementsByClass("pc_temp_songname").first().attr("href");
            object.put("href", href);
            int index = Integer.parseInt(li.getElementsByClass("pc_temp_num").text());
            object.put("index", index);
            object.put("length", li.getElementsByClass("pc_temp_tips_r").first().children().last().text());
            object.put("title", li.attr("title"));
            result.add(object);
        }
        result.forEach(System.out::println);
        return null;
    }

    private JSONObject listData(String keyword, int page, int pagesize, String url) {
        JSONArray songs = null;
        int total = 0;
        try (InputStream is = HttpParser.connect(url)
                .data("keyword", keyword)
                .data("page", String.valueOf(page))
                .data("pagesize", String.valueOf(pagesize))
                .data("platform", "WebFilter")
                .get()
                .getInputStream()) {
            JSONObject object = JSONObject.parseObject(StreamUtils.getStrFromStream(is), JSONObject.class);
            // 获取歌曲list
            songs = object.getJSONObject("data").getJSONArray("lists");
            total = object.getJSONObject("data").getInteger("total");
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return JsonUtils.toPageData(songs, total);
    }
}
