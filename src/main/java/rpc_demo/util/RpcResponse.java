package rpc_demo.util;

import com.alibaba.rocketmq.common.protocol.ResponseCode;
import lombok.Data;
import java.io.Serializable;

@Data
public class RpcResponse<T> implements Serializable {
    //响应状态码
    private Integer statusCode;
    //响应状态补充信息
    private String message;
    //响应数据
    private T data;

    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<T>();
        response.setStatusCode(0000);
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<T>();
        response.setStatusCode(1111);
        response.setMessage("error!");
        return response;
    }
}
