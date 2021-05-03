package rpc_demo.test;

import rpc_demo.Interface.HelloService;
import rpc_demo.Interface.HelloServiceImpl;
import rpc_demo.registry.map.DefaultServiceRegistry;
import rpc_demo.registry.map.ServiceRegistry;
import rpc_demo.remote.socket.SocketServer;
/*
    使用socket通信
 */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);
    }
}
