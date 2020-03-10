package cn.caojiantao.spider.music;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author caojiantao
 */
@NoArgsConstructor
@AllArgsConstructor
public enum MediaActionEnum {

    /**
     * 获取歌曲列表
     */
    GET_SONG_LIST("getSongList"),
    /**
     * 获取歌曲信息
     */
    GET_SONG_INFO("getSongInfo"),
    /**
     * 获取MV列表
     */
    GET_MV_LIST("getMvList"),
    /**
     * 虎丘MV信息
     */
    GET_MV_INFO("getMvInfo"),
    /**
     * 获取歌词
     */
    GET_LYRIC_BY_ID("getLyricById"),
    /**
     * 获取评论列表
     */
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
