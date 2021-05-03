package rpc_demo.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.registry.zk.util.CuratorUtils;
import java.net.InetSocketAddress;

public class ZkServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZkServiceRegistry.class);

    @Override
    public void registerService(String rpcServiceName,InetSocketAddress inetSocketAddress) {
        //znode路径格式： ZK_REGISTER_ROOT_PATH/接口名称/ip地址+端口号
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        logger.info(servicePath);
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createEphemeralNode(zkClient, servicePath);
    }
}
