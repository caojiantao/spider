package cn.caojiantao.api.tencent;

import cn.caojiantao.api.AssetQuery;
import com.alibaba.fastjson.JSONObject;
import com.github.caojiantao.dto.ResultDTO;
import com.github.caojiantao.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tencent")
public class TencentController {

    private final IQQMusicService qqMusicService;
    private final Tencent tencent;

    @Autowired
    public TencentController(IQQMusicService qqMusicService, Tencent tencent) {
        this.qqMusicService = qqMusicService;
        this.tencent = tencent;
    }

    @GetMapping("/songs")
    public ResultDTO getSongs(AssetQuery query) {
        JSONObject songs = qqMusicService.getSongs(query).getJSONObject("data").getJSONObject("song");
        JSONObject data = JSONUtils.toPageData(songs.getJSONArray("list"), songs.getInteger("totalnum"));
        return ResultDTO.success(data);
    }

    @GetMapping("/songs/{id}/play")
    public ResultDTO getPlayById(@PathVariable("id") String id) {
        JSONObject play = qqMusicService.getSongPlay(id);
        String url = play.getJSONObject("req_0").getJSONObject("data").getJSONArray("midurlinfo").getJSONObject(0).getString("purl");
        return ResultDTO.success(tencent.getPreSongPlay() + url);
    }
}
