package cn.caojiantao.spider.music.netease;

import cn.caojiantao.spider.AssetQuery;
import cn.caojiantao.spider.music.MediaBeanContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.caojiantao.util.ExceptionUtils;
import com.github.caojiantao.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

/**
 * @author caojiantao
 * @date 2018-11-09 00:48:21
 * @description
 */
@Slf4j
@Service
public class NeteaseServiceImpl implements INeteaseService, InitializingBean {

    private final Netease netease;

    @Autowired
    public NeteaseServiceImpl(Netease netease) {
        this.netease = netease;
    }

    @Override
    public JSONObject getSongList(AssetQuery query) {
        SongSearchParam param = new SongSearchParam();
        param.setS(query.getKeyword());
        param.setOffset((query.getPage() - 1) * query.getPagesize());
        param.setLimit(query.getPagesize());
        try {
            String result = Jsoup.connect(netease.getSongSearch())
                    .data("params", getParams(JSON.toJSONString(param)))
                    .data("encSecKey", netease.getEncSecKey())
                    .post()
                    .text();
            JSONObject songs = JSON.parseObject(result, JSONObject.class).getJSONObject("result");
            return JSONUtils.toPageData(songs.getJSONArray("songs"), songs.getInteger("songCount"));
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public JSONObject getSongInfo(AssetQuery query) {
        try {
            Long id = query.getId();
            String result = Jsoup.connect(netease.getSongPlay())
                    .data("params", getParams("{\"ids\":\"[" + id + "]\",\"br\":128000,\"csrf_token\":\"\"}"))
                    .data("encSecKey", netease.getEncSecKey())
                    .post()
                    .text();
            JSONObject object = JSON.parseObject(result);
            return object.getJSONArray("data").getJSONObject(0);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public String getLyricById(AssetQuery query) {
        try {
            Long id = query.getId();
            String result = Jsoup.connect(netease.getSongLyric())
                    .data("params", getParams("{\"id\":\"" + id + "\",\"lv\":-1,\"tv\":-1,\"csrf_token\":\"\"}"))
                    .data("encSecKey", netease.getEncSecKey())
                    .post()
                    .text();
            JSONObject object = JSON.parseObject(result);
            return object.getJSONObject("lrc").getString("lyric");
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public JSONObject getCommentList(AssetQuery query) {
        try {
            SongCommentParam param = new SongCommentParam();
            param.setRid("R_SO_4_" + query.getId());
            param.setOffset((query.getPage() - 1) * query.getPagesize());
            param.setLimit(query.getPagesize());
            String result = Jsoup.connect(netease.getSongComment() + param.getRid())
                    .data("params", getParams(JSON.toJSONString(param)))
                    .data("encSecKey", netease.getEncSecKey())
                    .post()
                    .text();
            return JSON.parseObject(result);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private String getParams(String data) {
        String params = "";
        try {
            params = encrypt(encrypt(data, netease.getFourthParam()), netease.getRandomKey());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return params;
    }

    private String encrypt(String text, String secKey) throws Exception {
        byte[] raw = secKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(netease.getIvParameter().getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return DatatypeConverter.printBase64Binary(encrypted);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MediaBeanContext.register("netease", this);
    }
}
