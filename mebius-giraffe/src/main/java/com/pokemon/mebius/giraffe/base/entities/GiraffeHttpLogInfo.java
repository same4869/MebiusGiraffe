package com.pokemon.mebius.giraffe.base.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * susionwang at 2019-10-25
 */

@Entity
public class GiraffeHttpLogInfo implements GiraffeInfoProtocol {

    @Id(autoincrement = true)
    public Long id;

    /**
     * 请求域名
     */
    public String host = "";

    /**
     * 请求的curl参数
     */
    public String curlBean = "";

    /**
     * 请求路径
     */
    public String path = "";
    /**
     * 请求体
     */
    public String requestBody = "";
    /**
     * 响应体
     */
    public String responseStr = "";
    /**
     * 响应体大小
     */
    public String size = "";
    /**
     * 请求方法类型
     */
    public String requestType = "";
    /**
     * 响应体类型
     */
    public String responseContentType = "gson";
    /**
     * 响应码
     */
    public String responseCode = "";
    /**
     * 请求参数---GET请求在url后面的参数
     */
    public String requestParamsMapString = "";
    /**
     * 请求发起时间
     */
    public Long time;
    /**
     * 请求耗时时间
     */
    public Long tookTime;
    /**
     * 这次请求是否是成功的(网络请求结果)
     */
    public boolean isSuccessRequest;
    /**
     * 这次请求是否是异常的(标记tryCatch住异常了)
     */
    public boolean isExceptionRequest;

    /**
     * requestHeaders
     */
    public String requestHeaders;
    /**
     * responseHeaders
     */
    public String responseHeaders;

    @Generated(hash = 453502905)
    public GiraffeHttpLogInfo(Long id, String host, String curlBean, String path,
                              String requestBody, String responseStr, String size, String requestType,
                              String responseContentType, String responseCode,
                              String requestParamsMapString, Long time, Long tookTime,
                              boolean isSuccessRequest, boolean isExceptionRequest,
                              String requestHeaders, String responseHeaders) {
        this.id = id;
        this.host = host;
        this.curlBean = curlBean;
        this.path = path;
        this.requestBody = requestBody;
        this.responseStr = responseStr;
        this.size = size;
        this.requestType = requestType;
        this.responseContentType = responseContentType;
        this.responseCode = responseCode;
        this.requestParamsMapString = requestParamsMapString;
        this.time = time;
        this.tookTime = tookTime;
        this.isSuccessRequest = isSuccessRequest;
        this.isExceptionRequest = isExceptionRequest;
        this.requestHeaders = requestHeaders;
        this.responseHeaders = responseHeaders;
    }

    @Generated(hash = 270070941)
    public GiraffeHttpLogInfo() {
    }

    @Override
    public Long getTime() {
        return this.time;
    }

    @Override
    public String getPageName() {
        return "";
    }

    public boolean isvalid() {
        return !host.isEmpty() && !path.isEmpty();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestBody() {
        return this.requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseStr() {
        return this.responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getResponseContentType() {
        return this.responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestParamsMapString() {
        return this.requestParamsMapString;
    }

    public void setRequestParamsMapString(String requestParamsMapString) {
        this.requestParamsMapString = requestParamsMapString;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTookTime() {
        return this.tookTime;
    }

    public void setTookTime(Long tookTime) {
        this.tookTime = tookTime;
    }

    public boolean getIsSuccessRequest() {
        return this.isSuccessRequest;
    }

    public void setIsSuccessRequest(boolean isSuccessRequest) {
        this.isSuccessRequest = isSuccessRequest;
    }

    public boolean getIsExceptionRequest() {
        return this.isExceptionRequest;
    }

    public void setIsExceptionRequest(boolean isExceptionRequest) {
        this.isExceptionRequest = isExceptionRequest;
    }

    public String getRequestHeaders() {
        return this.requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getResponseHeaders() {
        return this.responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getCurlBean() {
        return this.curlBean;
    }

    public void setCurlBean(String curlBean) {
        this.curlBean = curlBean;
    }
}
