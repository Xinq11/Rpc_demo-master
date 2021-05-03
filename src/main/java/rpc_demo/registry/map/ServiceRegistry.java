package rpc_demo.registry.map;

public interface ServiceRegistry {
    <T> void register(T service);
    Object getService(String serviceName);
}
