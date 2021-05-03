package rpc_demo.registry.zk;

public interface ServiceDiscovery {
    /*
       发现服务
     */
    Object lookupService(String rpcServiceName);
}
