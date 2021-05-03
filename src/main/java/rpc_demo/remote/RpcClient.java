package rpc_demo.remote;

import rpc_demo.util.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
