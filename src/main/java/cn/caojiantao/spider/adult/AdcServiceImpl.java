package cn.caojiantao.spider.adult;

import cn.caojiantao.spider.dto.VideoCategoryDTO;
import cn.caojiantao.spider.dto.VideoDTO;
import com.github.caojiantao.util.ExceptionUtils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author caojiantao
 */
@Slf4j
@Service("adczma")
public class AdcServiceImpl implements IAdultService {

    private static String host = "https://adczma.com";

    private Map<String, Integer> categoryIndexMap = new HashMap<>();

    /**
     * 视频加速域名
     */
    private List<String> vpsList = Arrays.asList("https://v.adceee.com/hls/", "https://v.adcecc.com/hls/",
            "https://v.adcebb.com/hls/", "https://v.adceme.com/hls/", "https://v.adcenn.com/hls/",
            "https://v.adceku.com/hls/", "https://v.adcekk.com/hls/", "https://v.adceaa.com/hls/",
            "https://v.adcdzz.com/hls/", "https://v.adceff.com/hls/");

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
    @Cacheable(cacheNames = "adultVideo")
    public VideoDTO getVideoInfo(String link) throws Exception {
        String vps = vpsList.get(ThreadLocalRandom.current().nextInt(vpsList.size()));
        Document document = Jsoup.connect(link).get();
        Element element = document.getElementById("vpath");
        Preconditions.checkNotNull(element.text());
        String playUrl = vps + element.text();
        String name = document.title().substring(0, document.title().indexOf("_"));
        return VideoDTO.builder()
                .thumb("https://adczma.com/static/p/Loading.gif")
                .link(link)
                .name(name)
                .playUrl(playUrl)
                .build();
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
