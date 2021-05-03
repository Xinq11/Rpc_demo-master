package rpc_demo.Interface;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class HelloServiceImpl implements HelloService{
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    public String hello(HelloObject object) {
        return "这是调用的返回值，id = " + object.getId();
    }
}
