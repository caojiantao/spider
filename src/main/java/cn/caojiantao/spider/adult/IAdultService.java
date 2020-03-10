package cn.caojiantao.spider.adult;

import cn.caojiantao.spider.dto.VideoCategoryDTO;
import cn.caojiantao.spider.dto.VideoDTO;

import java.util.List;

/**
 * @author caojiantao
 */
public interface IAdultService {

    List<VideoCategoryDTO> getVideoCategoryList() throws Exception;

    List<VideoDTO> getVideoList(String category, int page) throws Exception;

    VideoDTO getVideoInfo(String link) throws Exception;
}
