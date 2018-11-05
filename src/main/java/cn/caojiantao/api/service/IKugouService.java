package cn.caojiantao.api.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * @author caojiantao
 * @since 2018-01-12 20:44:59
 */
public interface IKugouService {

    /**
     * 根据关键词，分页获取歌曲信息集合
     *
     * @param keyword  关键词
     * @param page     页码
     * @param pagesize 每页数量
     * @return 歌曲分页信息
     */
    JSONObject getSongs(String keyword, int page, int pagesize);

    /**
     * 根据歌曲的hash和专辑ID获取歌曲播放相关信息
     *
     * @param fileHash 歌曲文件hash值
     * @param albumId  专辑ID
     * @return 歌曲播放信息
     */
    JSONObject getSongPlay(String fileHash, String albumId);


    JSONObject getMvs(String keyword, int page, int pagesize);

    JSONObject getMvPlay(String hash);

    JSONObject getRankByType(String type) throws IOException;
}
