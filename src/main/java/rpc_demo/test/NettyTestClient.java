package rpc_demo.test;

import rpc_demo.Interface.HelloObject;
import rpc_demo.Interface.HelloService;
import rpc_demo.registry.zk.ZkServiceDiscovery;
import rpc_demo.remote.RpcClientProxy;
import rpc_demo.remote.netty.NettyClient;
import java.net.InetAddress;
import java.net.UnknownHostException;
/*
    使用netty通信
 */
public class NettyTestClient {
    public static void main(String[] args){
        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println("============================分割线==============================");
        System.out.println(res);
        System.out.println("===============================================================");
    }
}
