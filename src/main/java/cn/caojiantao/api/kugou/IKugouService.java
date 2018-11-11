package cn.caojiantao.api.kugou;

import cn.caojiantao.api.AssetQuery;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * @author caojiantao
 */
public interface IKugouService {

    JSONObject getSongs(AssetQuery query);

    JSONObject getSongInfo(String fileHash, String albumId);

    JSONObject getMvs(AssetQuery query);

    JSONObject getMvPlay(String hash);

    JSONObject getRankByType(String type) throws IOException;
}
