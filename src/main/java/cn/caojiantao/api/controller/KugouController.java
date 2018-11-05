package cn.caojiantao.api.controller;

import cn.caojiantao.api.service.IKugouService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caojiantao
 * @since 2018-01-12 21:26:40
 */
@RestController
@RequestMapping("/kugou")
public class KugouController {

    private final IKugouService songService;

    @Autowired
    public KugouController(IKugouService songService) {
        this.songService = songService;
    }

    @GetMapping("/songs")
    public JSONObject listSongPage(String keyword, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "20", required = false) int pagesize) {
        return songService.getSongs(keyword, page, pagesize);
    }

    @GetMapping("/songs/play")
    public JSONObject getSongPlay(String fileHash, String albumId) {
        return songService.getSongPlay(fileHash, albumId);
    }

    @GetMapping("/mvs")
    public JSONObject listMv(String keyword, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "20", required = false) int pagesize) {
        return songService.getMvs(keyword, page, pagesize);
    }

    @GetMapping("/mvs/play")
    public JSONObject getMvPlay(String hash) {
        return songService.getMvPlay(hash);
    }
}
