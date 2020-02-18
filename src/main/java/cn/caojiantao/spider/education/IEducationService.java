package cn.caojiantao.spider.education;

import cn.caojiantao.spider.dto.VideoCategoryDTO;
import cn.caojiantao.spider.dto.VideoDTO;

import java.util.List;

/**
 * @author caojiantao
 */
public interface IEducationService {

    List<VideoCategoryDTO> getVideoCategoryList() throws Exception;

    List<VideoDTO> getVideoList(String category, int page) throws Exception;

    String getVideoPlayUrl(String link) throws Exception;
}
