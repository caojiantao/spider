package cn.caojiantao.api.service.impl;

import cn.caojiantao.api.configuration.Netease;
import cn.caojiantao.api.service.INeteaseService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
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
@Service
public class NeteaseServiceImpl implements INeteaseService {

    private final Netease netease;

    @Autowired
    public NeteaseServiceImpl(Netease netease) {
        this.netease = netease;
    }

    @Override
    public JSONObject getSongs(String keyword, int page, int pagesize) {
        try {
            String result = Jsoup.connect(netease.getSongSearch())
                    .data("params", getParams("{\"hlpretag\":\"<span class=\\\"s-fc7\\\">\",\"hlposttag\":\"</span>\",\"s\":\"" + keyword + "\",\"type\":\"1\",\"offset\":\""  + (page - 1) * pagesize + "\",\"total\":\"true\",\"limit\":\"" + pagesize + "\",\"csrf_token\":\"\"}"))
                    .data("encSecKey", netease.getEncSecKey())
                    .post()
                    .text();
            return JSON.parseObject(result, JSONObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getParams(String data) {
        String params = "";
        try {
            params = encrypt(encrypt(data, netease.getFourthParam()), netease.getRandomKey());
        } catch (Exception e) {
            e.printStackTrace();
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
}
