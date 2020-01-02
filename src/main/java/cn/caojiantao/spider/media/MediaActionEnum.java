package cn.caojiantao.spider.media;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author caojiantao
 */
@NoArgsConstructor
@AllArgsConstructor
public enum MediaActionEnum {

    GET_SONG_LIST("getSongList"),
    GET_SONG_INFO("getSongInfo"),
    GET_MV_LIST("getMvList"),
    GET_MV_INFO("getMvInfo"),
    GET_LYRIC_BYID("getLyricById"),
    GET_COMMENT_LIST("getCommentList"),
    ;

    private String action;

    public static MediaActionEnum of(String action) {
        MediaActionEnum[] values = MediaActionEnum.values();
        for (MediaActionEnum item : values) {
            if (item.action.equalsIgnoreCase(action)) {
                return item;
            }
        }
        return null;
    }
}
