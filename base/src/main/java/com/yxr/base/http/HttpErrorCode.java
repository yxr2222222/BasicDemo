package com.yxr.base.http;

/**
 * @author ciba
 * @description 接口访问异常错误码
 * @date 2020/09/17
 */
public interface HttpErrorCode {
     int CODE_TIMEOUT = -5000;
     int CODE_UNKNOWN_HOST = -5001;
     int CODE_NETWORK_EXCEPTION = -5002;
     int CODE_CONNECT_EXCEPTION = -5003;
     int CODE_DATA_PARSE_EXCEPTION = -5004;
     int CODE_IO_EXCEPTION = -5005;
     int CODE_NULL_DATA = -5006;
     int CODE_UNKNOWN = -1;
}
