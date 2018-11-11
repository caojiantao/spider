package cn.caojiantao.api.netease;

import cn.caojiantao.api.AssetQuery;
import com.alibaba.fastjson.JSONObject;
import com.github.caojiantao.dto.ResultDTO;
import com.github.caojiantao.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caojiantao
 * @date 2018-11-09 01:03:42
 * @description
 */
@RestController
@RequestMapping("/netease")
public class NeteaseController {

    private final INeteaseService neteaseService;

    @Autowired
    public NeteaseController(INeteaseService neteaseService) {
        this.neteaseService = neteaseService;
    }

    @GetMapping("/songs")
    public ResultDTO getSongs(AssetQuery query) {
        JSONObject songs = neteaseService.getSongs(query).getJSONObject("result");
        JSONObject data = JSONUtils.toPageData(songs.getJSONArray("songs"), songs.getInteger("songCount"));
        return ResultDTO.success(data);
    }

    @GetMapping("/songs/{id}/play")
    public ResultDTO getPlayById(@PathVariable("id") Long id) {
        JSONObject play = neteaseService.getPlayById(id);
        return ResultDTO.success(play);
    }

    @GetMapping("/songs/{id}/lyric")
    public ResultDTO getLyricById(@PathVariable("id") Long id) {
        String lyric = neteaseService.getLyricById(id);
        return ResultDTO.success(lyric);
    }

    @GetMapping("/songs/{id}/comments")
    public ResultDTO getCommentsById(@PathVariable("id") Long id, AssetQuery query) {
        query.setId(id);
        JSONObject result = neteaseService.getComments(query);
        return ResultDTO.success(JSONUtils.toPageData(result.getJSONArray("comments"), result.getInteger("total")));
    }

    @GetMapping("/songs/{id}/hotComments")
    public ResultDTO getHotCommentsById(@PathVariable("id") Long id) {
        AssetQuery query = new AssetQuery();
        query.setId(id);
        JSONObject result = neteaseService.getComments(query);
        return ResultDTO.success(result.getJSONArray("hotComments"));
    }
}
