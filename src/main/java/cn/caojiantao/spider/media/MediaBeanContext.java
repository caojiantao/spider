package cn.caojiantao.spider.media;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author caojiantao
 */
public class MediaBeanContext {

    private static Map<String, IMediaService> mediaServiceMap = new ConcurrentHashMap<>();

    public static IMediaService fetch(String key) {
        return mediaServiceMap.get(key);
    }

    public static void register(String key, IMediaService value) {
        mediaServiceMap.put(key, value);
    }
}
