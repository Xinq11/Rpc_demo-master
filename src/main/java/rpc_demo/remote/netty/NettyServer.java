package rpc_demo.remote.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.codec.CommonDecoder;
import rpc_demo.codec.CommonEncoder;
import rpc_demo.codec.serializer.JsonSerializer;
import rpc_demo.remote.RpcServer;
import rpc_demo.remote.netty.handler.NettyServerHandler;

public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Override
    public void start(int port) {
        EventLoopGroup BossGroup = new NioEventLoopGroup();
        EventLoopGroup WorkerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(BossGroup,WorkerGroup)
                           .channel(NioServerSocketChannel.class)
                           .handler(new LoggingHandler(LogLevel.INFO))
                           .option(ChannelOption.SO_KEEPALIVE, true)
                           .childOption(ChannelOption.TCP_NODELAY, true)
                           .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                                pipeline.addLast(new CommonDecoder());
                                pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            logger.error("启动服务器时有错误发生: ", e);
        }finally {
            BossGroup.shutdownGracefully();
            WorkerGroup.shutdownGracefully();
        }
    }
}
