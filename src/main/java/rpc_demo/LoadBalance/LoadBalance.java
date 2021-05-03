package rpc_demo.LoadBalance;

import java.util.List;

public interface LoadBalance {
    String selectServiceAddress(List<String> serviceUrlList,String rpcServiceName);
}
