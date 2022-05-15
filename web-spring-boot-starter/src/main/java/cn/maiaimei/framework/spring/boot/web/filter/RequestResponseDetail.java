package cn.maiaimei.framework.spring.boot.web.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求细节
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestResponseDetail {
    /**
     * 请求标识
     */
    private String requestId;
    /**
     * 请求方法
     */
    private String requestMethod;
    /**
     * 请求地址（不含请求参数）
     */
    private String requestUri;
    /**
     * 请求头
     */
    private String requestHeaders;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 响应体
     */
    private String responseBody;
    /**
     * 状态码
     */
    private String responseStatus;
    /**
     * 客户端远程主机
     */
    private String remoteHost;
    /**
     * 客户端设备类型
     */
    private String device;
    /**
     * 客户端操作系统
     */
    private String os;
}
