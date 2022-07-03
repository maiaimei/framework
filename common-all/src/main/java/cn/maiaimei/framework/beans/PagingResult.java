package cn.maiaimei.framework.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResult<T> {
    protected List<T> records;
    protected long total;
    protected long current;
    protected long size;
}