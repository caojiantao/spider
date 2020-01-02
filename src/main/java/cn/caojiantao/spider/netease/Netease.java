package cn.caojiantao.spider.netease;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author caojiantao
 * @date 2018-11-09 00:51:42
 * @description
 */
@Data
@Configuration
@ConfigurationProperties("netease")
public class Netease {

    private String randomKey;
    private String fourthParam;
    private String ivParameter;
    private String encSecKey;

    private String songSearch;
    private String songPlay;
    private String songLyric;
    private String songComment;
}
