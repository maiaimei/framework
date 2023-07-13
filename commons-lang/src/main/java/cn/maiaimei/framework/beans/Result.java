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

  private Integer code;
  private Object message;
  private T data;
  private String source;
}
