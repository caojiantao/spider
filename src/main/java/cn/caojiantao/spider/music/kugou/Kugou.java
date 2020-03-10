package cn.caojiantao.spider.music.kugou;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author caojiantao
 * @date 2018-11-05 23:53:48
 * @description
 */
@Data
@Configuration
@ConfigurationProperties("kugou")
public class Kugou {

    private String songSearch;
    private String songPlay;
    private String mvSearch;
    private String mvPlay;
    private String salt;
}
