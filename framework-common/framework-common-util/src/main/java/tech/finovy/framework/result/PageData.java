package tech.finovy.framework.result;


import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PageData<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> data;

    private long totalCounts;

    public PageData(List<T> data, long totalCounts) {
        this.data = data;
        this.totalCounts = totalCounts;
    }
    public PageData(List<T> data) {
        this.data = data;
        this.totalCounts = CollectionUtils.isEmpty(data) ? 0L:data.size();
    }
    public PageData() {
    }
}
