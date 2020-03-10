package cn.caojiantao.spider.adult;

import cn.caojiantao.spider.dto.VideoCategoryDTO;
import cn.caojiantao.spider.dto.VideoDTO;
import com.github.caojiantao.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author caojiantao
 */
@Slf4j
@RestController
@RequestMapping("/adult")
public class AdultController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/{source}/getVideoCategoryList")
    public ResultDTO getVideoCategoryList(@PathVariable String source) {
        IAdultService educationService = context.getBean(source, IAdultService.class);
        try {
            List<VideoCategoryDTO> videoCategoryList = educationService.getVideoCategoryList();
            return ResultDTO.success(videoCategoryList);
        } catch (Exception e) {
            log.error("获取视频分类异常", e);
            return ResultDTO.failure(e.getMessage());
        }
    }

    @GetMapping("/{source}/getVideoList")
    public ResultDTO getVideoList(@PathVariable String source, String category, int page) {
        IAdultService educationService = context.getBean(source, IAdultService.class);
        try {
            List<VideoDTO> videoDTOList = educationService.getVideoList(category, page);
            return ResultDTO.success(videoDTOList);
        } catch (Exception e) {
            log.error("获取视频列表异常", e);
            return ResultDTO.failure(e.getMessage());
        }
    }

    @GetMapping("/{source}/getVideoInfo")
    public ResultDTO getVideoInfo(@PathVariable String source, String link) {
        IAdultService adultService = context.getBean(source, IAdultService.class);
        try {
            VideoDTO videoDTO = adultService.getVideoInfo(link);
            return ResultDTO.success(videoDTO);
        } catch (Exception e) {
            log.error("获取视频地址异常", e);
            return ResultDTO.failure(e.getMessage());
        }
    }
}
