package cn.caojiantao.spider.music.kugou;

import cn.caojiantao.spider.music.IMediaService;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * @author caojiantao
 */
public interface IKugouService extends IMediaService {

    JSONObject getRankByType(String type) throws IOException;
}
