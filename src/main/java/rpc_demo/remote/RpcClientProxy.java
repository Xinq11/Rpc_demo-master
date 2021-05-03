package rpc_demo.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.registry.zk.ZkServiceDiscovery;
import rpc_demo.remote.netty.NettyClient;
import rpc_demo.remote.socket.SocketClient;
import rpc_demo.util.RpcRequest;
import rpc_demo.util.RpcResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {

    private String host;
    private int port;
    private NettyClient client;
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public RpcClientProxy(){}

    public RpcClientProxy(NettyClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        //创建代理对象
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        ZkServiceDiscovery zkServiceDiscovery = new ZkServiceDiscovery();
        String s = zkServiceDiscovery.lookupService(method.getDeclaringClass().getName());
        logger.info(s);
        String[] address = s.split(":");
        NettyClient client = new NettyClient(address[0],Integer.valueOf(address[1]));
        if(client==null){
            RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
            SocketClient rpcClient = new SocketClient();
            return ((RpcResponse) rpcClient.sendRequest(rpcRequest, host, port)).getData();
        }else{
            RpcRequest rpcRequest = RpcRequest.builder()
                    .interfaceName(method.getDeclaringClass().getName())
                    .methodName(method.getName())
                    .parameters(args)
                    .paramTypes(method.getParameterTypes())
                    .build();
            return client.sendRequest(rpcRequest);
        }
    }
}
