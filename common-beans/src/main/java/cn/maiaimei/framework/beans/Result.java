package cn.maiaimei.framework.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result<T> {
    private String code;
    private String message;
    private T data;
    private String traceId;
    private String trace;
    private String path;
}
