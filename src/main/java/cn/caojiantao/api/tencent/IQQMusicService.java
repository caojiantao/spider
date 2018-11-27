package cn.caojiantao.api.tencent;

import cn.caojiantao.api.AssetQuery;
import com.alibaba.fastjson.JSONObject;

public interface IQQMusicService {

    JSONObject getSongs(AssetQuery query);

    JSONObject getSongPlay(String mid);
}
