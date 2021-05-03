package rpc_demo.registry.zk.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    //client本地缓存
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    //server本地缓存
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;
    private static final Logger logger = LoggerFactory.getLogger(CuratorUtils.class);
    private CuratorUtils() {}

    //注册服务
    public static void createEphemeralNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                logger.info("The node already exists. The node is:[{}]", path);
            } else {
                //逆递归创建父节点 父节点是持久节点 path路径内的节点为临时节点
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                logger.info("The node was created successfully. The node is:[{}]", path);
            }
            //更新key缓存
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            logger.error("create persistent node for path [{}] fail", path);
        }
    }

    //得到服务
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        /**
         * 先去本地缓存里找
         * 1.找到直接返回IP地址
         * 2.没找到再去zk里找并添加到本地缓存
         */
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result = null;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            logger.error("get children nodes for path [{}] fail", servicePath);
        }
        return result;
    }

    //连接zk
    public static CuratorFramework getZkClient() {
        String zookeeperAddress = "121.43.132.192:2181";
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }

    //监听节点
    /**
     * Curator 事件有两种模式，一种是标准的观察模式，一种是缓存监听模式。
     *                      标准的监听模式是使用Watcher 监听器。
     *                      第二种缓存监听模式引入了一种本地缓存视图的Cache机制，来实现对Zookeeper服务端事件监听。
     *
     * Cache事件监听可以理解为一个本地缓存视图与远程Zookeeper视图的对比过程。
     * Cache提供了反复注册的功能。Cache是一种缓存机制，可以借助Cache实现监听。
     * 简单来说，Cache在客户端缓存了znode的各种状态，当感知到zk集群的znode状态变化，会触发event事件，注册的监听器会处理这些事件。
     *
     * Watcher 监听器比较简单，只有一种。Cache事件监听的种类有3种Path Cache，Node Cache，Tree Cache。
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        /**
         * 缓存
         * PathChildrenCache是用来监听指定节点的子节点变化情况
         * 传入Curator端和想要监听的路径
         * cacheData表示是否将监听变化的节点内容缓存。
         * 如果设置为true的话，客户端在接收到节点列表发生变化的同时，也能够获取到节点的数据内容。
         */
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        /**
         * 构造一个子节点缓存监听器PathChildrenCacheListener实例
         */
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener(){
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        logger.info("子节点增加, path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                        break;
                    case CHILD_UPDATED:
                        logger.info("子节点更新, path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                        break;
                    case CHILD_REMOVED:
                        logger.info("子节点删除, path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                        break;
                    default:
                        break;
                }
                List<String> serviceAddresses = client.getChildren().forPath(servicePath);
                //更新map
                SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }
}
