package cn.caojiantao.spider.education;

import cn.caojiantao.spider.dto.VideoCategoryDTO;
import cn.caojiantao.spider.dto.VideoDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caojiantao
 */
@Service("n3t3")
public class N3t3ServiceImpl implements IEducationService {

    private static Pattern pattern = Pattern.compile("var player_data=(.*)");

    private static String host = "http://n3t3.com";

    @Override
    public List<VideoCategoryDTO> getVideoCategoryList() throws Exception {
        List<VideoCategoryDTO> videoCategoryDTOList = new ArrayList<>();
        Document document = Jsoup.connect(host).get();
        Elements elements = document.select(".nav_menu li a");
        elements.forEach(element -> {
            String href = element.attr("href");
            if (href.startsWith("/index.php/vod")) {
                String name = element.text();
                VideoCategoryDTO videoCategoryDTO = VideoCategoryDTO.builder().name(name).link(href).build();
                videoCategoryDTOList.add(videoCategoryDTO);
            }
        });
        return videoCategoryDTOList;
    }

    @Override
    public List<VideoDTO> getVideoList(String category, int page) throws Exception {
        List<VideoDTO> videoDTOList = new ArrayList<>();
        int i = category.indexOf(".html");
        StringBuilder builder = new StringBuilder(category);
        builder.insert(i, "/page/" + page).insert(0, host);
        Document document = Jsoup.connect(builder.toString()).get();
        Elements elements = document.select(".box.movie1_list li a");
        elements.forEach(element -> {
            String href = element.attr("href");
            String imgUrl = element.getElementsByTag("img").first().attr("src");
            String title = element.getElementsByTag("h3").first().text();
            VideoDTO videoDTO = VideoDTO.builder().name(title).link(href).thumb(imgUrl).build();
            videoDTOList.add(videoDTO);
        });
        return videoDTOList;
    }

    @Override
    public String getVideoPlayUrl(String link) throws Exception {
        int i = link.lastIndexOf("/");
        int j = link.indexOf(".html");
        String vid = link.substring(i, j);
        String detailUrlFmt = "http://n3t3.com/index.php/vod/play/id/%s/sid/1/nid/1.html";
        link = String.format(detailUrlFmt, vid);
        Document document = Jsoup.connect(link).get();
        Elements scripts = document.select("script");
        for (Element script : scripts) {
            String text = script.html();
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                String pageData = matcher.group(1);
                JSONObject object = JSON.parseObject(pageData);
                return object.getString("url");
            }
        }
        return null;
    }
}
