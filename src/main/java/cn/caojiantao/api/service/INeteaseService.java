package cn.caojiantao.api.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author caojiantao
 * @date 2018-11-09 00:46:44
 * @description
 */
public interface INeteaseService {

    JSONObject getSongs(String keyword, int page, int pagesize);
}
