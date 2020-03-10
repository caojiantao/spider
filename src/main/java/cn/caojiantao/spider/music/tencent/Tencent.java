package cn.caojiantao.spider.music.tencent;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("tencent")
public class Tencent {

    private String songSearch;
    private String songPlay;
    private String preSongPlay;
}
