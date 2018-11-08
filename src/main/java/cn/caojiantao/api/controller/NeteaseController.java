package cn.caojiantao.api.controller;

import cn.caojiantao.api.service.INeteaseService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public JSONObject listSongPage(String keyword, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "20", required = false) int pagesize) {
        return neteaseService.getSongs(keyword, page, pagesize);
    }
}
