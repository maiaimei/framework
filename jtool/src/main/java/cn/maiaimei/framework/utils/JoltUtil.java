package cn.maiaimei.framework.utils;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.core.type.TypeReference;

public class JoltUtil {

  private JoltUtil() {
    throw new UnsupportedOperationException();
  }

  public static Object transform(Object spec, Object input) {
    Chainr chainr = Chainr.fromSpec(spec);
    return chainr.transform(input);
  }

  public static <T> T transform(Object spec, Object input, Class<T> toValueType) {
    return JsonUtil.convert(transform(spec, input), toValueType);
  }

  public static <T> T transform(Object spec, Object input, TypeReference<T> toValueTypeRef) {
    return JsonUtil.convert(transform(spec, input), toValueTypeRef);
  }
}
