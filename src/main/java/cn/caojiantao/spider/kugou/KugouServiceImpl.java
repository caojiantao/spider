package cn.caojiantao.spider.kugou;

import cn.caojiantao.spider.AssetQuery;
import cn.caojiantao.spider.media.MediaBeanContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.caojiantao.util.ExceptionUtils;
import com.github.caojiantao.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;

/**
 * @author caojiantao
 * @since 2018-01-12 21:24:41
 */
@Slf4j
@Service
public class KugouServiceImpl implements IKugouService, InitializingBean {

    private final Kugou kugou;

    @Autowired
    public KugouServiceImpl(Kugou kugou) {
        this.kugou = kugou;
    }

    @Override
    public JSONObject getSongList(AssetQuery query) {
        return getSongs(query, kugou.getSongSearch());
    }

    @Override
    public JSONObject getSongInfo(AssetQuery query) {
        try {
            String result = Jsoup.connect(kugou.getSongPlay())
                    .data("r", "play/getdata")
                    .data("hash", query.getHash())
                    .data("album_id", query.getAlbumId())
                    .get()
                    .text();
            return JSONObject.parseObject(result, JSONObject.class).getJSONObject("data");
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public JSONObject getMvList(AssetQuery query) {
        return getSongs(query, kugou.getMvSearch());
    }

    @Override
    public JSONObject getMvInfo(AssetQuery query) {
        JSONObject play = null;
        String hash = query.getHash();
        String key = DigestUtils.md5DigestAsHex((hash + kugou.getSalt()).getBytes());
        try {
            String result = Jsoup.connect(kugou.getMvPlay() + "/cmd=100&hash=" + hash + "&key=" + key + "&ext=mp4")
                    .get()
                    .text();
            play = JSONObject.parseObject(result, JSONObject.class);
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

    private JSONObject getSongs(AssetQuery query, String url) {
        try {
            String result = Jsoup.connect(url)
                    .data("keyword", query.getKeyword())
                    .data("page", String.valueOf(query.getPage()))
                    .data("pagesize", String.valueOf(query.getPagesize()))
                    .data("platform", "WebFilter")
                    .get()
                    .text();
            JSONObject object = JSONObject.parseObject(result, JSONObject.class).getJSONObject("data");
            return JSONUtils.toPageData(object.getJSONArray("lists"), object.getInteger("total"));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MediaBeanContext.register("kugou", this);
    }
}
