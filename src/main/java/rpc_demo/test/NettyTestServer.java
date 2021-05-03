package rpc_demo.test;

import rpc_demo.Interface.HelloService;
import rpc_demo.Interface.HelloServiceImpl;
import rpc_demo.registry.zk.ZkServiceRegistry;
import rpc_demo.remote.netty.NettyServer;
import rpc_demo.registry.zk.ServiceRegistry;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
/*
    使用netty通信
 */
public class NettyTestServer {
    public static void main(String[] args) throws UnknownHostException {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new ZkServiceRegistry();
        InetAddress netAddress = InetAddress.getLocalHost();
        String host_address = netAddress.getHostAddress();
        InetSocketAddress address = new InetSocketAddress(host_address, 9999);
        Class<?>[] interfaces = helloService.getClass().getInterfaces();
        for(Class<?> i :interfaces){
            serviceRegistry.registerService(i.getCanonicalName(),address);
        }
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
