package cn.caojiantao.api.netease;

import cn.caojiantao.api.AssetQuery;
import com.alibaba.fastjson.JSONObject;

/**
 * @author caojiantao
 * @date 2018-11-09 00:46:44
 * @description
 */
public interface INeteaseService {

    JSONObject getSongs(AssetQuery query);

    JSONObject getPlayById(Long id);

    String getLyricById(Long id);

    JSONObject getComments(AssetQuery query);
}
