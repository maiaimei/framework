package cn.maiaimei.framework.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SFID {

  private static Snowflake snowflake;

  private SFID() {
    throw new UnsupportedOperationException();
  }

  public static void getInstance(long datacenterId, long workerId) {
    snowflake = IdUtil.getSnowflake(workerId, datacenterId);
  }

  public static long nextId() {
    return snowflake.nextId();
  }
}
