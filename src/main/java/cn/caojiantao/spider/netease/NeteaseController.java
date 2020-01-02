package cn.caojiantao.spider.netease;

import cn.caojiantao.spider.AssetQuery;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.caojiantao.dto.ResultDTO;
import com.github.caojiantao.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/getCommentListById")
    public ResultDTO getCommentListById(AssetQuery query) {
        query.setId(query.getId());
        JSONObject result = neteaseService.getCommentList(query);
        JSONObject page = JSONUtils.toPageData(result.getJSONArray("comments"), result.getInteger("total"));
        return ResultDTO.success(page);
    }

    @GetMapping("getHotCommentListById")
    public ResultDTO getHotCommentListById(Long id) {
        AssetQuery query = new AssetQuery();
        query.setId(id);
        JSONObject result = neteaseService.getCommentList(query);
        JSONArray page = result.getJSONArray("hotComments");
        return ResultDTO.success(page);
    }
}
