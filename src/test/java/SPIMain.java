/*
    SPI机制 服务端找到远程调用实现类的方法
 */
import rpc_demo.Interface.HelloObject;
import rpc_demo.Interface.HelloService;
import java.util.ServiceLoader;

public class SPIMain {
    public static void main(String[] args) {
        HelloObject xq = new HelloObject(1, "xq");
        ServiceLoader<HelloService> load = ServiceLoader.load(HelloService.class);
        String re = "";
        for(HelloService s:load){
            System.out.println(s);
            re = s.hello(xq);
        }

        System.out.println(re);
    }
}
