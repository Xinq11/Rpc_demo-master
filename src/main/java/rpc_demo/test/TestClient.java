package rpc_demo.test;

import rpc_demo.Interface.HelloObject;
import rpc_demo.Interface.HelloService;
import rpc_demo.remote.RpcClientProxy;
/*
    使用socket通信
 */
public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println("============================分割线==============================");
        System.out.println(res);
        System.out.println("===============================================================");
    }
}
