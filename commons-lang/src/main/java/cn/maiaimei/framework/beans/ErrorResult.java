package cn.maiaimei.framework.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorResult<T> extends Result<T> {
    private String traceId;
    private String trace;
    private String path;
}
