package cn.caojiantao.spider.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author caojiantao
 */
@Data
@Builder
public class VideoCategoryDTO {

    private String name;
    private String link;
}
