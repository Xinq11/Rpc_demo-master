package rpc_demo.registry.zk;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    /*
       注册服务到注册中心
     */
    void registerService(String rpcServiceName,InetSocketAddress inetSocketAddress);
}
