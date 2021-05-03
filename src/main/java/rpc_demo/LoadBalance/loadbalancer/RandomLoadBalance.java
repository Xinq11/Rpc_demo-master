package rpc_demo.LoadBalance.loadbalancer;

import rpc_demo.LoadBalance.LoadBalance;
import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> serviceUrlList, String rpcServiceName) {
        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            return null;
        }
        if (serviceUrlList.size() == 1) {
            return serviceUrlList.get(0);
        }
        Random random = new Random();
        return serviceUrlList.get(random.nextInt(serviceUrlList.size()));
    }
}
