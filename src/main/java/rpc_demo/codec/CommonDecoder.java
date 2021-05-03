package rpc_demo.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.codec.utils.PackageType;
import rpc_demo.util.RpcRequest;
import rpc_demo.util.RpcResponse;

import java.util.List;

/**
 * 解码
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        // 检测协议头
        int magic = in.readInt();
        if(magic!=MAGIC_NUMBER){
            logger.error("不识别的协议包: {}", magic);
        }
        int packageCode = in.readInt();

        //检测包类型
        Class<?> packageClass = null;
        if(packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if(packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包: {}", packageCode);
        }

        //获取序列化方式
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null) {
            logger.error("不识别的反序列化器: {}", serializerCode);
        }

        //获取数据包长度
        int length = in.readInt();
        byte[] bytes = new byte[length];

        //读取固定长度数据
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);

        out.add(obj);
    }
}
