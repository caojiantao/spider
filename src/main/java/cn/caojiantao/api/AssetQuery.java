package cn.caojiantao.api;

import com.github.caojiantao.dto.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caojiantao
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AssetQuery extends BaseQuery {

    private Long id;
    private String keyword;

    @Override
    public Integer getPage() {
        if (super.getPage() == null) {
            return 1;
        }
        return super.getPage();
    }

    @Override
    public Integer getPagesize() {
        if (super.getPagesize() == null) {
            return 20;
        }
        return super.getPagesize();
    }
}
