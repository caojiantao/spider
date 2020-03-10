package cn.caojiantao.spider.music;

import cn.caojiantao.spider.AssetQuery;
import com.alibaba.fastjson.JSONObject;

/**
 * @author caojiantao
 */
public interface IMediaService {

    default JSONObject getSongList(AssetQuery query) {
        throw new IllegalArgumentException("暂不支持");
    }

    default JSONObject getSongInfo(AssetQuery query) {
        throw new IllegalArgumentException("暂不支持");
    }

    default String getLyricById(AssetQuery query) {
        throw new IllegalArgumentException("暂不支持");
    }

    default JSONObject getCommentList(AssetQuery query) {
        throw new IllegalArgumentException("暂不支持");
    }

    default JSONObject getMvList(AssetQuery query) {
        throw new IllegalArgumentException("暂不支持");
    }

    default JSONObject getMvInfo(AssetQuery query) {
        throw new IllegalArgumentException("暂不支持");
    }
}
