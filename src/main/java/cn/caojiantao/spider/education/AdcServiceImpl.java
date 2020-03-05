package cn.caojiantao.spider.education;

import cn.caojiantao.spider.dto.VideoCategoryDTO;
import cn.caojiantao.spider.dto.VideoDTO;
import com.github.caojiantao.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author caojiantao
 */
@Slf4j
@Service("adczma")
public class AdcServiceImpl implements IEducationService {

    private static String host = "https://adczma.com";

    private static Pattern pattern = Pattern.compile("var vHLSurl = \"(.*)\";");

    private Map<String, Integer> categoryIndexMap = new HashMap<>();

    @Override
    public List<VideoCategoryDTO> getVideoCategoryList() throws Exception {
        List<VideoCategoryDTO> categoryList = new ArrayList<>();
        Document document = Jsoup.connect(host + "/index.html").get();
        Elements elements = document.select(".navbar-textad li a");
        elements.forEach(element -> {
            String href = element.attr("href");
            if (!href.startsWith("http")) {
                String text = element.text();
                VideoCategoryDTO categoryDTO = VideoCategoryDTO.builder().name(text).link(href).build();
                categoryList.add(categoryDTO);
            }
        });
        return categoryList;
    }

    @Override
    public List<VideoDTO> getVideoList(String category, int page) throws Exception {
        Integer index = getCategoryIndex(category);
        List<VideoDTO> videoDTOList = new ArrayList<>();
        String builder = host +
                category +
                "list_" +
                index +
                "_" +
                page +
                ".html";
        Document document = Jsoup.connect(builder).get();
        Elements elements = document.getElementsByClass("list-item");
        elements.forEach(element -> {
            String thumb = element.selectFirst("img").attr("src");
            String name = element.selectFirst(".name").text();
            String link = host + element.select("a").attr("href");
            VideoDTO videoDTO = VideoDTO.builder().thumb(thumb).name(name).link(link).build();
            videoDTOList.add(videoDTO);
        });
        return videoDTOList;
    }

    @Override
    public String getVideoPlayUrl(String link) throws Exception {
        String prefix = "https://v.adceee.com/hls";
        Document document = Jsoup.connect(link).get();
        Element element = document.getElementById("vpath");
        return prefix + element.text();
    }

    private Integer getCategoryIndex(String category) {
        if (!categoryIndexMap.containsKey(category)) {
            try {
                Document document = Jsoup.connect(host + category).get();
                String href = document.select(".pagination .next").attr("href");
                int index = Integer.parseInt(href.split("_")[1]);
                categoryIndexMap.put(category, index);
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return categoryIndexMap.get(category);
    }
}
