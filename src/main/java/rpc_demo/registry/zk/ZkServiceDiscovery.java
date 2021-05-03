package rpc_demo.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.LoadBalance.LoadBalance;
import rpc_demo.LoadBalance.loadbalancer.RandomLoadBalance;
import rpc_demo.registry.zk.util.CuratorUtils;
import java.util.List;

public class ZkServiceDiscovery implements ServiceDiscovery {

    private final LoadBalance loadBalance;
    public ZkServiceDiscovery() {
        this.loadBalance = new RandomLoadBalance();
    }
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    @Override
    public String lookupService(String rpcServiceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList.size() == 0) {
            logger.info("error");
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList,rpcServiceName);
        return targetServiceUrl;
    }
}
