package rpc_demo.remote.netty.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.registry.zk.ServiceDiscovery;
import rpc_demo.registry.zk.ZkServiceDiscovery;
import rpc_demo.remote.RpcServerProxy;
import rpc_demo.util.RpcRequest;
import rpc_demo.util.RpcResponse;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            String interfaceName = msg.getInterfaceName();
            Class c = Class.forName(String.valueOf(interfaceName));
            Object result = RpcServerProxy.serverspi(c,msg);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
            } finally {
        }
    }
}
