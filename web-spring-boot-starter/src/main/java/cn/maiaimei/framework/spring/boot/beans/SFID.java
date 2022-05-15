package cn.maiaimei.framework.spring.boot.beans;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class SFID {
    private static Snowflake snowflake;

    @Value("${SFID.datacenterId:1}")
    private long datacenterId;
    @Value("${SFID.workerId:1}")
    private long workerId;

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    @PostConstruct
    public void init() {
        snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    public static long nextId() {
        return snowflake.nextId();
    }
}
