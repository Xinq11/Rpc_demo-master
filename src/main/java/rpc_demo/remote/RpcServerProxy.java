package rpc_demo.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.util.RpcRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;

public class RpcServerProxy {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerProxy.class);

    public static Object serverspi(Class clazz, RpcRequest rpcRequest) throws IllegalAccessException, InvocationTargetException {

        ServiceLoader load = ServiceLoader.load(clazz);
        Iterator iterator = load.iterator();
        Object re = null;
        while(iterator.hasNext()){
            Method method = null;
            Object service = iterator.next();
            try {
                method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            } catch (NoSuchMethodException e) {
                logger.info("方法没有找到");
            }
            re = method.invoke(service,rpcRequest.getParameters());
        }
        return re;
    }
}
