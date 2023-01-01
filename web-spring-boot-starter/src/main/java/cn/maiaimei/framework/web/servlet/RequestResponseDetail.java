package cn.maiaimei.framework.web.servlet;

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
     * 请求地址
     */
    private String requestUri;
    /**
     * 请求方法
     */
    private String requestMethod;
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
     * 状态码
     */
    private String responseStatus;
    /**
     * 响应体
     */
    private String responseBody;
    /**
     * 客户端IP
     */
    private String clientIP;
    /**
     * 客户端设备
     */
    private String clientDevice;
    /**
     * 客户端系统
     */
    private String clientOS;
    /**
     * 用户代理
     */
    private String userAgent;
}
