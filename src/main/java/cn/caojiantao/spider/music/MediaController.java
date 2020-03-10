package cn.caojiantao.spider.music;

import cn.caojiantao.spider.AssetQuery;
import com.github.caojiantao.dto.ResultDTO;
import com.google.common.base.Preconditions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caojiantao
 */
@RestController
@RequestMapping("/music")
public class MediaController {

    @GetMapping("/{platform}/{action}")
    public ResultDTO action(AssetQuery query, @PathVariable String platform, @PathVariable String action) {
        IMediaService mediaService = MediaBeanContext.fetch(platform);
        Preconditions.checkNotNull(mediaService, platform + "不支持");
        MediaActionEnum actionEnum = MediaActionEnum.of(action);
        Preconditions.checkNotNull(actionEnum, action + "不合法");
        Object data = null;
        switch (actionEnum) {
            case GET_SONG_LIST:
                data = mediaService.getSongList(query);
                break;
            case GET_SONG_INFO:
                data = mediaService.getSongInfo(query);
                break;
            case GET_MV_LIST:
                data = mediaService.getMvList(query);
                break;
            case GET_MV_INFO:
                data = mediaService.getMvInfo(query);
                break;
            case GET_LYRIC_BY_ID:
                data = mediaService.getLyricById(query);
                break;
            case GET_COMMENT_LIST:
                data = mediaService.getCommentList(query);
                break;
            default:
                break;
        }
        return ResultDTO.success(data);
    }
}
