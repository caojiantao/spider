package cn.caojiantao.api.controller;

import cn.caojiantao.api.service.IYouKuService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caojiantao
 */
@RestController
@RequestMapping("/youku")
public class YoukuController {

    private final IYouKuService youkuService;

    @Autowired
    public YoukuController(IYouKuService youkuService) {
        this.youkuService = youkuService;
    }

    @GetMapping("/videos/{vid}")
    public JSONObject getJSONDataByVid(@PathVariable("vid") String vid) {
        return youkuService.getJSONDataByVid(vid);
    }
}
