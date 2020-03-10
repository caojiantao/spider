package cn.caojiantao.spider.music.netease;

import lombok.Data;

/**
 * @author caojiantao
 */
@Data
class SongCommentParam {

    private String rid;
    private Integer offset;
    private String total = "true";
    private Integer limit;
    private String csrf_token;
}
