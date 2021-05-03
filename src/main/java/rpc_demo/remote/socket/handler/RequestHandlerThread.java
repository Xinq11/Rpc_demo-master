package rpc_demo.remote.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc_demo.registry.map.ServiceRegistry;
import java.net.Socket;

/**
 * 处理线程，接受对象
 */
public class RequestHandlerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    //private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;

    //public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
    //    this.socket = socket;
    //    this.requestHandler = requestHandler;
    //   this.serviceRegistry = serviceRegistry;
    //}
    @Override
    public void run() {
//        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());){
//            RpcRequest request = (RpcRequest)objectInputStream.readObject();
//            String interfaceName = request.getInterfaceName();
//            Object service = serviceRegistry.getService(interfaceName);
//            Object result = requestHandler.handler(request,service);
//            objectOutputStream.writeObject(RpcResponse.success(result));
//            objectOutputStream.flush();
//        }catch (IOException | ClassNotFoundException e){
//            logger.info("调用或发送时有错误发生");
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
    }
}
