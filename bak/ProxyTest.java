
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        UserImpl user = new UserImpl();
//        user.show();
//        静态代理
//        User userProxy = new UserProxy(user);
//        userProxy.show();

//        动态代理
//        classloader,要代理的接口，要做的事情
        InvocationHandler userInvocationHandler = new UserInvocationHandler(user);
        User userProxy = (User)Proxy.newProxyInstance(user.getClass().getClassLoader(),
//                user.getClass().getInterfaces(),
                new Class<?>[]{User.class},
                userInvocationHandler);
        userProxy.create();
    }
}
