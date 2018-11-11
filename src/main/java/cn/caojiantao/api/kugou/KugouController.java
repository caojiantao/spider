package cn.caojiantao.api.kugou;

import cn.caojiantao.api.AssetQuery;
import com.github.caojiantao.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caojiantao
 * @since 2018-01-12 21:26:40
 */
@RestController
@RequestMapping("/kugou")
public class KugouController {

    private final IKugouService kugouService;

    @Autowired
    public KugouController(IKugouService kugouService) {
        this.kugouService = kugouService;
    }

    @GetMapping("/songs")
    public ResultDTO getSongs(AssetQuery query) {
        return ResultDTO.success(kugouService.getSongs(query));
    }

    @GetMapping("/songs/info")
    public ResultDTO getSongInfo(String fileHash, String albumId) {
        return ResultDTO.success(kugouService.getSongInfo(fileHash, albumId));
    }

    @GetMapping("/mvs")
    public ResultDTO listMv(AssetQuery query) {
        return ResultDTO.success(kugouService.getMvs(query));
    }

    @GetMapping("/mvs/play")
    public ResultDTO getMvPlay(String hash) {
        return ResultDTO.success(kugouService.getMvPlay(hash));
    }
}
