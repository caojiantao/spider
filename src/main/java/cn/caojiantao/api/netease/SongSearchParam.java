package cn.caojiantao.api.netease;

import lombok.Data;

/**
 * @author caojiantao
 */
@Data
class SongSearchParam {

    private String hlpretag = "<span class=\"s-fc7\">";
    private String hlposttag = "</span>";
    private String s;
    private Integer type = 1;
    private Integer offset;
    private String total = "true";
    private Integer limit;
    private String csrf_token;
}
