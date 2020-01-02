package cn.caojiantao.spider.youku;

import com.alibaba.fastjson.JSONObject;

/**
 * @author caojiantao
 */
public interface IYouKuService {

    /**
     * 获取指定VID的优酷视频
     */
    JSONObject getJSONDataByVid(String vid);
}
