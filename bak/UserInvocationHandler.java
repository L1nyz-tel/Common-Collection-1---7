import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserInvocationHandler implements InvocationHandler {
    User user;
    public UserInvocationHandler(){}
    public UserInvocationHandler(User user){
        this.user = user;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用了" + method.getName());
        method.invoke(user,args);
        return null;
    }
}